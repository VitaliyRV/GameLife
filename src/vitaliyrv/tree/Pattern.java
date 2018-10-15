package vitaliyrv.tree;

import java.util.ArrayList;
import java.util.List;

public final class Pattern {
    public enum Names {
        GALAXY(0), PLANER_GUN(1);

        private final int value;

        Names(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static List<String> LIST = new ArrayList<>();

    static {
        LIST.add("111111011," +
                "111111011," +
                "000000011," +
                "110000011," +
                "110000011," +
                "110000011," +
                "110000000," +
                "110111111," +
                "110111111"
        );
        LIST.add("000000000000000000000000100000000000," +
                "000000000000000000000010100000000000," +
                "000000000000110000001100000000000011," +
                "000000000001000100001100000000000011," +
                "110000000010000010001100000000000000," +
                "110000000010001011000010100000000000," +
                "000000000010000010000000100000000000," +
                "000000000001000100000000000000000000," +
                "000000000000110000000000000000000000"
        );

    }

}
