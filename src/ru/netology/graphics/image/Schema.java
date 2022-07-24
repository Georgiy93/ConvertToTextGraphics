package ru.netology.graphics.image;

import java.util.ArrayList;
import java.util.Arrays;

public class Schema implements TextColorSchema {
    public char c;
    ArrayList<Character> tableSym = new ArrayList<>
            (Arrays.asList('@', 'W', '$', 'K', 'I', '*', '^'));

    @Override
    public char convert(int color) {

        c = tableSym.get(color / (256 / tableSym.size()));
        return c;


    }


}
