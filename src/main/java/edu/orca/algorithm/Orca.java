package edu.orca.algorithm;

import edu.jgraphtsupport.AbstractEdge;
import edu.jgraphtsupport.AbstractVertex;
import edu.jgraphtsupport.GraphUtils;
import javaslang.collection.Array;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.UndirectedGraph;

import java.util.stream.IntStream;

/**
 * Orbit Counting Algorithm for generating signature vector/graphlet degree vector
 * for 4 or 5 node graphlets
 *
 * @author Kanth Kumar Dayanand
 */
public class Orca {

    private static final Logger LOGGER = LogManager.getLogger(Orca.class);
    private OrbitCounter orbitCounter;

    /**
     * Initializer method
     *
     * @param graphletSize size of graphlets over which signature of a node needs to be calculated (either 4 or 5)
     * @param graph undirected simple graph (no loops and multiple edge between vertices)
     * @param <T> vertex or node type
     * @param <U> edge type
     */
    public <T extends AbstractVertex, U extends AbstractEdge<T>> void init(int graphletSize, UndirectedGraph<T, U> graph) {
        if (graphletSize != 4 && graphletSize != 5) {
            throw new IllegalArgumentException("Incorrect graphlet size " + graphletSize + ". Should be 4 or 5.");
        }

        int n = GraphUtils.getMaxVertexId(graph).getOrElse(-1) + 1;
        int m = graph.edgeSet().size();
        int[ ] deg = new int[n];

        graph.vertexSet().forEach(vertex -> {
            int vertexId = vertex.getVertexId();
            deg[vertexId] = graph.degreeOf(vertex);
        });

        Array<AbstractEdge> edges = Array.ofAll(graph.edgeSet());
        OrcaGraph immutableGraph = ImmutableOrcaGraph.builder()
                .nodesCount(n)
                .edgesCount(m)
                .edges(edges)
                .nodesDegree(deg).build();

        int maxDegree = IntStream.of(deg).max().orElse(0);
        LOGGER.debug("nodes: " + n);
        LOGGER.debug("edges: " + m);
        LOGGER.debug("max degree: " + maxDegree);

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
