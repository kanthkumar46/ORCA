package com.orca.algorithm;

import javaslang.Tuple;
import javaslang.Tuple2;
import org.immutables.value.Value;

import java.util.Arrays;
import java.util.List;

/**
 * Created by KanthKumar on 2/23/17.
 */
@Value.Immutable
public abstract class Graph {
    public abstract int getEdgesCount();
    public abstract int getNodesCount();
    public abstract int[ ] getNodesDegree();
    public abstract List<Tuple2<Integer, Integer>> getEdges();

    @Value.Derived
    public int[ ][ ] getAdjacencyList() {
        int n = getNodesCount();
        List<Tuple2<Integer, Integer>> edges = getEdges();
        int[ ] deg = getNodesDegree();
        int[ ][ ] adj = new int[n][ ];

        for (int i = 0; i < n; i++) {
            adj[i] = new int[deg[i]];
        }

        int a, b;
        int[ ] d = new int[n];
        for (Tuple2<Integer, Integer> edge : edges) {
            a = edge._1;
            b = edge._2;
            adj[a][d[a]] = b;
            adj[b][d[b]] = a;
            d[a]++;
            d[b]++;
        }

        for (int i = 0; i < n; i++) {
            Arrays.sort(adj[i]);
        }

        return adj;
    }

    @Value.Derived
    public Tuple2<Integer, Integer>[ ][ ] getIncidentList(){
        int n = getNodesCount();
        int m = getEdgesCount();
        List<Tuple2<Integer, Integer>> edges = getEdges();
        int[ ] deg = getNodesDegree();
        Tuple2<Integer, Integer>[ ][ ] inc = new Tuple2[n][ ];

        for (int i = 0; i < n; i++) {
            inc[i] = new Tuple2[deg[i]];
        }

        int a, b;
        int[ ] d = new int[n];
        for (int i = 0; i < m; i++) {
            a = edges.get(i)._1;
            b = edges.get(i)._2;
            inc[a][d[a]] = Tuple.of(b, i);
            inc[b][d[b]] = Tuple.of(a, i);
            d[a]++;
            d[b]++;
        }

        for (int i = 0; i < n; i++) {
            Arrays.sort(inc[i]);
        }

        return inc;
    }
}
