package com.radu.dlx;

import com.radu.dlx.io.progress.PrintableEvent;
import com.radu.dlx.io.progress.StreamListener;
import com.radu.dlx.io.tree.SolutionTree;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolverTest {

    @Test
    public void shouldBuildEmpty() {
        //TODO: maybe we should forbid invocation of build without proper function calls
        assertThrows(IllegalStateException.class, () -> Solver.builder().build());
    }

    @Test
    public void shouldBuildWithDebugAndMaxSolution2() {
        Solver solver = Solver.builder()
                .maxSolutionCount(2)
                .withDebug()
                .forSolver()
                .forLangFordPair(2)
                .forAlgorithm()
                .withStacklessAlgo()
                .build();
        assertTrue(solver.isDebug());
        assertEquals(2, solver.maxSolutionCount());
    }

    @Test
    public void shouldBuildWithProgress() {
        //TODO: refactor solver so that we cannot have empty builder
        class MockListener implements StreamListener {
            PrintableEvent event = null;

            @Override
            public void consume(PrintableEvent event) {
                this.event = event;
            }
        }
        MockListener listener = new MockListener();
        Solver solver = Solver.builder()
                .withDebug()
                .writeInfoToLogger()
                .writeToStdOut()
                .writeTo(listener)
                .forLangfordPair(3)
                .forAlgorithm()
                .withStacklessAlgo()
                .build();
        assertTrue(solver.isDebug());
        assertEquals(1, solver.maxSolutionCount());
        solver.writeProgress(() -> "blaa");
        assertEquals("blaa", listener.event.print());
    }

    @Test
    public void shouldBuildSudokuSolution() {
        //TODO: refactor solver so that we cannot have empty builder
        class MockListener implements StreamListener {
            PrintableEvent m_event = null;

            @Override
            public void consume(PrintableEvent event) {
                m_event = event;
            }
        }
        MockListener listener = new MockListener();
        Solver solver = Solver.builder()
                .withDebug()
                .writeInfoToLogger()
                .writeTo(listener)
                .forSudoku()
                .setBoard(0, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 4, 0, 7, 0, 0, 8, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0, 9, 0, 0, 0, 0, 3, 0, 0, 4, 0, 0, 2, 0, 0, 0, 5, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 6, 0, 0, 0)
                .withStacklessAlgo()
                .build();
        solver.solve();
        assertTrue(solver.isDebug());
        assertEquals(1, solver.maxSolutionCount());
        List<String> solutions = solver.printSolution().collect(Collectors.toList());
        assertEquals("154:p46->r41->c61->b51,160:p48->r48->c88->b68,187:p59->r51->c91->b61,208:p66->r68->c68->b58,197:p62->r64->c24->b44,201:p64->r63->c43->b53,195:p61->r67->c17->b47,163:p51->r55->c15->b45,186:p58->r59->c89->b69,220:p72->r71->c21->b71,94:p31->r31->c11->b11,63:p25->r21->c51->b21,289:p97->r91->c71->b91,240:p79->r78->c98->b98,245:p81->r88->c18->b78,229:p76->r75->c65->b85,227:p73->r79->c39->b79,233:p78->r76->c86->b96,249:p83->r86->c36->b76,283:p93->r94->c34->b74,147:p43->r42->c32->b42,275:p91->r92->c12->b72,144:p42->r43->c23->b43,143:p41->r49->c19->b49,1:p11->r16->c16->b16,168:p52->r56->c26->b46,153:p44->r46->c46->b56,113:p35->r36->c56->b26,26:p15->r18->c58->b28,50:p22->r28->c28->b18,9:p12->r19->c29->b19,280:p92->r97->c27->b77,126:p37->r38->c78->b38,176:p55->r54->c54->b54,252:p85->r82->c52->b82,287:p95->r93->c53->b83,228:p75->r77->c57->b87,258:p86->r89->c69->b89,80:p27->r29->c79->b39,91:p29->r26->c96->b36,109:p34->r39->c49->b29,210:p67->r66->c76->b66,34:p17->r15->c75->b35,59:p24->r25->c45->b25,101:p33->r35->c35->b15,260:p87->r87->c77->b97,302:p99->r99->c99->b99,295:p98->r95->c85->b95,217:p69->r65->c95->b65,212:p68->r62->c82->b62,70:p26->r22->c62->b22,39:p19->r12->c92->b32,29:p16->r14->c64->b24,10:p13->r13->c33->b13,19:p14->r17->c47->b27,55:p23->r27->c37->b17,82:p28->r23->c83->b33,117:p36->r33->c63->b23,132:p38->r37->c87->b37,136:p39->r34->c94->b34,171:p54->r52->c42->b52,182:p56->r57->c67->b57,269:p89->r83->c93->b93,264:p88->r84->c84->b94",
                solutions.get(0));
    }

    @Test
    public void shouldBuildLangfordSolution2() {
        //TODO: refactor solver so that we cannot have empty builder
        class MockListener implements StreamListener {
            PrintableEvent event = null;

            @Override
            public void consume(PrintableEvent event) {
                this.event = event;
            }
        }
        MockListener listener = new MockListener();
        Solver.Builder builder = Solver.builder()
                .withDebug()
                .writeInfoToLogger()
                .writeToStdOut()
                .writeTo(listener)
                .forLangfordPair(3)
                .forAlgorithm()
                .withStacklessAlgo();
        Solver solver = builder.build();
        solver.solve();
        SolutionTree solution = builder.forLangFordPair(3)
                .forAlgorithm()
                .withStacklessAlgo()
                .build()
                .solve();
        assertTrue(solver.isDebug());

        assertEquals(1, solver.maxSolutionCount());//8:1->s2->s4,0:1->s1->s3,2:2->s2->s5
        List<String> solutions = solver.printSolution().collect(Collectors.toList());
        assertEquals("7:3->s1->s5,6:2->s3->s6,1:1->s2->s4", solutions.get(0));
    }

    @Test
    public void shouldBuildExactCoverRecursiveSolution() {
        //TODO: refactor solver so that we cannot have empty builder
        class MockListener implements StreamListener {
            PrintableEvent event = null;

            @Override
            public void consume(PrintableEvent event) {
                this.event = event;
            }
        }
        MockListener listener = new MockListener();
        Solver.ProgressBuilder builder = Solver.builder()
                .withDebug()
                .writeInfoToLogger()
                .writeToStdOut()
                .writeTo(listener);
        Solver solver =
                builder.forExactCover()
                        .addOptionRow("C", "E")
                        .addOptionRow("A", "D", "G")
                        .addOptionRow("B", "C", "F")
                        .addOptionRow("A", "D", "F")
                        .addOptionRow("B", "G")
                        .addOptionRow("D", "E", "G")
                        .forAlgorithm()
                        .withStacklessAlgo()
                        .build();
        solver.solve();
        List<String> solutions = solver.printSolution().collect(Collectors.toList());
        assertEquals("0:C->E,4:B->G,3:A->D->F", solutions.get(0));
    }

    private void compareSolution(String expected, Solver solver, SolutionTree solution) {

    }

    @Test
    public void shouldBuildExactCoverStacklessSolution() {
        //TODO: refactor solver so that we cannot have empty builder
        class MockListener implements StreamListener {
            PrintableEvent event = null;

            @Override
            public void consume(PrintableEvent event) {
                this.event = event;
            }
        }
        MockListener listener = new MockListener();
        Solver.ProgressBuilder builder = Solver.builder()
                .withDebug()
                .writeInfoToLogger()
                .writeToStdOut()
                .writeTo(listener);
        Solver solver =
                builder.forExactCover()
                        .addOptionRow("C", "E")
                        .addOptionRow("A", "D", "G")
                        .addOptionRow("B", "C", "F")
                        .addOptionRow("A", "D", "F")
                        .addOptionRow("B", "G")
                        .addOptionRow("D", "E", "G")
                        .forAlgorithm()
                        .withStacklessAlgo()
                        .build();
        solver.solve();
        List<String> solutions = solver.printSolution().collect(Collectors.toList());
        assertEquals("0:C->E,4:B->G,3:A->D->F", solutions.get(0));
    }

    @Test
    public void shouldBuildExactCoverStacklessSolution2() {
        class MockListener implements StreamListener {
            PrintableEvent event = null;

            @Override
            public void consume(PrintableEvent event) {
                this.event = event;
            }
        }
        MockListener listener = new MockListener();
        Solver.ProgressBuilder builder = Solver.builder()
                .withDebug()
                .writeInfoToLogger()
                .writeToStdOut()
                .writeTo(listener);
        Solver solver = builder
                .forExactCover()
                .addOptionRow("C", "E", "F")
                .addOptionRow("A", "D", "G")
                .addOptionRow("B", "C", "F")
                .addOptionRow("A", "D")
                .addOptionRow("B", "G")
                .addOptionRow("D", "E", "G")
                .forAlgorithm()
                .withStacklessAlgo()
                .build();
        solver.solve();
        solver = builder
                .forExactCover()
                .addOptionRow("C", "E")
                .addOptionRow("A", "D", "G")
                .addOptionRow("B", "C", "F")
                .addOptionRow("A", "D", "F")
                .addOptionRow("B", "G")
                .addOptionRow("D", "E", "G")
                .forAlgorithm()
                .withStacklessAlgo()
                .build();
        solver.solve();//28,21,17
        List<String> solutions = solver.printSolution().collect(Collectors.toList());
        assertEquals("0:C->E,4:B->G,3:A->D->F", solutions.get(0));
    }
}