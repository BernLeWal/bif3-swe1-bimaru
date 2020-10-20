package bimaru.logic.local;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PitchTest {
    @Test
    @DisplayName("ok")
    public void testOk() {
        assertDoesNotThrow( ()->new Pitch("""
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
                """));
    }

    @Test
    @DisplayName("bad border")
    public void testBadBorder() {
        assertThrows( IllegalArgumentException.class, ()->new Pitch("""
                |O    O|2
                |      |1
                |      |1
                |  O   |3
                |      |1
                | X    |2
                +------+
                 212203
                1x3, 2x2, 3x1

                """));
    }

    @Test
    @DisplayName("short line")
    public void testBadLine1() {
        assertThrows( IllegalArgumentException.class, ()->new Pitch("""
                  123456
                 +------+
                1|O  O|2
                2|      |1
                3|      |1
                4|  O   |3
                5|      |1
                6| X    |2
                 +------+
                  212203
                1x3, 2x2, 3x1
                """));
    }

    @Test
    @DisplayName("5 lines only")
    public void testBadLine2() {
        assertThrows( IllegalArgumentException.class, ()->new Pitch("""
                  123456
                 +------+
                1|O    O|2
                2|      |1
                3|      |1
                4|  O   |3
                5|      |1
                 +------+
                  212203
                1x3, 2x2, 3x1
                """));
    }

    @Test
    @DisplayName("no num at end")
    public void testBadLine3() {
        assertThrows( IllegalArgumentException.class, ()->new Pitch("""
                    123456
                   +------+
                  1|O    O|
                  2|      |1
                  3|      |1
                  4|  O   |3
                  5|      |1
                  6| X    |2
                   +------+
                    212203
                  1x3, 2x2, 3x1
                """));
    }

    @Test
    @DisplayName("invalid border at the end")
    public void testBadLine4() {
        assertThrows( IllegalArgumentException.class, ()->new Pitch("""
                      123456
                     +------+
                    1|O    O|2
                    2|      |1
                    3|      |1
                    4|  O   |3
                    5|      |1
                    6| X    |2
                      212203
                    1x3, 2x2, 3x1
                """));
    }

    @Test
    @DisplayName("too few column constraints")
    public void testBadLine5() {
        assertThrows( IllegalArgumentException.class, ()->new Pitch("""
                    12345
                   +-----+
                  1|O    |2
                  2|     |1
                  3|     |1
                  4|  O  |3
                  5|     |1
                  6| X   |2
                   +-----+
                    21220
                  1x3, 2x2, 3x1
                """));
    }

}