package edu.orca.utils;

import javaslang.Tuple;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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
        return IntStream.of(x, y, z).boxed().sorted()
                .collect(collectingAndThen(toList(), list -> Tuple.of(list.get(0), list.get(1), list.get(2))));
    }
}
