package com.radu.dlx.struct;

public class DancingArrayBuilder {

    public static final String INITIAL_EXACT_COVER = "    i      0     1     2     3     4     5     6     7     8   \n" +
            " name(i)         a     b     c     d     e     f     g         \n" +
            "llink(i)   7     0     1     2     3     4     5     6     8   \n" +
            "rlink(i)   1     2     3     4     5     6     7     0     8   \n" +
            "    x      0     1     2     3     4     5     6     7     8   \n" +
            " len(x)    0     2     2     2     3     2     2     3     0   \n" +
            "ulink(x)   0     20    24    17    27    28    22    29    0   \n" +
            "dlink(x)   0     12    16    9     13    10    18    14    10  \n" +
            "    x      9     10    11    12    13    14    15    16    17  \n" +
            " top(x)    3     5     -1    1     4     7     -2    2     3   \n" +
            "ulink(x)   3     5     9     1     4     7     12    2     9   \n" +
            "dlink(x)   17    28    14    20    21    25    18    24    3   \n" +
            "    x      18    19    20    21    22    23    24    25    26  \n" +
            " top(x)    6     -3    1     4     6     -4    2     7     -5  \n" +
            "ulink(x)   6     16    12    13    18    20    16    14    24  \n" +
            "dlink(x)   22    22    1     27    6     25    2     29    29  \n" +
            "    x      27    28    29    30  \n" +
            " top(x)    4     5     7     -6  \n" +
            "ulink(x)   21    10    25    27  \n" +
            "dlink(x)   4     5     7     0   \n";

    public static DancingArray buildExactCover() {
        return DancingArrays.build()
                .forItems("a", "b", "c", "d", "e", "f", "g")
                .withOption()
                .withOption("c", "e")
                .withOption("a", "d", "g")
                .withOption("b", "c", "f")
                .withOption("a", "d", "f")
                .withOption("b", "g")
                .withOption("d", "e", "g")
                .build();//1 spacer + 7 headers + 16 options + 6 spacers = 31
    }

    public static DancingArray buildLangfordPairN3() {
        return DancingArrays.build()
                .forItems("1", "2", "3", "s1", "s2", "s3", "s4", "s5", "s6")
                .withOption()
                .withOption("1", "s1", "s3")
                .withOption("1", "s2", "s4")
                .withOption("1", "s3", "s5")
                .withOption("1", "s4", "s6")
                .withOption("2", "s1", "s4")
                .withOption("2", "s2", "s5")
                .withOption("2", "s3", "s6")
                .withOption("3", "s1", "s5")
                .withOption("3", "s2", "s6")
                .build();//1 spacer + 7 headers + 16 options + 6 spacers = 31
    }

    public static DancingArray buildExactCoverProblem() {
        return DancingArrays.build()
                .forItems("A", "B", "C", "D", "E", "F", "G")
                .withOption()
                .withOption("C", "E")
                .withOption("A", "D", "G")
                .withOption("B", "C", "F")
                .withOption("A", "D", "F")
                .withOption("B", "G")
                .withOption("D", "E", "G")
                .build();
    }

    public static DancingArray buildExactCoverProblem2() {
        return DancingArrays.build()
                .forItems("A", "B", "C", "D", "E", "F", "G")
                .withOption("C", "E", "F")
                .withOption("A", "D", "G")
                .withOption("B", "C", "F")
                .withOption("A", "D")
                .withOption("B", "G")
                .withOption("D", "E", "G")
                .build();
    }

    public static DancingArray buildExactCoverProblemWithOptions() {
        return DancingArrays.build()
                .forItems("A", "B", "C", "D")
                .withSecondaryItems("E", "F", "G")
                .withOption("C", "E")
                .withOption("A", "D", "G")
                .withOption("B", "C", "F")
                .withOption("A", "D", "F")
                .withOption("B", "G")
                .withOption("D", "E", "G")
                .build();
    }

}
