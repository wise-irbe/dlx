package com.radu.dlx.struct;

public interface StructurePrinter {
    /**
     * @param item item idex
     * @return Item name
     */
    String printItem(int item);
    /**
     * Print option id, which is equivalent to the id of the item connected to the option via up/down links
     *
     * @param option option idx
     * @return option id (option.id == option.item.id)
     */
    String printOption(int option);
    /**
     * @param element option
     * @param verbose if <span>true</span>, will produce more verbose description
     * @return Description of the option and its context in the row
     */
    String describe(int element, boolean verbose);

    /**
     * Print all the items connected to the option left-to-right + the option itself.
     *
     * @param optionIdx option idx
     * @return item list
     */
    String printOptionItemList(int optionIdx);

    /**
     * @param solution solution array
     * @return solution description
     */
    String printCurrentSolution(int[] solution);

    /**
     * @return Memory state
     */
    String printMemory();
}
