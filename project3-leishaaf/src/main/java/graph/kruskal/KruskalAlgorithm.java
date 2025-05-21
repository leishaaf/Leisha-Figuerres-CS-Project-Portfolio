package graph.kruskal;

import graph.Edge;
import graph.Graph;
import graph.MSTAlgorithm;
// added imports by Leisha
import java.util.ArrayList;
import java.util.Collections;

/** Subclass of MSTAlgorithm. Computes MST of the graph using Kruskal's algorithm. */
public class KruskalAlgorithm extends MSTAlgorithm {

    /**
     * Constructor for KruskalAlgorithm. Takes the graph
     * @param graph input graph
     */
    public KruskalAlgorithm(Graph graph) {
        super(graph);
    }

    /**
     * Compute minimum spanning tree for this graph.
     * Add edges of MST using the addMSTEdge method inherited from the parent
     * class MSTAlgorithm.
     * Should use Kruskal's algorithm and DisjointSets class.
     */
    @Override
    public void computeMST() {
        System.out.println("helloffff");
        // FILL IN CODE
        // Run Kruskal's algorithm using disjoint sets.
        DisjointSets sets = new DisjointSets(); // create n sets for each vertex
        sets.createSets(numVertices());
        // add edges to arraylist and sort
        ArrayList<Edge> sortedEdges = new ArrayList<>();
        for(int i = 0; i < numVertices(); i++){
            Edge curr = getFirstEdge(i);
            while(curr != null){
                sortedEdges.add(curr);
                curr = curr.next();
            }
//            sortedEdges.add(curr);
        }
        Collections.sort(sortedEdges);
        // Add all edges of the graph to the ArrayList, sort edges
        // create disjoint sets
        // for each edge, decide whether to add the edge to MST (use disjoint sets)
        // If you are adding an edge to MST, call addMSTEdge from the parent class (and change disjoint sets accordingly)
//        for(int i = 0; i < sortedEdges.size() - 1; i++){
//            System.out.println("Source " + sortedEdges.get(i).getSource() + " neighbor " + sortedEdges.get(i).getNeighbor());
//        }
        for(int i = 0; i < sortedEdges.size(); i++){
            System.out.println("hello in sorting");
                int firstRep = sets.find(sortedEdges.get(i).getSource());
                System.out.println("first root " + firstRep);
                int secondRep = sets.find(sortedEdges.get(i).getNeighbor());
                System.out.println("second root " + secondRep);
                if(firstRep != secondRep){
                    System.out.println("ADDING");
                    addMSTEdge(sortedEdges.get(i));
                    sets.union(sortedEdges.get(i).getSource(), sortedEdges.get(i).getNeighbor());
                }
        }

    }
}

