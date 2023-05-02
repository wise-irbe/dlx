package com.radu.dlx.struct;

import org.junit.jupiter.api.Test;

import static com.radu.dlx.struct.DancingArrayBuilder.INITIAL_EXACT_COVER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DancingNucleusTest {

    @Test
    public void whenExactCoverInitialized_StructureCorrect() {
        DancingArray nucleus = DancingArrayBuilder.buildExactCover();
        assertEquals("x=9 is option\n" +
                " c e (1 of 2)", nucleus.printer().describe(9, true));
        testNucleus(INITIAL_EXACT_COVER, nucleus);
    }

    private void testNucleus(String expectedTable, DancingArray nucleus) {
        assertEquals(expectedTable, nucleus.toString());
    }

    @Test
    public void whenCovered_UncoverEqualToInitialized() {
        DancingArray nucleus = DancingArrayBuilder.buildExactCover();
        nucleus.cover(1);
        nucleus.uncover(1);
        testNucleus(INITIAL_EXACT_COVER, nucleus);
    }

    @Test
    public void whenCovered_StateCorrect() {
        DancingArray nucleus = DancingArrayBuilder.buildExactCover();
        nucleus.cover(1);
        testNucleus(
                "    i      0     1     2     3     4     5     6     7     8   \n" +
                        " name(i)         a     b     c     d     e     f     g         \n" +
                        "llink(i)   7     0     0     2     3     4     5     6     8   \n" +
                        "rlink(i)   2     2     3     4     5     6     7     0     8   \n" +
                        "    x      0     1     2     3     4     5     6     7     8   \n" +
                        " len(x)    0     2     2     2     1     2     1     2     0   \n" +
                        "ulink(x)   0     20    24    17    27    28    18    29    0   \n" +
                        "dlink(x)   0     12    16    9     27    10    18    25    10  \n" +
                        "    x      9     10    11    12    13    14    15    16    17  \n" +
                        " top(x)    3     5     -1    1     4     7     -2    2     3   \n" +
                        "ulink(x)   3     5     9     1     4     7     12    2     9   \n" +
                        "dlink(x)   17    28    14    20    21    25    18    24    3   \n" +
                        "    x      18    19    20    21    22    23    24    25    26  \n" +
                        " top(x)    6     -3    1     4     6     -4    2     7     -5  \n" +
                        "ulink(x)   6     16    12    4     18    20    16    7     24  \n" +
                        "dlink(x)   6     22    1     27    6     25    2     29    29  \n" +
                        "    x      27    28    29    30  \n" +
                        " top(x)    4     5     7     -6  \n" +
                        "ulink(x)   4     10    25    27  \n" +
                        "dlink(x)   4     5     7     0   \n",
                nucleus);
    }

}