import graph.Edge;
import graph.Graph;
import graph.kruskal.KruskalAlgorithm;
import graph.prim.PrimAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** Tests Prim's and Kruskal's on the basic graph (without GUI, city nodes etc).
 *  This test is not enough to test your project 3. Perform additional testing using GUI.
 */
public class BasicGraphMSTTest {
    private Graph g;

    @BeforeEach
    public void setUp() {
        // Creates a simple graph with 7 vertices from the lecture slides on MST.
        g = new Graph(7);
        g.addEdge(0, new Edge(0, 1, 2));
        g.addEdge(0, new Edge(0, 2, 8));
        g.addEdge(0, new Edge(0, 3, 4));

        g.addEdge(1, new Edge(1, 0, 2));
        g.addEdge(1, new Edge(1, 3, 1));
        g.addEdge(1, new Edge(1, 4, 4));

        g.addEdge(2, new Edge(2, 0, 8));
        g.addEdge(2, new Edge(2, 3, 5));
        g.addEdge(2, new Edge(2, 5, 2));

        g.addEdge(3, new Edge(3, 0, 4));
        g.addEdge(3, new Edge(3, 1, 1));
        g.addEdge(3, new Edge(3, 2, 5));
        g.addEdge(3, new Edge(3, 4, 4));
        g.addEdge(3, new Edge(3, 5, 3));
        g.addEdge(3, new Edge(3, 6, 4));

        g.addEdge(4, new Edge(4, 1, 4));
        g.addEdge(4, new Edge(4, 3, 4));
        g.addEdge(4, new Edge(4, 6, 3));

        g.addEdge(5, new Edge(5, 2, 2));  // corrected weight here: now 2
        g.addEdge(5, new Edge(5, 3, 3));
        g.addEdge(5, new Edge(5, 6, 1));

        g.addEdge(6, new Edge(6, 3, 4));
        g.addEdge(6, new Edge(6, 4, 3));
        g.addEdge(6, new Edge(6, 5, 1));
    }

    /** Helper method to represent an undirected edge as a String
     *
     * @param u source vertex
     * @param v destination vertex
     * @return
     */
    private String undirectedEdgeKey(int u, int v) {
        int min = Math.min(u, v);
        int max = Math.max(u, v);
        return min + "-" + max;
    }

    /** Returns the expected set of MST edges represented as strings.
     *
     * @return set of MST edges
     */
    private Set<String> getExpectedEdges() {
        Set<String> expectedEdges = new HashSet<>();
        expectedEdges.add(undirectedEdgeKey(0, 1));
        expectedEdges.add(undirectedEdgeKey(1, 3));
        expectedEdges.add(undirectedEdgeKey(3, 5));
        expectedEdges.add(undirectedEdgeKey(5, 6));
        expectedEdges.add(undirectedEdgeKey(6, 4));
        expectedEdges.add(undirectedEdgeKey(5, 2));
        return expectedEdges;
    }

    /** Converts a list of MST edges into an undirected set representation.
     *
     * @param mstEdges list of MST edges
     * @return set of edges that will be used for comparison
     */
    private Set<String> convertEdgesToSet(List<Edge> mstEdges) {
        Set<String> actualEdges = new HashSet<>();
        for (Edge e : mstEdges) {
            actualEdges.add(undirectedEdgeKey(e.getSource(), e.getNeighbor()));
        }
        return actualEdges;
    }

    @Test
    public void testPrimMSTEdges() {
        // Compute MST using Prim's algorithm starting from vertex 0.
        PrimAlgorithm primAlgorithm = new PrimAlgorithm(g, 0);
        primAlgorithm.computeMST();
        List<Edge> mstEdges = primAlgorithm.getEdgesMST();

        Set<String> actualEdges = convertEdgesToSet(mstEdges);
        assertEquals(getExpectedEdges(), actualEdges, "Prim MST edges do not match the expected edges.");
    }

    @Test
    public void testKruskalMSTEdges() {
        // Compute MST using Kruskal's algorithm.
        KruskalAlgorithm kruskalAlgorithm = new KruskalAlgorithm(g);
        kruskalAlgorithm.computeMST();
        List<Edge> mstEdges = kruskalAlgorithm.getEdgesMST();

        Set<String> actualEdges = convertEdgesToSet(mstEdges);
        assertEquals(getExpectedEdges(), actualEdges, "Kruskal MST edges do not match the expected edges.");
    }
}