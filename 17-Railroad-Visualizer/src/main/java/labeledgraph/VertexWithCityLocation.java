/** A class that represents a node of the graph.
 *  Contains the name of the city and the location  (x, y coordinates) on the map.
 *  Do not modify this class.
 */
package labeledgraph;

import java.awt.*;

/** A vertex with the city label and the location.
 *  Do not change this class.  */
public class VertexWithCityLocation {
	private final String city;
	private Point location;

	/** Create a vertex of the graph with a "label" of the given city (and x and y coordinates)
	 * @param cityName name of the city
	 * @param x x coordinate on the image of the map
	 * @param y y coordinate on the image of the map
	 */
	public VertexWithCityLocation(String cityName, double x, double y) {
		// Do not change this method
		this.city = cityName;
		this.location = new Point((int) (507 * x / 7.0), (int) (289 - 289 * y/4.0));
	}

	/**
	 * Get the location on the image
	 * @return location of the vertex on the image of the USA map
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * Get the name of the city
	 * @return name of the city
	 */
	public String getCity() {
		return city;
	}
}