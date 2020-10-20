package bimaru.logic.remote;

import bimaru.logic.PitchInterface;
import bimaru.logic.PitchProviderInterface;
import bimaru.logic.local.Pitch;

import java.io.*;
import java.net.Socket;

public class RemotePitchProvider implements PitchProviderInterface, AutoCloseable {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public RemotePitchProvider() {
        try {
            socket = new Socket("localhost", 8000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PitchInterface getNextPitch() {
        try {
            writer.write(ServerCommands.GET.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RemotePitch(this, readPitchRawPitchFromSocket());
    }

    private String readPitchRawPitchFromSocket() {
        StringBuilder builder = new StringBuilder(100);
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if( line.isEmpty() )
                    break;
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    @Override
    public void close() {
        try {
            if( writer!=null ) {
                writer.write(ServerCommands.QUIT.toString());
                writer.newLine();
                writer.close();
                writer = null;
            }
            if( reader!=null ) {
                reader.close();
                reader = null;
            }
            if( socket!=null ) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pitch sendToggle(final int index) {
        try {
            writer.write(ServerCommands.TOGGLE.toString());
            writer.newLine();
            writer.write(String.valueOf(index));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pitch(readPitchRawPitchFromSocket());
    }

    public boolean sendSolvedCheck() {
        boolean returnValue = false;
        try {
            writer.write(ServerCommands.SOLVED.toString());
            writer.newLine();
            writer.flush();
            var answer = reader.readLine();
            returnValue = Boolean.parseBoolean(answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
