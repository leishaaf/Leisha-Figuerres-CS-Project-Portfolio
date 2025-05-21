package labeledgraph;

import graph.Edge;
import graph.Graph;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/** A child class of class Graph that is used for GUI. Look at class Graph before writing code in this class.
 * Stores city vertices in the array, and stores the map that maps each city name to an index of the vertex.
 * The adjacency list is stored in the parent class Graph.
 * Methods used by GUI that have been provided to you.
 * Fill in code in the constructor.
 */
public class GraphWithCityLabels extends Graph {
    private VertexWithCityLocation[] cityVertices; // vertices of the graph
    private Map<String, Integer> citiesToIndices = new HashMap<>();

    /**
     * Constructor. Read graph info from the given file,
     * and create vertices and edges of the graph.
     *
     *  @param filename name of the file that has vertices and edges
     */
    public GraphWithCityLabels(String filename) {
        // FILL IN CODE (see USA.txt under resources to understand the format)
        // Read labeled vertices and edges from the file,
        // update cityVertices, citiesToIndices map,
        // update the adjacency list (stored in the parent class Graph) by calling addEdge method from class Graph
        String line;
        int lineCount = 0; // keeps track of the lines so we can properly skip ones that aren't meant for parsing and separate edges and vertices clauses
        int vertexID = 0; // index of cityVertices that correlate to a city's vertex ID
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            while((line = br.readLine()) != null){
                lineCount++;
                if(lineCount == 2){ // initialize cityVertices with the number of vertices in USA.txt, should parse (20)
                    cityVertices = new VertexWithCityLocation[Integer.parseInt(line)];
                }
                if(lineCount < 3){ // skips over the first two lines in file
                    continue;
                }
                if(line.contains("EDGES")){ // skips over EDGES line
                    continue;
                }
                String[] arr = line.split(" "); // split the line into parts for parsing edges and vertices
                if(lineCount < 24){ // VERTICES CLAUSE - the first 23 lines
                    String cityName = arr[0]; // get the variables needed to make VertexWithCityLocation object and add that to city vertices
                    double xCor = Double.parseDouble(arr[1]);
                    double yCor = Double.parseDouble(arr[2]);
                    VertexWithCityLocation v = new VertexWithCityLocation(cityName, xCor, yCor);
                    cityVertices[vertexID] = v;
                    citiesToIndices.put(cityName, vertexID); // populate hashmap with vertexID mapping to a city
                    incrementVertices(); // populate the number of vertices (numVertices) in Graph
                    vertexID++; // keep track of the index of cityVertices (vertex IDs)
                }else{ // EDGES CLAUSE - 24th line and up until the end
                    int source = citiesToIndices.get(arr[0]); // get the source, neighbors, and cost
                    int neighbor = citiesToIndices.get(arr[1]);
                    int cost = Integer.parseInt(arr[2]);
                    // add edge both directions
                    Edge edge = new Edge(source, neighbor, cost);
                    Edge edgeReverse = new Edge(neighbor, source, cost);
                    addEdge(source, edge); // add edge to Graph
                    addEdge(neighbor, edgeReverse); // add the edge going the opposite direction as well
                }
            }
        }catch (IOException e){
            System.err.println("Exception found! " + e);
        }
    }


    /**
     * Return the edges of the graph as a 2D array of points.
     * Called from GUIApp to display the edges of the graph.
     *
     * @return a 2D array of Points.
     * For each edge, we store an array of two Points, v1 and v2.
     * v1 is the source vertex for this edge, v2 is the destination vertex.
     * This info can be obtained from the adjacency list
     */
    public Point[][] getEdges() {
        Point[][] edges2D = new Point[getNumEdges()][2];
        int idx = 0;
        for (int i = 0; i < getNumVertices(); i++) {
            for (Edge tmp = getFirstEdge(i); tmp != null; tmp = tmp.next(), idx++) {
                edges2D[idx][0] = cityVertices[tmp.getSource()].getLocation();
                edges2D[idx][1] = cityVertices[tmp.getNeighbor()].getLocation();
            }
        }
        return edges2D;
    }

    /**
     * Get the vertices of the graph as a 1D array of Points.
     * Used in GUIApp to display the nodes of the graph.
     * @return a list of Points that correspond to nodes of the graph.
     */
    public Point[] getVerticesAsPoints() {
        if (cityVertices == null) {
            System.out.println("Array of nodes is empty. Load the graph first.");
            return null;
        }
        Point[] nodes = new Point[this.cityVertices.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = this.cityVertices[i].getLocation();
        }
        return nodes;
    }

    /**
     * Used in GUIApp to display the names of the cities.
     * @return the list that contains the names of cities (that correspond
     * to the vertices of the graph)
     */
    public String[] getCities() {
        if (cityVertices == null) {
            return null;
        }
        String[] labels = new String[cityVertices.length];
        for (int i = 0; i < cityVertices.length; i++) {
            labels[i] = cityVertices[i].getCity();
        }
        return labels;
    }

    /**
     * Return VertexWithCityLocation for the given vertexId
     * @param vertexId id of the vertex
     * @return VertexWithCityLocation that contains the name of the city, location on the image etc.
     */
    public VertexWithCityLocation getVertex(int vertexId) {
        return cityVertices[vertexId];
    }

}
