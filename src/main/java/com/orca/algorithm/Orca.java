package com.orca.algorithm;

import com.jgraphtsupport.Edge;
import com.jgraphtsupport.GraphUtils;
import com.jgraphtsupport.Vertex;
import javaslang.collection.Array;
import org.jgrapht.UndirectedGraph;

import java.util.stream.IntStream;

/**
 * Orbit Counting Algorithm for generating signature vector/graphlet degree vector
 * for 4 or 5 node graphlets
 *
 * @author Kanth Kumar Dayanand
 */
public class Orca {

    private OrbitCounter orbitCounter;

    public void init(int graphletSize, UndirectedGraph<Vertex, Edge> graph) {
        if (graphletSize != 4 && graphletSize != 5) {
            throw new IllegalArgumentException("Incorrect graphlet size " + graphletSize + ". Should be 4 or 5.");
        }

        int n = GraphUtils.getMaxVertexId(graph).orElse(-1) + 1;
        int m = graph.edgeSet().size();
        int[ ] deg = new int[n];

        graph.vertexSet().forEach(vertex -> {
            int vertexId = vertex.getVertexId();
            deg[vertexId] = graph.degreeOf(vertex);
        });

        Array<Edge> edges = Array.ofAll(graph.edgeSet());
        ImmutableOrcaGraph immutableGraph = ImmutableOrcaGraph.builder()
                .nodesCount(n)
                .edgesCount(m)
                .edges(edges)
                .nodesDegree(deg).build();

        int maxDegree = IntStream.of(deg).max().orElse(0);
        System.out.println("nodes: " + n);
        System.out.println("edges: " + m);
        System.out.println("max degree: " + maxDegree);

        if(graphletSize == 4) {
            this.orbitCounter = new FourNodeGraphletOrbitCounter(immutableGraph);
        } else {
            this.orbitCounter = new FiveNodeGraphletOrbitCounter(immutableGraph);
        }
    }

    public long[ ][ ] count() {
        return orbitCounter.count();
    }

}
