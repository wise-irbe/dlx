package com.radu.dlx.algo;

import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.struct.DancingStructure;

public interface AlgorithmListener {
    void beforeStart(DancingStructure struct, SolutionTree tree);

    void afterAdvance(DancingStructure struct, SolutionTree tree);

    void beforeStore(DancingStructure struct, SolutionTree tree);

    void afterEnd(DancingStructure struct, SolutionTree tree);

    AlgorithmListener SILENT = new AlgorithmListener() {
        @Override
        public void beforeStart(DancingStructure struct, SolutionTree tree) {

        }

        @Override
        public void afterAdvance(DancingStructure struct, SolutionTree tree) {

        }

        @Override
        public void beforeStore(DancingStructure struct, SolutionTree tree) {

        }

        @Override
        public void afterEnd(DancingStructure struct, SolutionTree tree) {

        }
    };
}
