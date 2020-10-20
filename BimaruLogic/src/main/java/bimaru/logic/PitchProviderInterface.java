package bimaru.logic;

public interface PitchProviderInterface {
    PitchInterface getNextPitch();
    void close();
}
