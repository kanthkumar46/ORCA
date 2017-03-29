package edu.orca.algorithm;

import edu.immutablessupport.styles.BuilderStyle;
import edu.jgraphtsupport.AbstractEdge;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Array;
import org.immutables.value.Value;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by KanthKumar on 2/23/17.
 */
@Value.Immutable
@BuilderStyle
public abstract class OrcaGraph {
    public abstract int getEdgesCount();
    public abstract int getNodesCount();
    public abstract int[ ] getNodesDegree();
    public abstract Array<AbstractEdge> getEdges();

    @Value.Derived
    public int[ ][ ] getAdjacencyList() {
        int n = getNodesCount();
        int[ ] deg = getNodesDegree();
        int[ ][ ] adj = new int[n][ ];

        IntStream.range(0, n).forEach(i -> adj[i] = new int[deg[i]]);

        int[ ] d = new int[n];
        getEdges().forEach(edge -> {
            int a = edge.getSourceVertexId();
            int b = edge.getTargetVertexId();
            adj[a][d[a]] = b;
            adj[b][d[b]] = a;
            d[a]++;
            d[b]++;
        });

        IntStream.range(0, n).forEach(i -> Arrays.sort(adj[i]));

        return adj;
    }

    @Value.Derived
    public Tuple2<Integer, Integer>[ ][ ] getIncidentList() {
        int n = getNodesCount();
        int[ ] deg = getNodesDegree();
        Tuple2[ ][ ] inc = new Tuple2[n][ ];

        IntStream.range(0, n).forEach(i -> inc[i] = new Tuple2[deg[i]]);

        int[ ] d = new int[n];
        getEdges().zipWithIndex().forEach(edgeIndexTuple -> {
            int a = edgeIndexTuple._1.getSourceVertexId();
            int b = edgeIndexTuple._1.getTargetVertexId();
            inc[a][d[a]] = Tuple.of(b, edgeIndexTuple._2);
            inc[b][d[b]] = Tuple.of(a, edgeIndexTuple._2);
            d[a]++;
            d[b]++;
        });

        IntStream.range(0, n).forEach(i -> Arrays.sort(inc[i]));

        return (Tuple2<Integer, Integer>[ ][ ]) inc;
    }
}
