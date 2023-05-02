package com.radu.dlx.io.storer;

import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.struct.DancingStructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Solutions will be stored in a binary format as two files.
 * One file will contain the index and the other file will contain the solutions.
 * <p>
 * The files, if they exists, will be truncated and access pattern will be append or read only.
 * Currently the storer cannot be shared by threads and should only be accessed by one thread.
 * <p>
 * Memory structure of the index:
 * <p>
 * |LIST_COUNT 4 bytes = LIST count N| LIST 0 POS OFFSET l_0_0 4 bytes| LIST 2 POS OFFSET list_2_0 4 bytes | ... | LIST_ {N-1} POS OFFSET l_{N-1}_0|
 * <p>
 * Memory structure of the solution data:
 * <p>
 * |LIST l_0 LEN 4 bytes = LIST len M | ELEM l_0_0 4 bytes | ... | ELEM l_0_{M-1} 4 bytes|
 * ...
 * |LIST l_{N-1} LEN 4 bytes = LIST len M_{N-1}| ELEM l_{N-1}_0 4 bytes | ... | ELEM l_{N-1}_{M - 1} 4 bytes|
 * <p>
 * Currently we do not utilize any compression or mathematical properties of {@link DancingStructure} for reducing file size.
 * If we were to go concurrent, we can assign pages for each thread. Also we can use unsigned integers.
 * <p>
 * If each solution has 10 items, our file memory consumption will be for 1 000 000 000 solutions, where each solution has 10 options:
 * Index: 4 + 1 000 000 000 * 4 = 4 000 000 004 bytes
 * Solution: 1 000 000 000 * (4 + 10*4) =  44 000 000 000 bytes
 * <p>
 * For smaller problems, where we can be sure that the max solution option index is <=2^16 - 1 and solution list can only have <=2^8 - 1 elements,
 * we can use a byte for list length and a short for option index.
 * <p>
 * If each solution has 10 items, our file memory consumption will be for 1 000 000 000 solutions, where each solution has 10 options:
 * * Index: 4 + 1 000 000 000 * 4 = 4 000 000 004 bytes
 * * Solution: 1 000 000 000 * (1 + 10*2) =  21 000 000 000 bytes
 * <p>
 * Parallelization idea:
 * store the memory state of best branching point and send these points along with the branch id to a thread
 */
public class MemoryMappedFileIntStorer implements SolutionStorer {
    private static final int PAGE_SIZE = 1_000_000;//1MB page as minimum map size

    private static final int ENTRY_LEN = 32;
    private static final int SEP_LEN = 1;
    private final RandomAccessFile solutionFile;
    private final List<Integer> solutionReadPos = new ArrayList<>();
    private final int size;
    private final RandomAccessFile indexFile;
    private final FileChannel solutionChannel;
    private final FileChannel indexChannel;
    private final MappedByteBuffer solutionBuff;
    private final MappedByteBuffer indexBuff;
    private int solutionPosition;
    private int listIndexPosition;
    private final int listCountPosition = 0;

    public MemoryMappedFileIntStorer(SolutionStorer storer) throws IOException {
        solutionFile = new RandomAccessFile("calc.out", "rwd");
        solutionFile.setLength(0L);//we reset the file
        solutionChannel = solutionFile.getChannel();
        solutionChannel.position(0);
        solutionBuff = solutionChannel.map(FileChannel.MapMode.READ_WRITE, 0, PAGE_SIZE);

        indexFile = new RandomAccessFile("calc.idx", "rwd");
        indexFile.setLength(0L);//we reset the file
        indexChannel = solutionFile.getChannel();
        indexChannel.position(0);
        indexBuff = indexChannel.map(FileChannel.MapMode.READ_WRITE, 0, PAGE_SIZE);

        size = storer.getSolutionCount();

        solutionPosition = 0;
    }

    private static class Block {
        int position;
    }

    private static class Index extends Block {
        int count;
        List<Integer> lists;
    }

    private static class Solution extends Block {
        int size;
        int[] list;

        public Solution(int[] solution, int position) {
            list = solution;
            size = list.length;
            this.position = position;
        }
    }

    private int updateIndex() {
        return 0;
    }

    private void updateIndexHeader() {

    }

    private Solution addSolution(SolutionTree solution, int position) {
        return new Solution(solution.getActiveBranch(), position);
    }

    @Override
    public void store(SolutionTree tree) {
        updateIndexHeader();
        listIndexPosition = listIndexPosition + 1;
        listIndexPosition = updateIndex();
        Solution solutionBlock = addSolution(tree, solutionPosition);
        solutionPosition += solutionBlock.size;//new position for new write
    }

    @Override
    public Stream<int[]> getSolutions() {
        return null;
    }

    @Override
    public int[] getFirstSolution() {
        return null;
    }

    @Override
    public int getSolutionCount() {
        return 0;
    }
}
