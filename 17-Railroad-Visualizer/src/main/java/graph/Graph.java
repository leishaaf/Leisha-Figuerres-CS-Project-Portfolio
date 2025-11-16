package graph;

/**
 * A class that represents a basic graph: stores edges in the adjacency list.
 * Understand this class well before implementing GraphWithCityLabels.
 *
 */
public class Graph {
    private int numVertices;
    private int numEdges; // total number of edges
    private Edge[] adjacencyList; // adjacency list; for each vertex stores a linked list of edges

    public Graph() {
        this.numVertices = 0;
        this.numEdges = 0;
    }

    /** Constructor
     * @param numVertices number of vertices
     */
    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.numEdges = 0;
        adjacencyList = new Edge[numVertices];
    }


    /**
     * Get the number of vertices in the graph
     * @return number of vertices
     */
    public int getNumVertices() {
        return numVertices;
    }

    /** Increase the number of vertices by 1.
     *
     */
    public void incrementVertices() {
        numVertices++;
    }

    /**
     * Get the number of edges
     * @return number of edges
     */
    public int getNumEdges() {
        return numEdges;
    }

    /**
     * Add an outgoing edge for a given vertexId to the adjacency list.
     * Add in front of the linked list of outgoing edges.
     * @param vertexId vertex
     * @param edge outgoing edge
     */
    public void addEdge(int vertexId, Edge edge) {
        if (adjacencyList == null) { // first check if the adjacency list is empty
            adjacencyList = new Edge[numVertices];
            numEdges = 0;
        }
        // FILL IN CODE:
        // Add a new edge to the front of the linked list of the given vertex
        Edge head = adjacencyList[vertexId]; // get head of adj list
        if(head != null){
            edge.setNext(head);
        }
        adjacencyList[vertexId] = edge;
        numEdges++;
    }

    /** Return the head of the linked list that contains all edges outgoing
     * from vertexId
     * @param vertexId id of the node
     * @return head of the linked list of Edges
     */
    public Edge getFirstEdge(int vertexId) {
        if (adjacencyList == null || adjacencyList.length == 0) {
            System.out.println("Adjacency list is empty. Load the graph first.");
            return null;
        }
        return adjacencyList[vertexId];
    }
}
