package bimaru.logic.local;

import bimaru.logic.PitchInterface;

import java.io.BufferedReader;
import java.io.StringReader;

public class Pitch implements PitchInterface {
    public static final int X_DIMENSION = 6;
    public static final int Y_DIMENSION = 6;

    private char[] field = new char[Y_DIMENSION * X_DIMENSION];
    private String additionalInfo;
    private int[] lineConstraints = new int[Y_DIMENSION];
    private int[] columnConstraints = new int[X_DIMENSION];

    public Pitch(String rawPitch) {
        try
        {
            BufferedReader reader = new BufferedReader(new StringReader(rawPitch));
            int mode = 0; // state machine
            int y = 0;
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.isBlank())
                {
                    continue;
                }

                if ((mode == 0) && line.trim().equals("123456"))
                {
                    // optional column-description
                    continue;
                }

                if ((mode == 0 || mode == 2) && line.trim().equals("+------+"))
                {
                    mode++;
                    continue;
                }

                if (mode == 1 &&
                        Character.isDigit(line.charAt(0)) &&
                        line.charAt(1) == '|' &&
                        line.charAt(8) == '|' &&
                        Character.isDigit(line.charAt(9)))
                {
                    for (int i = 0; i < X_DIMENSION; i++)
                    {
                        switch (line.charAt(2 + i))
                        {
                            case ' ':
                            case 'O':
                            case 'X':
                                this.field[y * X_DIMENSION + i] = line.charAt(2 + i);
                                break;
                            default:
                                throw new IllegalArgumentException( "invalid sign in line '" + line + "' at position " + i);
                        }
                    }

                    lineConstraints[y] = Integer.parseInt(String.valueOf(line.charAt(9)));

                    y++;
                    if (y == 6)
                    {
                        mode++;
                    }

                    continue;
                }

                if (mode == 3 && line.startsWith("  "))
                {
                    for (int i = 0; i < X_DIMENSION; i++)
                    {
                        if (!Character.isDigit(line.charAt(2 + i)))
                        {
                            throw new IllegalArgumentException("invalid column constraints");
                        }

                        this.columnConstraints[i] = Integer.parseInt(line.substring(2 + i, 2 + i + 1));
                    }

                    mode++;
                    continue;
                }

                if (mode == 4)
                {
                    additionalInfo += line + System.lineSeparator();
                    continue;
                }

                throw new IllegalArgumentException("invalid line start in line '" + line + "'");
            }

            additionalInfo = additionalInfo!=null ? additionalInfo.trim() : null;
        }
        catch (Exception exc)
        {
            throw new IllegalArgumentException("unexpected data processed", exc);
        }
    }

    public char[] getField() {
        return field;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public int[] getLineConstraints() {
        return lineConstraints;
    }

    public int[] getColumnConstraints() {
        return columnConstraints;
    }


    public int calcIndex(int x, int y) {
        return (x - 1) + (y - 1) * X_DIMENSION;
    }

    public int toggle(int x, int y) {
        return toggle(calcIndex(x, y));
    }

    public int toggle(int index) {
        return field[index] = switch (field[index])
        {
            case ' ' -> 'O';
            case 'O' -> 'X';
            case 'X' -> ' ';
            default -> ' ';
        };
    }

    public boolean isSolved() {
        for (int i = 0; i < Y_DIMENSION; i++)
        {
            int lineCount = 0;
            for (int j = 0; j < X_DIMENSION; j++)
            {
                if (this.field[i * X_DIMENSION + j] == 'X')
                {
                    lineCount++;
                }
            }

            if (lineCount != this.lineConstraints[i])
            {
                return false;
            }
        }

        for (int i = 0; i < X_DIMENSION; i++)
        {
            int rowCount = 0;
            for (int j = 0; j < Y_DIMENSION; j++)
            {
                if (this.field[j * X_DIMENSION + i] == 'X')
                {
                    rowCount++;
                }
            }

            if (rowCount != this.columnConstraints[i])
            {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(100);
        builder.append("  123456 \n");
        builder.append(" +------+\n");
        for (int i = 0; i < field.length; i++)
        {
            if (i % X_DIMENSION == 0)
            {
                builder.append((i / X_DIMENSION) + 1);
                builder.append('|');
            }

            builder.append(this.field[i]);

            if ((i % X_DIMENSION) == X_DIMENSION - 1)
            {
                builder.append("|");
                builder.append(lineConstraints[i / X_DIMENSION]);
                builder.append("\n");
            }
        }
        builder.append(" +------+\n");
        builder.append("  ");
        for (var constraint : columnConstraints)
        {
            builder.append(constraint);
        }

        builder.append("\n");
        builder.append(additionalInfo);
        builder.append("\n");

        var returnValue = builder.toString();
        return returnValue;
    }
}
