package graph;

import labeledgraph.GraphWithCityLabels;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Parent class of PrimAlgorithm and KruskalAlgorithm.
 * Fill in the code in printMST() method. */
public abstract class MSTAlgorithm {
    private Graph graph; // stores the reference to the graph
    private List<Edge> edgesMST = new ArrayList<>(); // edges that belong to
    // minimal spanning tree

    public MSTAlgorithm(Graph graph) {
        this.graph = graph;
    }

    /** Add an edge to the list of edges of the Minimal Spanning Tree
     *
     * @param edge edge that is a part of MST
     */
    public void addMSTEdge(Edge edge) {
        edgesMST.add(edge);
    }

    /**
     * Compute minimum spanning tree for this graph. Add edges of MST to
     * edgesMST list. Will be implemented differently in Prim's and Kruskal's
     */
    public abstract void computeMST();

    /** Print the edges of the MST tree.
     * On each line it should print one edge, using names of two cities.
     * */
    public void printMST() {
        // FILL IN CODE: print source and neighbor for each edge
        for(int i = 0; i < edgesMST.size(); i++){ // iterate through the edges MST and prints them by source and neighbor
            System.out.println("Source " + edgesMST.get(i).getSource() + " Neighbor: " + edgesMST.get(i).getNeighbor());
        }
    }

    /**
     * Return the number of vertices in the underlying graph
     * @return number of vertices
     */
    public int numVertices() {
        return graph.getNumVertices();
    }

    /** Return the head of the linked list that contains all edges outgoing
     * from a given vertex id
     * @param vertexId id of the vertex
     * @return head of the linked list of Edges
     */
    public Edge getFirstEdge(int vertexId) {
        return graph.getFirstEdge(vertexId);
    }

    /**
     * Return edges of the Minimal Spanning Tree
     * @return MST edges
     */
    public List<Edge> getEdgesMST() {
        return edgesMST;
    }

    // -------------------- Method needed for the GUIApp-------------------
    /**
     * Used in GUIApp to display the MST. Returns a 2D Array, where each element
     * represents an edge and is an array of two Points (where this edge starts
     * and where it is going).
     */
    public Point[][] getMSTEdges() {
        Point[][] edges = new Point[edgesMST.size()][2];
        Point[] locations = ((GraphWithCityLabels)graph).getVerticesAsPoints();
        int i = 0;
        for (Edge edge : edgesMST) {
            edges[i][0] = locations[edge.getSource()];
            edges[i][1] = locations[edge.getNeighbor()];
            i++;
        }
        return edges;
    }

}

