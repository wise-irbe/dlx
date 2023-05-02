package com.radu.dlx.struct;

import com.radu.dlx.Utilities;
import com.radu.dlx.list.OfIntArrayList;
import com.radu.dlx.list.OfObjectArrayList;
import com.radu.dlx.problem.ExactCoveringProblem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Will create a {@see DancingStructure} implementation. DancingArray implmementation is default.
 * If item name begins with '#', a sharp reference heuristic will be used.
 * If item name has :${color} after name, ColoredDancingNucleus will be created.
 * Builders will ensure that duplicate items and options will not be created.
 * Items will be created in FIFO order.
 */
public final class DancingArrays {

    private DancingArrays(){}

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {
        public final OfIntArrayList left = new OfIntArrayList();
        public final OfIntArrayList right = new OfIntArrayList();
        public final OfIntArrayList up = new OfIntArrayList();
        public final OfIntArrayList down = new OfIntArrayList();
        public final OfIntArrayList len = new OfIntArrayList();
        public final OfIntArrayList top = new OfIntArrayList();
        public final OfIntArrayList colors = new OfIntArrayList();
        public final OfObjectArrayList<String> items = new OfObjectArrayList<>(String.class);
        public boolean hasColors = false;
        private int pos = 0;
        private int spacer = 0;
        public final Map<String, Integer> itemIdToIdx = new HashMap<>();

        void move() {
            pos++;
        }

        Builder() {
        }

        public ItemBuilder forItems(String... items) {
            return new ItemBuilder(this, new OptionBuilder(this)).push(items);
        }

        public ItemBuilder forItems(List<String> items) {
            return new ItemBuilder(this, new OptionBuilder(this)).push(items);
        }

        public DancingArray build() {
            int itemNum = left.size();
            int optionNum = top.size();
            return new DancingArray(
                    spacer,
                    left.toArray(itemNum),
                    right.toArray(itemNum),
                    up.toArray(optionNum),
                    down.toArray(optionNum),
                    len.toArray(optionNum),
                    top.toArray(optionNum),
                    items.toArray(itemNum));
        }

        public ColoredDancingArray buildColored() {
            int itemNum = left.size();
            int optionNum = top.size();
            return new ColoredDancingArray(
                    spacer,
                    left.toArray(itemNum),
                    right.toArray(itemNum),
                    up.toArray(optionNum),
                    down.toArray(optionNum),
                    len.toArray(optionNum),
                    top.toArray(optionNum),
                    items.toArray(itemNum),
                    colors.toArray(optionNum));
        }
    }


    //TODO: Move these builders?
    public static class OptionBuilder {

        private final Builder builder;
        int optRow = 0;

        OptionBuilder(Builder builder) {
            this.builder = builder;
        }

        public OptionBuilder withOption(String... ids) {
            if (builder.pos == Integer.MAX_VALUE) {
                throw new IllegalArgumentException();
            }
            if (ids == null || ids.length == 0) {
                return this;
            }
            //spacer
            int firstPos = builder.pos;

            for (int i = 0, optLen = ids.length; i < optLen; i++) {
                builder.move();
                String id = ids[i];

                updateOptionItem(id);

                int col = builder.itemIdToIdx.get(id);
                //setting header
                builder.len.set(col, builder.len.get(col) + 1);
                //top[col] = pos;
                builder.top.set(builder.pos, col);

                if (builder.down.get(col) == 0) {
                    connectHeader(col);
                } else {
                    connectOption(col);
                }

                builder.up.set(col, builder.pos);
            }
            connectSpacer(firstPos);
            optRow++;
            return this;
        }

        private void updateOptionItem(String id) {
            int colorPos = id.indexOf(':');
            if (colorPos > 0 && colorPos < id.length() - 1) {
                if (!builder.hasColors) {
                    builder.hasColors = true;
                }
                builder.colors.set(builder.pos, Utilities.encode(id.substring(colorPos + 1)));
            } else {
                builder.colors.set(builder.pos, 0);
            }
        }

