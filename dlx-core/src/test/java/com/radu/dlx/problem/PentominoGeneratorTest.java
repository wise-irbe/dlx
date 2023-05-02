package com.radu.dlx.problem;

import com.radu.dlx.Solver;
import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.problem.polyomino.PentominoGenerator;
import com.radu.dlx.problem.polyomino.Polyomino;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PentominoGeneratorTest {

    @Test
    public void shouldCreateAllPossiblePentominosFor20Times3Board() {
        Board<Polyomino> generator = PentominoGenerator.createPentominoBoard();
        Iterator<Polyomino> it = generator.iterator();
        Map<String, Integer> counts = new HashMap<>();
        Map<String, Set<String>> description = new HashMap<>();
        Map<String, List<String>> descriptionList = new HashMap<>();
        int count = 0;
        while (it.hasNext()) {
            Polyomino polyomino = it.next();
            count++;
            String name = polyomino.name();
            String content = polyomino.toString();
            if (counts.containsKey(name)) {
                counts.put(name, counts.get(name) + 1);
                description.get(name).add(content);
                descriptionList.get(name).add(content);
            } else {
                counts.put(name, 1);
                Set<String> vals = new TreeSet<>();
                vals.add(content);
                description.put(name, vals);
                List<String> valList = new ArrayList<>();
                valList.add(content);
                descriptionList.put(name, valList);
            }
        }
        assertEquals(718, count);
        assertEquals(44, descriptionList.get("U").size());//110 (ok)->80 (k 44)         //ok
        assertEquals(12, descriptionList.get("W").size());//72 (ok)->12 (k 12)          //ok
        assertEquals(28, descriptionList.get("R").size());//144 (ok) -> 28 (k 28)        //ok
        assertEquals(48, descriptionList.get("O").size());//48                           //ok
        assertEquals(130, descriptionList.get("S").size());//136 (ok) -> 130 (k 128)    ??
        assertEquals(132, descriptionList.get("Q").size());//136 (ok) -> (k 132)        //ok
        assertEquals(16, descriptionList.get("T").size());//72 (ok) -> 16 (k 16)         //ok
        assertEquals(4, descriptionList.get("V").size());//72 (ok)-> 16 (ok) -> 4(ok)        //OK
        assertEquals(4, descriptionList.get("X").size());//18 (ok)->4 (k 4)                 //ok
        assertEquals(130, descriptionList.get("Y").size());//136 (ok) -> 130 (k 128)        ??
        assertEquals(154, descriptionList.get("P").size());//220 (ok)-> 154 (k 156)         ??
        assertEquals(16, descriptionList.get("Z").size());//72 (ok) ->  16 (k 16)                //ok
    }

    @Test
    public void shouldBuildPentominoSolution() {
        Solver solver = Solver.builder()
                .maxSolutionCount(1000)
                .withDebug()
                .writeToStdOut()
                .forPentomino()
                .forAlgorithm()
                .withDotTreePrinter()
                .withStacklessAlgo()
                .build();
        SolutionTree tree = solver.solve();
        assertTrue(solver.isDebug());
        assertEquals(2, tree.getSolutionCount());
        List<String> solutions = solver.printSolution().collect(Collectors.toList());
        assertEquals("669:V->0h->0i->0j->1j->2j,670:X->11->02->12->22->13,675:U->00->10->20->01->21,132:P->03->04->14->05->15,35:O->23->24->25->26->27,213:Z->06->16->17->18->28,270:Y->07->08->09->19->0a,225:W->29->1a->2a->0b->1b,660:T->2b->0c->1c->2c->2d,240:R->0d->1d->1e->2e->1f,614:S->0e->0f->0g->1g->1h,451:Q->2f->2g->2h->1i->2i", solutions.get(0));
        assertEquals("669:V->0h->0i->0j->1j->2j,670:X->11->02->12->22->13,675:U->00->10->20->01->21,132:P->03->04->14->05->15,35:O->23->24->25->26->27,217:Z->0g->1g->1h->1i->2i,318:Y->2e->1f->2f->2g->2h,227:W->1d->2d->0e->1e->0f,659:T->0b->0c->1c->2c->0d,237:R->19->0a->1a->1b->2b,399:Q->06->16->07->08->09,630:S->17->18->28->29->2a", solutions.get(1));
    }
}