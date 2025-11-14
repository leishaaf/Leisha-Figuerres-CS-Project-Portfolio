package graph;

/** Edge class represents an edge in the adjacency list of the graph. 
 * Implements Comparable. Compares Edges based on the cost.
 * Fill in code in compareTo. */
public class Edge implements Comparable<Edge> {
    private int source; //source vertex
    private int neighbor; //destination vertex
    private int cost; // cost (weight) of the vertex
    private Edge next;

    /**
     * Constructor of class Edge
     * @param source id of the first vertex
     * @param neighbor id of the second vertex
     * @param cost cost (weight) of the edge
     */
    public Edge(int source, int neighbor, int cost){
        this.source = source;
        this.neighbor = neighbor;
        this.cost = cost;
        next = null;
    }

    /**
     * Getter for next
     * @return the next edge in the linked list
     */
    public Edge next(){
        return this.next;
    }

    /**
     * Getter for source
     * @return source
     */
    public int getSource(){
        return this.source;
    }

    /**
     * Getter for neighbor
     * @return neighbor
     */
    public int getNeighbor(){
        return this.neighbor;
    }

    /**
     * Getter for the cost of the edge
     * @return cost of the edge
     */
    public int getCost(){
        return this.cost;
    }

    /**
     * Setter for next
     * @param newnext nextEdge in the linked list
     */
    public void setNext(Edge newnext){
        this.next = newnext;
    }


    /**
     * Compares this edge to a given edge based on the cost
     * @param o another edge
     * @return 0 if edges are equal; -1 if this edge is "less", and 1 otherwise
     */
    @Override
    public int compareTo(Edge o) {
        // FILL IN CODE: compare edges by cost
        if(this.cost < o.getCost()){ // if this cost is cheaper
            return -1;
        }else if(this.cost > o.getCost()){
            return 1; // if other cost is cheaper
        }else{
            return 0; // if equal
        }
    }
}