        private void connectHeader(int col) {
            builder.down.set(col, builder.pos);
            builder.down.set(builder.pos, col);
            builder.up.set(builder.pos, col);
        }

        private void connectOption(int col) {
            builder.up.set(builder.pos, builder.up.get(col));
            builder.down.set(builder.up.get(col), builder.pos);
            builder.down.set(builder.pos, builder.top.get(builder.down.get(col)));
        }

        private void connectSpacer(int firstPos) {
            //prev spacer
            builder.down.set(firstPos, builder.pos);
            //spacer
            builder.move();
            builder.spacer++;
            builder.top.set(builder.pos, -builder.spacer);
            builder.up.set(builder.pos, firstPos + 1);
        }

        public DancingArray build() {
            return builder.build();
        }

        public ColoredDancingArray buildColored() {
            return builder.buildColored();
        }

        public OptionBuilder pushAll(List<String[]> optionsList) {
            for (String[] options : optionsList) {
                withOption(options);
            }
            return this;
        }
    }

    public static class ItemBuilder {


        private final OptionBuilder optionBuilder;
        private final Builder builder;

        private int imax = 0;
        private int secondaryIdx = 0;

        protected ItemBuilder(Builder builder, OptionBuilder optionBuilder) {
            this.optionBuilder = optionBuilder;
            this.builder = builder;
        }

        private void moveItem() {
            imax++;
        }

        private void finish() {
            imax++;
            if (secondaryIdx == 0) {
                secondaryIdx = imax;
            }
            //we make the primary item list circular
            //right[0] = 1;
            builder.left.set(0, secondaryIdx - 1);

            //we make the secondary item list circular against another root for secondary items
            builder.left.set(imax, imax - 1);
            builder.right.set(imax - 1, imax);
            builder.right.set(secondaryIdx - 1, 0);//if secondaryIdx == 0, right[imax - 1] is being overwritten by right[secondaryIdx - 1]

            builder.right.set(imax, secondaryIdx);
            builder.left.set(secondaryIdx, imax);

        }

        private void connect() {
            builder.right.set(imax - 1, imax);
            builder.left.set(imax, imax - 1);
        }

        private void index(String id) {
            builder.itemIdToIdx.put(id, imax);
            builder.items.set(imax, id);
            builder.top.set(imax, imax);
        }

        private void pushInternal(String id) {
            moveItem();
            builder.move();
            connect();
            index(id);
        }

        public OptionBuilder withOption(String... items) {
            if (secondaryIdx - 1 == imax) {
                throw new IllegalStateException("If secondary items are to be added using forSecondary(), they should be added");
            }
            finish();
            builder.move();
            return optionBuilder.withOption(items);
        }

        protected ItemBuilder push(List<String> items) {
            for (String item : items) {
                pushInternal(item);
            }
            return this;
        }

        //rename to push
        protected ItemBuilder push(String... items) {
            for (String item : items) {
                pushInternal(item);
            }
            return this;
        }

        public OptionBuilder withSecondaryItems(List<String> items) {
            if (!items.isEmpty()) {
                startSecondaryItemList();
                push(items);
            }
            return withOption();
        }

        public OptionBuilder withSecondaryItems(String... items) {
            if (items != null && items.length > 0) {
                startSecondaryItemList();
                push(items);
            }
            return withOption();
        }

        private void startSecondaryItemList() {
            secondaryIdx = imax + 1;
        }
    }


    public static ColoredDancingArray colored(ExactCoveringProblem problem) {
        return new Builder()
                .forItems(problem.items)
                .withSecondaryItems(problem.secondaryItems)
                .pushAll(problem.options)
                .buildColored();
    }

    public static DancingArray create(ExactCoveringProblem problem) {
        return new Builder()
                .forItems(problem.items)
                .withSecondaryItems(problem.secondaryItems)
                .pushAll(problem.options)
                .build();
    }
}
