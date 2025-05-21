package labeledgraph;

import graph.Edge;
import graph.Graph;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
// imports added by Leisha
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



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
        int lineCount = 0; // to skip first 2 lines
        int vertexID = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            while((line = br.readLine()) != null){
                lineCount++;
                while(lineCount < 2){ continue; }
                String[] arr = line.split(" ");
                String cityName = arr[0];
                double xCor = Double.parseDouble(arr[1]);
                double yCor = Double.parseDouble(arr[2]);
                VertexWithCityLocation v = new VertexWithCityLocation(cityName, xCor, yCor);
                cityVertices[vertexID] = v;
                if(line.contains("EDGES")){

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
