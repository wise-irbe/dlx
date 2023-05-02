package com.radu.dlx.struct;

import java.util.List;

/**
 * Data type for the dancing links algorithm.
 */
public interface DancingStructure {
    void hide(int p);

    void unhide(int p);

    boolean isSpacer(int x);

    int size();

    /**
     * @return number of items in the structure
     */
    int getItemNum();

    /**
     * @return number of options and option headers
     */
    int getOptionAndHeaderNum();

    /**
     * @return number of option rows
     */
    int getOptionRowNum();

    /**
     * @param element option or item or option header
     * @return number of option rows for the corresponding item
     */
    int getOptionRowNum(int element);

    /**
     * @param element option
     * @return option position in row
     */
    int getOptionPositionInRow(int element);

    /**
     * @param optionIdx Option index
     * @return list of item names corresponding to the option
     */
    List<String> getItemList(int optionIdx);

    int getOptionItem(int option);

    /**
     * @return Is empty
     */
    boolean isEmpty();

    /**
     * @return Printer
     */
    StructurePrinter printer();

}
