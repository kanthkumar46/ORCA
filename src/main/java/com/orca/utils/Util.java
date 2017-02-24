package com.orca.utils;

import javaslang.Tuple;
import javaslang.collection.Array;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;

/**
 * Created by KanthKumar on 2/22/17.
 */
public class Util {

    public static boolean adjacent(int[ ] adj, int y) {
        return Arrays.binarySearch(adj, y) >= 0;
    }

    public static Tuple createTuple(int a, int b) {
        return Tuple.of(NumberUtils.min(a, b), NumberUtils.max(a, b));
    }

    public static Tuple createTuple(int x, int y, int z) {
        Array arr = Array.of(x, y, z).sorted();
        return Tuple.of(arr.get(0), arr.get(1), arr.get(2));
    }
}
