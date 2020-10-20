package bimaru.logic.remote;

import bimaru.logic.PitchInterface;
import bimaru.logic.local.Pitch;

public class RemotePitch implements PitchInterface {
    private final RemotePitchProvider provider;
    private Pitch pitch;


    public RemotePitch(RemotePitchProvider provider, String rawField) {
        this.provider = provider;
        this.pitch = new Pitch(rawField);
    }

    @Override
    public char[] getField() {
        return pitch.getField();
    }

    @Override
    public String getAdditionalInfo() {
        return pitch.getAdditionalInfo();
    }

    @Override
    public int[] getLineConstraints() {
        return pitch.getLineConstraints();
    }

    @Override
    public int[] getColumnConstraints() {
        return pitch.getColumnConstraints();
    }

    @Override
    public int toggle(int x, int y) {
        return toggle( pitch.calcIndex(x, y) );
    }

    @Override
    public int toggle(int index) {
        pitch = provider.sendToggle(index);
        return index;
    }

    @Override
    public boolean isSolved() {
        return provider.sendSolvedCheck();
    }

    @Override
    public String toString() {
        return pitch.toString();
    }
}
