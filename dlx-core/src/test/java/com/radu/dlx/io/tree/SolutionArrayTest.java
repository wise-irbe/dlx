package com.radu.dlx.io.tree;

import com.radu.dlx.struct.DancingArrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolutionArrayTest {

    @Test
    public void whenEmptyTree_hasRootNode() {
        SolutionTree tree = SolutionArray.createStoring(DancingArrays.build()
                .forItems()
                .withOption()
                .build(), 1);
        assertTrue(tree.isEmpty());
    }

    @Test
    public void whenOneOptionAdded_AddedAsActiveNode() {
        SolutionTree tree = SolutionArray.createStoring(DancingArrays.build()
                .forItems("item1")
                .withOption("item1")
                .build(), 1);
        tree.advance(1);
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.getActiveBranch().length);
        assertEquals("SolutionArray[activeBranch=[1]]", tree.toString());
    }

    @Test
    public void whenNewOptionAddedAtAnotherLevel_AddedAsActiveNode() {
        SolutionTree tree = SolutionArray.createStoring(DancingArrays.build()
                .forItems("item1", "item2")
                .withOption("item1", "item2")
                .build(), 1);
        tree.advance(1);
        tree.advance(2);
        assertFalse(tree.isEmpty());
        assertEquals(2, tree.getActiveBranch().length);
        assertEquals("SolutionArray[activeBranch=[1, 2]]", tree.toString());
    }

    @Test
    public void whenOptionAddedAtSameLevel_OldBranchInactive() {
        SolutionTree tree = SolutionArray.createStoring(DancingArrays.build()
                .forItems("item1", "item2", "item3")
                .withOption("item1", "item2", "item3")
                .build(), 1);
        tree.advance(1);
        tree.advance(2);
        tree.backup();
        tree.advance(3);
        assertFalse(tree.isEmpty());
        assertEquals(2, tree.getActiveBranch().length);
        assertEquals("SolutionArray[activeBranch=[1, 3]]", tree.toString());
        int option = tree.backup();
        assertEquals("SolutionArray[activeBranch=[1]]", tree.toString());
    }

    @Test
    public void whenOptionAddedAtLowerActiveLevel_OldBranchInActive() {
        SolutionTree tree = SolutionArray.createStoring(DancingArrays.build()
                .forItems("item1", "item2", "item3", "item4")
                .withOption("item1", "item2", "item3", "item4")
                .build(), 1);
        tree.advance(1);
        tree.advance(2);
        tree.advance(3);
        tree.backup();
        tree.backup();
        tree.advance(4);
        assertFalse(tree.isEmpty());
        assertEquals(2, tree.getActiveBranch().length);
        assertEquals("SolutionArray[activeBranch=[1, 4]]", tree.toString());
    }

    @Test
    public void whenOptionAddedAtLowerActiveLevel_OldBranchInActiveTwice() {
        SolutionTree tree = SolutionArray.createStoring(DancingArrays.build()
                .forItems("item1", "item2", "item3", "item4", "item5")
                .withOption("item1", "item2", "item3", "item4", "item5")
                .build(), 1);
        tree.advance(1);
        tree.advance(2);
        tree.advance(3);
        tree.advance(4);
        tree.backup();
        tree.backup();
        tree.backup();
        tree.advance(5);
        tree.backup();
        tree.advance(5);
        assertFalse(tree.isEmpty());
        assertEquals(2, tree.getActiveBranch().length);
        assertEquals("SolutionArray[activeBranch=[1, 5]]", tree.toString());
    }
}