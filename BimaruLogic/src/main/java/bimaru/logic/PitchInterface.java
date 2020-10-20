package bimaru.logic;

public interface PitchInterface {
    char[] getField();
    String getAdditionalInfo();
    int[] getLineConstraints();
    int[] getColumnConstraints();

    int toggle(int x, int y);
    int toggle(final int index);
    boolean isSolved();
}
