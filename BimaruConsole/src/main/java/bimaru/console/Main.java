package bimaru.console;

import bimaru.logic.PitchProviderInterface;
import bimaru.logic.local.RawPitchProvider;
import bimaru.logic.remote.RemotePitchProvider;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // use the corresponding line for demonstration
//        PitchProviderInterface provider = new RawPitchProvider();     // local-call
        PitchProviderInterface provider = new RemotePitchProvider();    // remote-call

        var pitch = provider.getNextPitch();
        Scanner sc = new Scanner(System.in);

        String command = "";
        do
        {
            if ( command.equals("quit") )
                break;

            if ( !command.isBlank() ) {
                var parts = command.split(" ");
                if ( parts.length==2 ) {
                    try {
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        var index = pitch.toggle(x, y);
                        System.out.printf("Field at index %d set\n\n", index);
                        if ( pitch.isSolved() ) {
                            System.out.println("congratulations you won");
                            break;
                        }
                    }
                    catch ( NumberFormatException e ) {
                        // not an int --> skip the command
                    }
                }
            }

            System.out.println(pitch.toString());
            System.out.print("toggle: ");
        }
        while ( (command=sc.nextLine()) != null );
    }
}
