package com.radu.dlx.io.tree;

import com.radu.dlx.struct.DancingArray;
import com.radu.dlx.struct.DancingArrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolutionTreeTest {

    @Test
    public void whenEmptyTree_hasRootNode() {
        DancingArray torus = DancingArrays.build().build();

        SolutionTree tree = OfIntSolutionTree.createStoring(torus, 1);
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.getActiveBranch().length);
        assertEquals("", tree.toString());
    }

    @Test
    public void whenOneOptionAdded_AddedAsActiveNode() {
        DancingArray array = DancingArrays.build()
                .forItems("item")
                .withOption("item")
                .build();
        assertEquals("    i      0     1     2   \n" +
                " name(i)        item       \n" +
                "llink(i)   1     0     2   \n" +
                "rlink(i)   1     0     2   \n" +
                "    x      0     1     2   \n" +
                " len(x)    0     1     0   \n" +
                "ulink(x)   0     3     0   \n" +
                "dlink(x)   0     3     3   \n" +
                "    x      3     4   \n" +
                " top(x)    1     -1  \n" +
                "ulink(x)   1     3   \n" +
                "dlink(x)   1     0   \n", array.toString());
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(3);
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.getActiveBranch().length);
        assertEquals("            [root]  \n" +
                "[0:item]<-\n", tree.toString());
    }

    @Test
    public void whenNewOptionAddedAtAnotherLevel_AddedAsActiveNode() {
        DancingArray array = DancingArrays.build()
                .forItems("item", "item2")
                .withOption("item")
                .withOption("item2")
                .build();
        assertEquals("    i      0     1     2     3   \n" +
                " name(i)        item item2       \n" +
                "llink(i)   2     0     1     3   \n" +
                "rlink(i)   1     2     0     3   \n" +
                "    x      0     1     2     3   \n" +
                " len(x)    0     1     1     0   \n" +
                "ulink(x)   0     4     6     0   \n" +
                "dlink(x)   0     4     6     4   \n" +
                "    x      4     5     6     7   \n" +
                " top(x)    1     -1    2     -2  \n" +
                "ulink(x)   1     4     2     6   \n" +
                "dlink(x)   1     6     2     0   \n", array.toString());
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(4);
        tree.advance(6);
        assertFalse(tree.isEmpty());
        assertEquals(2, tree.getActiveBranch().length);
        assertEquals("                        [root]   \n" +
                "           [0:item]<- \n" +
                "[1:item2]<-\n", tree.toString());
    }

    @Test
    public void whenOptionAddedAtSameLevel_OldBranchInactive() {
        DancingArray array = DancingArrays.build()
                .forItems("item1", "item2", "item3")
                .withOption("item1", "item2", "item3")
                .withOption("item2", "item3")
                .withOption("item3", "item1").build();
        assertEquals("    i      0     1     2     3     4   \n" +
                " name(i)       item1 item2 item3       \n" +
                "llink(i)   3     0     1     2     4   \n" +
                "rlink(i)   1     2     3     0     4   \n" +
                "    x      0     1     2     3     4   \n" +
                " len(x)    0     2     2     3     0   \n" +
                "ulink(x)   0     13    9     12    0   \n" +
                "dlink(x)   0     5     6     7     7   \n" +
                "    x      5     6     7     8     9   \n" +
                " top(x)    1     2     3     -1    2   \n" +
                "ulink(x)   1     2     3     5     6   \n" +
                "dlink(x)   13    9     10    10    2   \n" +
                "    x      10    11    12    13    14  \n" +
                " top(x)    3     -2    3     1     -3  \n" +
                "ulink(x)   7     9     10    5     12  \n" +
                "dlink(x)   12    13    3     1     0   \n", array.toString());
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(6);
        tree.advance(9);
        tree.backup();
        tree.advance(12);
        assertFalse(tree.isEmpty());
        assertEquals("                                                           [root]          \n" +
                "                         [0:item1->item2->item3]<-\n" +
                "   [2:item3->item1]<-       ->[1:item2->item3]    \n", tree.toString());
        assertEquals(2, tree.getActiveBranch().length);
    }

    @Test
    public void whenOptionAddedAtLowerActiveLevel_OldBranchInActive() {
        DancingArray array = DancingArrays.build()
                .forItems("item1", "item2", "item3", "item4")
                .withOption("item1", "item2")
                .withOption("item2", "item3")
                .withOption("item3", "item4")
                .withOption("item1", "item4")
                .build();
        assertEquals("    i      0     1     2     3     4     5   \n" +
                " name(i)       item1 item2 item3 item4       \n" +
                "llink(i)   4     0     1     2     3     5   \n" +
                "rlink(i)   1     2     3     4     0     5   \n" +
                "    x      0     1     2     3     4     5   \n" +
                " len(x)    0     2     2     2     2     0   \n" +
                "ulink(x)   0     15    9     12    16    0   \n" +
                "dlink(x)   0     6     7     10    13    7   \n" +
                "    x      6     7     8     9     10    11  \n" +
                " top(x)    1     2     -1    2     3     -2  \n" +
                "ulink(x)   1     2     6     7     3     9   \n" +
                "dlink(x)   15    9     10    2     12    13  \n" +
                "    x      12    13    14    15    16    17  \n" +
                " top(x)    3     4     -3    1     4     -4  \n" +
                "ulink(x)   10    4     12    6     13    15  \n" +
                "dlink(x)   3     16    16    1     4     0   \n", array.toString());
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(6);
        tree.advance(9);
        tree.advance(12);
        tree.backup();
        tree.backup();
        tree.advance(15);
        assertFalse(tree.isEmpty());
        assertEquals(2, tree.getActiveBranch().length);
        assertEquals("                                          [root]      \n" +
                "                  [0:item1->item2]<-\n" +
                "[3:item1->item4]<-->[1:item2->item3]\n" +
                "                                                      ->[2:item3->item4]\n", tree.toString());
    }

    @Test
    public void whenOptionAdded3Times_BranchHasThreeNodes() {
        DancingArray array = DancingArrays.build()
                .forItems("item1", "item2", "item3")
                .withOption("item1", "item3")
                .withOption("item2")
                .build();
        assertEquals("    i      0     1     2     3     4   \n" +
                " name(i)       item1 item2 item3       \n" +
                "llink(i)   3     0     1     2     4   \n" +
                "rlink(i)   1     2     3     0     4   \n" +
                "    x      0     1     2     3     4   \n" +
                " len(x)    0     1     1     1     0   \n" +
                "ulink(x)   0     5     8     6     0   \n" +
                "dlink(x)   0     5     8     6     6   \n" +
                "    x      5     6     7     8     9   \n" +
                " top(x)    1     3     -1    2     -2  \n" +
                "ulink(x)   1     3     5     2     8   \n" +
                "dlink(x)   1     3     8     2     0   \n", array.toString());
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(6);
        tree.advance(5);
        tree.advance(8);
        assertFalse(tree.isEmpty());
        assertEquals(3, tree.getActiveBranch().length);
        //item1
        //item4
        assertEquals("                                                            [root]      \n" +
                "                                    [0:item1->item3]<-\n" +
                "                  [0:item1->item3]<-\n" +
                "   [1:item2]<-    \n", tree.toString());
    }

    @Test
    public void whenOptionAddedAtLowerActiveLevel_OldBranchInActiveTwice() {
        DancingArray array = DancingArrays.build()
                .forItems("item1", "item2", "item3", "item4", "item5")
                .withOption("item1", "item5")
                .withOption("item2", "item3")
                .withOption("item1", "item2", "item3", "item4", "item5")
                .build();
        assertEquals("    i      0     1     2     3     4     5     6   \n" +
                " name(i)       item1 item2 item3 item4 item5       \n" +
                "llink(i)   5     0     1     2     3     4     6   \n" +
                "rlink(i)   1     2     3     4     5     0     6   \n" +
                "    x      0     1     2     3     4     5     6   \n" +
                " len(x)    0     2     2     2     1     2     0   \n" +
                "ulink(x)   0     13    14    15    16    17    0   \n" +
                "dlink(x)   0     7     10    11    16    8     8   \n" +
                "    x      7     8     9     10    11    12    13  \n" +
                " top(x)    1     5     -1    2     3     -2    1   \n" +
                "ulink(x)   1     5     7     2     3     10    7   \n" +
                "dlink(x)   13    17    11    14    15    17    1   \n" +
                "    x      14    15    16    17    18  \n" +
                " top(x)    2     3     4     5     -3  \n" +
                "ulink(x)   10    11    4     8     13  \n" +
                "dlink(x)   2     3     4     5     0   \n", array.toString());
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(10);
        tree.advance(11);
        tree.advance(7);
        tree.advance(11);
        tree.backup();
        tree.backup();
        tree.backup();
        tree.advance(8);
        tree.backup();
        tree.advance(16);
        assertFalse(tree.isEmpty());
        assertEquals(2, tree.getActiveBranch().length);
        //item1
        //item4
        assertEquals("                                                                                              [root]                 \n" +
                "                                                 [1:item2->item3]<-           \n" +
                "[2:item1->item2->item3->item4->item5]<-          ->[0:item1->item5]                     ->[1:item2->item3]           \n" +
                "                                                                                                                                                                      ->[0:item1->item5]           \n" +
                "                                                                                                                                                                                                             ->[1:item2->item3]           \n", tree.toString());
    }
}