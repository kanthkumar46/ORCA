package edu.orca.algorithm;

import edu.jgraphtsupport.AbstractEdge;
import javaslang.Tuple2;
import javaslang.collection.Array;

/**
 * Created by KanthKumar on 2/22/17.
 */
public abstract class OrbitCounter {
    protected final OrcaGraph hostGraph;
    protected final int m; //edges count
    protected final int n; //nodes count
    protected final int[ ] deg; //deg[x] - degree of node x
    protected Array<AbstractEdge> edges;

    protected final int[ ][ ] adj; // adj[x] - adjacency list of node x
    protected final Tuple2<Integer, Integer>[ ][ ] inc; // inc[x] - incidence list of node x: (y, edge id)

    protected long[ ][ ] orbit; // orbit[x][o] - how many times does node x participate in orbit o

    public OrbitCounter(OrcaGraph graph) {
        this.hostGraph = graph;
        this.n = graph.getNodesCount();
        this.m = graph.getEdgesCount();
        this.deg = graph.getNodesDegree();
        this.edges = graph.getEdges();
        this.adj = graph.getAdjacencyList();
        this.inc = graph.getIncidentList();
    }

    public abstract long[ ][ ] count();

    public int[ ] countTriangles() {
        int[ ] tri = new int[m];
        edges.zipWithIndex().forEach(edgeIndexTuple -> {
            int x = edgeIndexTuple._1.getSourceVertexId();
            int y = edgeIndexTuple._1.getTargetVertexId();
            for (int xi = 0, yi = 0; xi < deg[x] && yi < deg[y];) {
                if (adj[x][xi] == adj[y][yi]) {
                    tri[edgeIndexTuple._2]++;
                    xi++;
                    yi++;
                } else if (adj[x][xi] < adj[y][yi]) {
                    xi++;
                } else {
                    yi++;
                }
            }
        });
        return tri;
    }

}
