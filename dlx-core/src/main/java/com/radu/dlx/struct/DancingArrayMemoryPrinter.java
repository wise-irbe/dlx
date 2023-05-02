package com.radu.dlx.struct;

import com.radu.dlx.Utilities;

import java.util.ArrayList;
import java.util.List;

public final class DancingArrayMemoryPrinter {
    private static final int NAME_WIDTH = 9;
    private static final int VALUE_WIDTH = 6;
    private StringBuilder result;

    private DancingArrayMemoryPrinter() {

    }

    public static DancingArrayMemoryPrinter create() {
        return new DancingArrayMemoryPrinter();
    }

    public String print(DancingArray array) {
        result = new StringBuilder();

        layoutItems(array);
        layoutHeader(array);
        if (array instanceof ColoredDancingArray) {
            coloredLayoutOption((ColoredDancingArray) array);
        } else {
            layoutOption(array);
        }
        return result.toString();
    }

    private void layoutItems(DancingArray array) {
        Row i = new Row("i");
        Row name = new Row("name(i)");
        Row llink = new Row("llink(i)");
        Row rlink = new Row("rlink(i)");

        i.add(printName(i.name));
        name.add(printName(name.name));
        llink.add(printName(llink.name));
        rlink.add(printName(rlink.name));

        for (int idx = 0, len = array.items.length; idx < len; idx++) {
            i.add(printNumber(idx));
            name.add(printValue(array.items[idx]));
            llink.add(printNumber(array.left[idx]));
            rlink.add(printNumber(array.right[idx]));
        }
        printLayout(array.itemTotal, i, name, llink, rlink);
    }

    private void layoutHeader(DancingArray array) {
        Row x = new Row("x");
        Row lenx = new Row("len(x)");
        Row ulinkx = new Row("ulink(x)");
        Row dlinkx = new Row("dlink(x)");

        x.add(printName(x.name));
        lenx.add(printName(lenx.name));
        ulinkx.add(printName(ulinkx.name));
        dlinkx.add(printName(dlinkx.name));

        for (int idx = 0, len = array.items.length; idx < len; idx++) {
            x.add(printNumber(idx));
            lenx.add(printNumber(array.len[idx]));
            ulinkx.add(printNumber(array.up[idx]));
            dlinkx.add(printNumber(array.down[idx]));
        }
        printLayout(array.itemTotal, x, lenx, ulinkx, dlinkx);
    }

    private void layoutOption(DancingArray array) {
        Row x = new Row("x");
        Row topx = new Row("top(x)");
        Row ulinkx = new Row("ulink(x)");
        Row dlinkx = new Row("dlink(x)");

        for (int j = array.itemTotal; j < array.optionTotal; j++) {
            if (j % array.itemTotal == 0) {
                x.add(printName(x.name));
                topx.add(printName(topx.name));
                ulinkx.add(printName(ulinkx.name));
                dlinkx.add(printName(dlinkx.name));
            }
            x.add(printNumber(j));
            topx.add(printNumber(array.top[j]));
            ulinkx.add(printNumber(array.up[j]));
            dlinkx.add(printNumber(array.down[j]));
        }
        printLayout(array.itemTotal, x, topx, ulinkx, dlinkx);
    }

    private void coloredLayoutOption(ColoredDancingArray array) {
        Row x = new Row("x");
        Row topx = new Row("top(x)");
        Row ulinkx = new Row("ulink(x)");
        Row dlinkx = new Row("dlink(x)");
        Row colorx = new Row("color(x)");

        x.add(printName(x.name));
        topx.add(printName(topx.name));
        ulinkx.add(printName(ulinkx.name));
        dlinkx.add(printName(dlinkx.name));
        colorx.add(printName(colorx.name));

        for (int j = array.itemTotal; j < array.optionTotal; j++) {
            x.add(printNumber(j));
            topx.add(printNumber(array.top[j]));
            ulinkx.add(printNumber(array.up[j]));
            dlinkx.add(printNumber(array.down[j]));
            colorx.add(printValue(Utilities.decode(array.colors[j])));
        }
        printLayout(array.itemTotal, x, topx, ulinkx, dlinkx, colorx);
    }


    private void printLayout(int colNum, Row... rows) {
        int pos = 0;
        int size = rows[0].size();
        int cnt = rows.length * rows[0].size();

        while (cnt > 0) {
            for (int k = 0; k < rows.length; k++) {
                for (int j = 0; j < colNum + 1; j++) {
                    if (pos < size) {
                        printCol(result, k, pos, rows);
                    }
                    pos++;
                    cnt--;
                }
                if (k < rows.length - 1) {
                    pos = pos - colNum - 1;
                }
                result.append('\n');
            }
        }
    }

    private void printCol(StringBuilder result, int k, int l, Row[] rows) {
        result.append(rows[k].get(l));
    }

    private String printName(String str) {
        return printName(str, NAME_WIDTH);
    }

    private String printNumber(int i) {
        return printName(Integer.toString(i), VALUE_WIDTH);
    }

    private String printValue(String str) {
        return printName(str, VALUE_WIDTH);
    }

    private String printName(String str, int width) {
        if (str == null) {
            return " ".repeat(width);
        }
        int strLength = str.length();

        if (strLength < width) {
            int diff = width - str.length();
            String prefix = " ".repeat(diff / 2);
            String suffix = " ".repeat(diff - diff / 2);
            return prefix + str + suffix;
        }
        if (strLength > width) {
            return str.substring(0, width);
        }
        return str;
    }

    private static class Row {
        final String name;

        Row(String name) {
            this.name = name;
        }

        private final List<String> values = new ArrayList<>();

        public void add(String val) {
            values.add(val);
        }

        public String get(int i) {
            return values.get(i);
        }

        public int size() {
            return values.size();
        }
    }
}
