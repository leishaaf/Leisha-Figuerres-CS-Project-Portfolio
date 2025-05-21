package graph;

import graph.kruskal.KruskalAlgorithm;
import graph.prim.PrimAlgorithm;

// Use this class to test Kruskal's and Prim's algorithms on a basic graph (without GUI)
// Use the graph created below or create a different graph,
// then run Prim's or Kruskal's on it and check the result
public class BasicDriver {
    public static void main(String[] args) {
        Graph g = new Graph(7);
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

        g.addEdge(5, new Edge(5, 2, 2));
        g.addEdge(5, new Edge(5, 3, 3));
        g.addEdge(5, new Edge(5, 6, 1));

        g.addEdge(6, new Edge(6, 3, 4));
        g.addEdge(6, new Edge(6, 4, 3));
        g.addEdge(6, new Edge(6, 5, 1));

//        PrimAlgorithm primAlgorithm = new PrimAlgorithm(g, 0);
//        primAlgorithm.computeMST();
//        // You can use printMST or get the list of MST edges, and then print them separately:  List<Edge> mstEdges = primAlgorithm.getEdgesMST();
//        primAlgorithm.printMST();


        System.out.println();
        KruskalAlgorithm kruskalAlgorithm = new KruskalAlgorithm(g);
        kruskalAlgorithm.computeMST();
        System.out.println(kruskalAlgorithm.getEdgesMST());
        kruskalAlgorithm.printMST();


        /* Should get edges: Prim's
        0, 1
        1, 3
        3, 5
        5, 6
        6, 4
        5, 2

        The order of edges does not matter
        The graph is undirected, so the order of source and neighbor of the edge does not matter,
        Ex: (0, 1) is the same as (1, 0).
         */
    }
}
