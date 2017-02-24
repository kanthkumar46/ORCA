package com.orca.algorithm;

import javaslang.Tuple;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Orbit Counting Algorithm for generating signature vector/graphlet degree vector
 * for 4 or 5 node graphlets
 *
 * @author Kanth Kumar Dayanand
 */
public class Orca {

    private OrbitCounter orbitCounter;

    public void init(int graphletSize, String graphFilePath) throws FileNotFoundException {
        if (graphletSize != 4 && graphletSize != 5) {
            throw new IllegalArgumentException("Incorrect graphlet size " + graphletSize + ". Should be 4 or 5.");
        }

        Scanner scanner = new Scanner(new File(graphFilePath));

        // read input graph
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int a, b;
        int[ ] deg = new int[n];
        ImmutableGraph.Builder graphBuilder = ImmutableGraph.builder()
                .nodesCount(n)
                .edgesCount(m);

        for (int i = 0; i < m; i++) {
            a = scanner.nextInt();
            b = scanner.nextInt();
            if (!(0 <= a && a < n) || !(0 <= b && b < n)) {
                throw new RuntimeException("Node ids should be between 0 and n-1.");
            }
            if (a == b) {
                throw new RuntimeException("Self loops (edge from x to x) are not allowed.");
            }
            deg[a]++;
            deg[b]++;
            graphBuilder.addEdges(Tuple.of(a, b));
        }

        ImmutableGraph graph = graphBuilder.nodesDegree(deg).build();

        int maxDegree = IntStream.of(deg).max().orElse(0);
        System.out.println("nodes: " + n);
        System.out.println("edges: " + m);
        System.out.println("max degree: " + maxDegree);

        if (graph.getEdges().stream().distinct().count() != m) {
            throw new RuntimeException("Input file contains duplicate undirected edges.");
        }

        if(graphletSize == 4) {
            this.orbitCounter = new FourNodeGraphletOrbitCounter(graph);
        } else {
            this.orbitCounter = new FiveNodeGraphletOrbitCounter(graph);
        }
    }

    public long[ ][ ] count() {
        return orbitCounter.count();
    }

}
