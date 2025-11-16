package labeledgraph;

import graph.Graph;

/** The Driver class for the MST project.
 *  Should take the names of two input files (USA.txt and USA.bmp as command line arguments)
 *  */
public class GUIDriver {
	public static void main(String[] args) {
		// Expects command line arguments (args):
		// the first command line argument, args[0], should be src/main/resources/USA.txt
		// the second command line argument, args[1], should be src/main/resources/USA.bmp
		if (args.length == 0) {
			System.out.println("No arguments");
			return;
		}
		Graph graph = new GraphWithCityLabels(args[0]); //load graph from the file given in args[0]
		GUIApp app = new GUIApp(graph, args[1]);
		// This will run the GUI, and then the user will be able to interact with GUI


	}
}