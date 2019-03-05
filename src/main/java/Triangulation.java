import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Triangulation implements Serializable {
	private static Triangulation instance = new Triangulation();

	// VARS

	private final List<Point> points = new ArrayList<Point>();
	private final List<Edge> edges = new ArrayList<Edge>();
	private final List<Triangle> triangles = new ArrayList<Triangle>();

	// GETTERS

	public List<Point> getPoints() {
		return points;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Triangle> getTriangles() {
		return triangles;
	}

	// CONSTRUCTOR
	private Triangulation() {
		reset();
	}

	public void reset() {
		points.clear();
		edges.clear();
		triangles.clear();

		Triangle.reset();
		Point.reset();
		Edge.reset();
	}

	// METHODS
	public void add(Point p) {
		points.add(p);
	}

	public void remove(Point p) {
		points.remove(p);
	}

	public void add(Edge e) {
		edges.add(e);
	}

	public void remove(Edge e) {
		edges.remove(e);
	}

	public void add(Triangle t) {
		triangles.add(t);
	}

	public void remove(Triangle t) {
		triangles.remove(t);
	}

	public static Triangulation getInstance() {
		return instance;
	}

	public void save() {
		XStream xstream = new XStream();
		String xml = xstream.toXML(instance);
		System.out.println(xml);

		try {
			FileUtils.writeStringToFile(new File("delaunay.xml"), xml, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		XStream xstream = new XStream();

		instance = (Triangulation) xstream.fromXML(new File("delaunay.xml"));
	}

	public void remove(List<Triangle> triangles) {
		for(Triangle t : triangles){
			this.triangles.remove(t);
		}
		
	}
}