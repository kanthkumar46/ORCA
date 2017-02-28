package com.orca.algorithm;

import com.jgraphtsupport.Edge;
import com.jgraphtsupport.GraphUtils;
import com.jgraphtsupport.Vertex;
import com.orca.utils.TestSetup;
import javaslang.collection.Array;
import org.apache.commons.io.IOUtils;
import org.jgrapht.UndirectedGraph;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Unit tests for ORCA
 *
 * @author Kanth Kumar Dayanand
 */
public class OrcaTest extends TestSetup {

    private Orca orca = new Orca();

    @Test
    public void orca_4NodeGraphlet_100NodeGraph_Test() throws IOException {
        UndirectedGraph<Vertex, Edge> graph = generateGraph("/test_graphs/example.in");
        orca.init(4, graph);
        long[ ][ ] vector = orca.count();

        Array<String> actualResult = Array.of(vector)
                .map(longs -> Array.ofAll(longs).map(String::valueOf).mkString(" "));

        List<String> expectedResult = IOUtils.readLines(getResourceStream("/signature_vector/example_4node.out"),
                Charset.defaultCharset());

        Assert.assertTrue(actualResult.eq(expectedResult));
    }

    @Test
    public void orca_4NodeGraphlet_1kNodeGraph_Test() throws IOException {
        UndirectedGraph<Vertex, Edge> graph = generateGraph("/test_graphs/p_05.in");
        orca.init(4, graph);
        long[ ][ ] vector = orca.count();

        Array<String> actualResult = Array.of(vector)
                .map(longs -> Array.ofAll(longs).map(String::valueOf).mkString(" "));

        List<String> expectedResult = IOUtils.readLines(getResourceStream("/signature_vector/p_05_4node.out"),
                Charset.defaultCharset());

        Assert.assertTrue(actualResult.eq(expectedResult));
    }

    @Test
    public void orca_4NodeGraphlet_10kNodeGraph_Test() throws IOException {
        UndirectedGraph<Vertex, Edge> graph = generateGraph("/test_graphs/graph_10k_60k.in");
        orca.init(4, graph);
        long[ ][ ] vector = orca.count();

        Array<String> actualResult = Array.of(vector)
                .map(longs -> Array.ofAll(longs).map(String::valueOf).mkString(" "));

        List<String> expectedResult = IOUtils.readLines(getResourceStream("/signature_vector/graph_10k_60k_4node.out"),
                Charset.defaultCharset());

        Assert.assertTrue(actualResult.eq(expectedResult));
    }

    @Test
    public void orca_5NodeGraphlet_100NodeGraph_Test() throws IOException {
        UndirectedGraph<Vertex, Edge> graph = generateGraph("/test_graphs/example.in");
        orca.init(5, graph);
        long[ ][ ] vector = orca.count();

        Array<String> actualResult = Array.of(vector)
                .map(longs -> Array.ofAll(longs).map(String::valueOf).mkString(" "));

        List<String> expectedResult = IOUtils.readLines(getResourceStream("/signature_vector/example_5node.out"),
                Charset.defaultCharset());

        Assert.assertTrue(actualResult.eq(expectedResult));
    }

    @Test
    public void orca_5NodeGraphlet_1kNodeGraph_Test() throws IOException {
        UndirectedGraph<Vertex, Edge> graph = generateGraph("/test_graphs/graph_1k_6k.in");
        orca.init(5, graph);
        long[ ][ ] vector = orca.count();

        Array<String> actualResult = Array.of(vector)
                .map(longs -> Array.ofAll(longs).map(String::valueOf).mkString(" "));

        List<String> expectedResult = IOUtils.readLines(getResourceStream("/signature_vector/graph_1k_6k_5node.out"),
                Charset.defaultCharset());

        Assert.assertTrue(actualResult.eq(expectedResult));
    }

    @Test
    public void orca_5NodeGraphlet_10kNodeGraph_Test() throws IOException {
        UndirectedGraph<Vertex, Edge> graph = generateGraph("/test_graphs/graph_10k_20k.in");
        orca.init(5, graph);
        long[ ][ ] vector = orca.count();

        Array<String> actualResult = Array.of(vector)
                .map(longs -> Array.ofAll(longs).map(String::valueOf).mkString(" "));

        List<String> expectedResult = IOUtils.readLines(getResourceStream("/signature_vector/graph_10k_20k_5node.out"),
                Charset.defaultCharset());

        Assert.assertTrue(actualResult.eq(expectedResult));
    }

    private InputStream getResourceStream(String file){
        return OrcaTest.class.getResourceAsStream(file);
    }

    private UndirectedGraph<Vertex, Edge> generateGraph(String file) throws IOException {
        List<String> edgeList = IOUtils.readLines(getResourceStream(file), Charset.defaultCharset());
        edgeList.remove(0);

        return GraphUtils.convert(edgeList);
    }
}
