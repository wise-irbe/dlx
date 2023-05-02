package com.radu.dlx;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuCommandTest {
    private static InputStream stdIn;
    private static PrintStream stdOut;

    private static ByteArrayOutputStream out;

    @BeforeAll
    public static void onInit() {
        stdIn = System.in;
        stdOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    public static void cleanUp() throws IOException {
        out.close();
        System.setIn(stdIn);
        System.setOut(stdOut);
    }

    @Test
    public void whenAcceptsArguments_throw() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> SudokuCommand.main("blaa"));
        assertTrue(thrown.getMessage().contains("Sudoku"));
    }

    @Test
    public void when17ClueSudokuArrayStackless_solved() throws IOException {
        testStacklessSudokuSolver("693784512487512936125963874932651487568247391741398625319475268856129743274836159",
                "000000010400000000020000000000050407008000300001090000300400200050100000000806000");
    }

    @Test
    public void whenPiStacklessSudoku_solved() throws IOException {
        testStacklessSudokuSolver("793412685415638297286579314562183749174956832938724561859261473647395128321847956",
                "003010000" +
                        "415000090" +
                        "206500300" +
                        "500080009" +
                        "070900032" +
                        "038004060" +
                        "000260403" +
                        "000300008" +
                        "320007950");
    }

    @Test
    public void whenSudokuMicroArrayStackless_solved() throws IOException {
        testStacklessSudokuSolver(
                "632957841" +
                        "491682573" +
                        "785341269" +
                        "248579316" +
                        "319264785" +
                        "576813924" +
                        "124795638" +
                        "967438152" +
                        "853126497",
                "002000041" +
                        "000082070" +
                        "000040009" +
                        "200079300" +
                        "010000080" +
                        "006810004" +
                        "100090000" +
                        "060430000" +
                        "850000400");
    }

    @Test
    public void whenSudoku2Stackless_solved() throws IOException {
        testStacklessSudokuSolver(
                "597218364" +
                        "132465897" +
                        "864379125" +
                        "915684732" +
                        "348792651" +
                        "276153489" +
                        "659847213" +
                        "421936578" +
                        "783521946"
                ,
                "000000300" +
                        "100400000" +
                        "000000105" +
                        "900000000" +
                        "000002600" +
                        "000053000" +
                        "050800000" +
                        "000900070" +
                        "083000040");
    }

    @Test
    public void whenSudoku3Stackless_solved() throws IOException {
        testStacklessSudokuSolver(
                "346795812" +
                        "258431697" +
                        "971862543" +
                        "129576438" +
                        "835214769" +
                        "764389251" +
                        "517948326" +
                        "493627185" +
                        "682153974"
                ,
                "000000012008030000000000040120500000000004700060000000507000300000620000000100000");
    }

    @Test
    public void whenSudoku4_solved() throws IOException {
        testStacklessSudokuSolver(
                "913875264584962173627413958879126345431759826256348791348597612192684537765231489"
                ,
                "010005060" +
                        "004900070" +
                        "000003008" +
                        "070006300" +
                        "030009020" +
                        "006048090" +
                        "300500000" +
                        "090004500" +
                        "060200080");
    }

    @Test
    public void whenSudokuStacklessEscargo_solved() throws IOException {
        testStacklessSudokuSolver("162857493534129678789643521475312986913586742628794135356478219241935867897261354",

                "100007090" +
                        "030020008" +
                        "009600500" +
                        "005300900" +
                        "010080002" +
                        "600004000" +
                        "300000010" +
                        "041000007" +
                        "007000300");
    }

    private void testStacklessSudokuSolver(String expected, String problem) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(problem.getBytes())) {
            System.setIn(in);
            String result = new SudokuCommand().solveStackless();
            assertEquals(expected.replace("\n", ""),
                    result.replace("\n", ""));
        }
    }
}
