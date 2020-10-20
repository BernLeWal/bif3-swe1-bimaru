package bimaru.server;

import bimaru.logic.local.Pitch;
import bimaru.logic.local.RawPitchProvider;
import bimaru.logic.remote.ServerCommands;

import java.io.*;
import java.net.ServerSocket;

public class Main {
    public static void main(String args[]) {
        try {
            ServerSocket listener = new ServerSocket(8000, 5);
            System.out.println("Waiting for clients...");
            while( true ) {
                var socket = listener.accept();
                System.out.println("New client arrived");
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        System.out.println("Start to handle client requests...");
                        RawPitchProvider provider = new RawPitchProvider();
                        Pitch remoteObject = (Pitch)provider.getNextPitch();
                        if ( remoteObject==null )
                            return;
                        try (
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())) )
                        {
                            String message = "";
                            do {
                                System.out.println("waiting for data to read...");
                                message = reader.readLine();
                                System.out.println("received: " + message);
                                switch (ServerCommands.valueOf(message)) {
                                    case GET:
                                        System.out.println("   send pitch");
                                        System.out.println(remoteObject.toString());
                                        writer.write(remoteObject.toString());
                                        writer.newLine();
                                        writer.flush();
                                        break;
                                    case TOGGLE:
                                        System.out.println("   read index");
                                        message = reader.readLine();
                                        System.out.println("   received: " + message);
                                        remoteObject.toggle(Integer.parseInt(message));
                                        System.out.println("   send pitch");
                                        System.out.println(remoteObject.toString());
                                        writer.write(remoteObject.toString());
                                        writer.newLine();
                                        writer.flush();
                                        break;
                                    case SOLVED:
                                        var solvedAnswer = String.valueOf(remoteObject.isSolved());
                                        System.out.printf("   send %s\n", solvedAnswer);
                                        writer.write(solvedAnswer);
                                        writer.newLine();
                                        writer.flush();
                                        break;
                                    case QUIT:
                                    default:
                                        break;
                                }
                            }
                            while (!ServerCommands.QUIT.toString().equals(message) );
                        }
                        catch( IOException e ) {
                            e.printStackTrace();
                        }
                        finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("Client gone.");
                    }
                };
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
