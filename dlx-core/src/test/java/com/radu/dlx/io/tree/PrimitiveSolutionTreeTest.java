package com.radu.dlx.io.tree;

import com.radu.dlx.struct.DancingArray;
import com.radu.dlx.struct.DancingArrayBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrimitiveSolutionTreeTest {

    @Test
    public void whenEmptyTree_hasRootNode() {
        DancingArray array = DancingArrayBuilder.buildExactCover();
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        assertTrue(tree.isEmpty());
    }

    @Test
    public void whenOneOptionAdded_AddedAsActiveNode() {
        DancingArray array = DancingArrayBuilder.buildExactCover();
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(10);
        assertEquals("            [root]  \n" +
                "[0:c->e]<-\n", tree.toString());
    }

    @Test
    public void whenNewOptionAddedAtAnotherLevel_AddedAsActiveNode() {
        DancingArray array = DancingArrayBuilder.buildExactCover();
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(10);
        tree.advance(12);
        assertEquals("                             [root]    \n" +
                "              [0:c->e]<-  \n" +
                "[1:a->d->g]<-\n", tree.toString());
    }

    @Test
    public void whenOptionAddedAtSameLevel_OldBranchInactive() {
        DancingArray array = DancingArrayBuilder.buildExactCover();
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(10);
        tree.advance(12);
        tree.backup();
        tree.advance(16);
        assertEquals("                             [root]    \n" +
                "              [0:c->e]<-  \n" +
                "[2:b->c->f]<-->[1:a->d->g]\n", tree.toString());
    }

    @Test
    public void whenOptionAddedAtLowerActiveLevel_OldBranchInActive() {
        DancingArray array = DancingArrayBuilder.buildExactCover();
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(10);
        tree.advance(12);
        tree.advance(16);
        tree.backup();
        tree.backup();
        tree.advance(20);
        assertEquals("                             [root]    \n" +
                "              [0:c->e]<-  \n" +
                "[3:a->d->f]<-->[1:a->d->g]\n" +
                "                                       ->[2:b->c->f]\n", tree.toString());
    }

    @Test
    public void whenOptionAddedAtLowerActiveLevel_OldBranchInActiveTwice() {
        DancingArray array = DancingArrayBuilder.buildExactCover();
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(10);
        tree.advance(12);
        tree.advance(16);
        tree.advance(20);
        tree.backup();
        tree.backup();
        tree.advance(22);
        //tree.backup();
        //tree.advance(22);
        assertEquals("                                          [root]    \n" +
                "                           [0:c->e]<-  \n" +
                "             [1:a->d->g]<-\n" +
                "[3:a->d->f]<-->[2:b->c->f]\n" +
                "                                       ->[3:a->d->f]\n", tree.toString());
    }

    @Test
    public void whenOptionAddedAtLowerActiveLevel_BranchesCreatedTwiceAtSameLevel() {
        DancingArray array = DancingArrayBuilder.buildExactCover();
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(10);
        tree.advance(12);
        tree.advance(16);
        tree.backup();
        tree.backup();
        tree.advance(20);
        tree.backup();
        tree.advance(22);
        //tree.backup();
        //tree.advance(22);
        assertEquals("                             [root]    \n" +
                "              [0:c->e]<-  \n" +
                "[3:a->d->f]<-->[3:a->d->f]->[1:a->d->g]\n" +
                "                                                    ->[2:b->c->f]\n", tree.toString());
    }

    @Test
    public void whenOptionAddedRemovedAddedAdded() {
        DancingArray array = DancingArrayBuilder.buildExactCover();
        SolutionTree tree = OfIntSolutionTree.createStoring(array, 1);
        tree.advance(10);
        tree.backup();
        tree.advance(16);
        tree.advance(20);
        tree.backup();
        tree.advance(22);
        assertEquals("                             [root]    \n" +
                "             [2:b->c->f]<- ->[0:c->e]  \n" +
                "[3:a->d->f]<-->[3:a->d->f]\n", tree.toString());
    }
}