package graph.prim;

import graph.Edge;
import graph.Graph;
import graph.MSTAlgorithm;

/** Subclass of MSTAlgorithm. Uses Prim's algorithm to compute MST of the graph. */
public class PrimAlgorithm extends MSTAlgorithm {
    private int sourceVertex;
    // Declare the table used in Prim's algorithm
    boolean[] added = new boolean[numVertices()]; // made 3 arrays representing a table for prims algorithm
    int[] cost = new int [numVertices()];
    int[] path = new int[numVertices()];

    /**
     * Constructor for PrimAlgorithm. Takes the graph
     * @param graph input graph
     * @param sourceVertex the first vertex of MST
     */
    public PrimAlgorithm(Graph graph, int sourceVertex) {
        super(graph);
        this.sourceVertex = sourceVertex;
    }

    /**
     * Compute minimum spanning tree for this graph using Prim's algorithm.
     * Add edges of MST using the addMSTEdge method inherited from the parent
     * class MSTAlgorithm.
     * */
    @Override
    public void computeMST() {
        // FILL IN CODE
        // Initialize the table
        // Run Prim's algorithm using the table
        // Need to call addMSTEdge from the parent class to add an edge to MST
        for(int i = 0; i < numVertices(); i++){ // initializing table
            this.added[i] = false;
            this.cost[i] = Integer.MAX_VALUE;
            this.path[i] = -1;
        }
        System.out.println("reached computeMST()");
        cost[sourceVertex] = 0; // the first vertex added to table
        added[sourceVertex] = true;
        int addedCount = 0; // keeps track of when vertices are added
        while(addedCount < numVertices() - 1){ // keep going until all vertices are added. make sure we don't exceed more than the number of vertices in graph. -1 to numVertices() because one was already added (source vertex)
            Edge cheapestEdge = getCheapestAddedVertex(added, cost, path); // call to helper method to get the cheapest edge
            if(cheapestEdge == null) { break; } // means that all vertices have been added
            addMSTEdge(cheapestEdge);
            addedCount++;
        }
        for(int i = 0; i < numVertices(); i++){
            System.out.println("cost " + cost[i] + " added " + added[i] + " path " + path[i]);
        }
    }

    // helper method to get the cheapest added vertex (outgoing edges)
    private Edge getCheapestAddedVertex(boolean[] added, int[] cost, int[] path){
        int minCost = Integer.MAX_VALUE;
        Edge cheapestEdge = null;
        for(int i = 0; i < numVertices(); i++){
            if(added[i]){
                Edge edge = getFirstEdge(i);
                while(edge != null){
                    // find the cheapest cost of an edge that has already been added and connects to one that isn't added
                    if(edge.getCost() < minCost && !added[edge.getNeighbor()]) {
                        minCost = edge.getCost(); // update the edge with the cheapest cost found so far
                        cheapestEdge = edge;
                    }
                    edge = edge.next();
                }
            }
        }
        if(cheapestEdge == null) { return null; } // vertices have all been visited and added so return null
        added[cheapestEdge.getNeighbor()] = true; // update the table after finding the edge with the cheapest path to a vertex not added yet
        path[cheapestEdge.getNeighbor()] = cheapestEdge.getSource(); // the index of the path is where we ended up and the element is where we came from i.e path
        cost[cheapestEdge.getNeighbor()] = cheapestEdge.getCost(); // cost of the path from source to neighbor
        return cheapestEdge;
    }



    // Feel free to add a helper method that searches for the "cheapest" vertex to attach to MST

}
