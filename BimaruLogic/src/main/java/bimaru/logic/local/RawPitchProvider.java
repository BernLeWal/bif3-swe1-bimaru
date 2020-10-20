package bimaru.logic.local;

import bimaru.logic.PitchInterface;
import bimaru.logic.PitchProviderInterface;

public class RawPitchProvider implements PitchProviderInterface, AutoCloseable {
    @Override
    public PitchInterface getNextPitch() {
        return new Pitch(getNextPitchRaw());
    }

    // due to implementing the AutoCloseable interface this method will act as a finalizer
    // see: https://www.baeldung.com/java-finalize
    @Override
    public void close() {
    }

    public String getNextPitchRaw() {
        return """
                  123456
                 +------+
                1|O    O|2
                2|      |1
                3|      |1
                4|  O   |3
                5|      |1
                6| X    |2
                 +------+
                  212203
                1x3, 2x2, 3x1
                
                """;
    }
}
