/** GUIApp - a class that deals with the graphical user interface of the project.
 *  Creates the window with the panel that displays the map of the US,
 *  with major cities as nodes and edges connecting them.
 *  Allows the user to click on either Kruskal's or Prim's button,
 *  and runs the corresponding algorithm for computing MST.
 *  Displays the MST edges.
 *  Do not change this class.
 */
package labeledgraph;

import graph.Graph;
import graph.MSTAlgorithm;
import graph.kruskal.KruskalAlgorithm;
import graph.prim.PrimAlgorithm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUIApp extends JFrame {
    private MapPanel panel;

    /**
     * Constructor of GUIApp
     * @param graph Reference to the graph
     * @param fileImage file with the image of the map
     */
    public GUIApp(Graph graph, String  fileImage) {
        // Creating a window
        JFrame frame = new JFrame("USA Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        // Creating a panel that will contains the "map" and buttons
        panel = new MapPanel(graph, fileImage);
        // Adding the panel to the window
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /** The panel for the GUI */
    private class MapPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        public final static int RAD = 3;

        private MSTAlgorithm algo = null; // algorithm for computing minimal spanning tree
        private Graph graph; // Graph

        private JButton buttonReset; // button to reset the algorithm
        private JButton buttonPrim; // button to run Prim's
        private JButton buttonKruskal; // button to run Kruskal's
        private JButton buttonQuit; // button to quit
        private BufferedImage image; // for showing the image of the US map

        private Color colMSTEdges; // color to use while displaying MST edges

        /**
         * Constructor for MapPanel class
         * @param graph Reference to the graph
         * @param fileImage file with the image of the map
         */
        public MapPanel(Graph graph, String fileImage) {
            this.algo = algo;
            this.graph = graph;
            this.setLayout(new BorderLayout());
            this.setPreferredSize(new Dimension(580, 290));
            this.setBackground(Color.lightGray);

            // button
            buttonQuit = new JButton("Quit");
            buttonReset = new JButton("Reset");
            buttonPrim  = new JButton("Prim's");
            buttonKruskal  = new JButton("Kruskal's");
            buttonReset.addActionListener(new ButtonListener());
            buttonPrim.addActionListener(new ButtonListener());
            buttonKruskal.addActionListener(new ButtonListener());
            buttonQuit.addActionListener(new ButtonListener());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.add(buttonReset);
            buttonPanel.add(buttonPrim);
            buttonPanel.add(buttonKruskal);
            buttonPanel.add(buttonQuit);
            this.add(buttonPanel, BorderLayout.EAST);

            try { // load the image of the map of the USA
                image = ImageIO.read(new File(fileImage));
            } catch (IOException ex) {
                System.out.println("Could not load the image. " + ex);
            }
            repaint(); // draw everything
        }

        /**
         * The method is responsible for drawing everything on the panel.
         * Do NOT call it explicitly. Instead, call repaint() when
         * something changes and needs to be repainted.
         * @param g Graphics
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
            drawNodes(g);
            drawEdges(g, Color.lightGray);
            drawMSTEdges(g);
        }

        /**
         * Draws a little circle at the given location of the node; Uses the
         * given color; "city" parameter is used to draw a label next to the
         * circle.
         * @param g Graphics
         * @param location point that contains x and y coordinates of the city on the image
         * @param col color
         * @param city name of the city
         */
        public void drawNode(Graphics g, Point location, Color col, String city) {
            g.setColor(col);
            g.fillOval(location.x - RAD, location.y - RAD, 2 * RAD, 2 * RAD);
            g.setColor(Color.black);
            g.setFont(new Font("SANS_SERIF", Font.PLAIN, 11));
            g.drawString(city, location.x + 2, location.y - 2);
        }

        /**
         * Display the nodes of the graph as little circles on the map
         * @param g Graphics
         */
        public void drawNodes(Graphics g) {
            Point[] nodes = ((GraphWithCityLabels)graph).getVerticesAsPoints();
            String[] labels = ((GraphWithCityLabels)graph).getCities();
            if (nodes == null) {
                //System.out.println("Nothing to display, nodes are null");
                return;
            }
            for (int i = 0; i < nodes.length; i++) {
                drawNode(g, nodes[i], Color.BLACK, labels[i]);
            }

        }

        /**
         * Draw edges of the graph
         * edges is an array, where each value is an array of two Points
         * @param g Graphics
         * @param color Color in which to draw the edges
         */
        public void drawEdges(Graphics g, Color color) {
            Point[][] edges = ((GraphWithCityLabels)graph).getEdges();
            if (edges == null) {
                return;
            }
            g.setColor(color);

            for (int i = 0; i < edges.length; i++) {
                Point[] edge = edges[i];
                assert(edge.length == 2); // should contain two vertices
                Point p1 = edge[0];
                Point p2 = edge[1];
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        } // drawEdges

        /**
         * Draw the edges of the MST in red.
         * @param g Graphics
         */
        public void drawMSTEdges(Graphics g) {
            if (algo == null)
                return;
            Point[][] pathEdges = algo.getMSTEdges();
            g.setColor(colMSTEdges);
            if (pathEdges == null)
                return;
            for (int i = 0; i < pathEdges.length; i++) {
                Point[] edge = pathEdges[i];
                assert(edge.length == 2); // should contain two vertices
                Point p1 = edge[0];
                Point p2 = edge[1];
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        } // drawShortestPath

        /** Inner class that handles button presses */
        class ButtonListener implements ActionListener {
            /**
             * Will be called automatically when the user clicks on a button.
             * @param e ActionEvent
             */
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == buttonQuit) {
                    System.exit(0);
                }
                else if (e.getSource() == buttonReset) {
                    algo = null;
                    repaint();
                }
                else if (e.getSource() == buttonKruskal) {
                    System.out.println("Button Kruskal clicked");
                    algo = new KruskalAlgorithm(graph);
                    algo.computeMST();
                    colMSTEdges = Color.RED;
                    repaint();
                }
                else if (e.getSource() == buttonPrim) {
                    System.out.println("Button Prim clicked");
                    algo = new PrimAlgorithm(graph, 0);
                    algo.computeMST();
                    colMSTEdges = Color.BLUE;
                    repaint();
                }
            }
        } // inner class ButtonListener

    } // MapPanel
} // GUIApp