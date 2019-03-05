import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;


public class Voronoi implements Serializable{
	private static Voronoi instance = new Voronoi();

//	VARS

	private final List<Point> centroids = new ArrayList<Point>();
	private final List<Edge> edges = new ArrayList<Edge>();
	private final List<Cell> cells = new ArrayList<Cell>();

//	GETTERS

	public List<Point> getCentroids(){
		return centroids;
	}

	public List<Edge> getEdges(){
		return edges;
	}

	public List<Cell> getCells(){
		return cells;
	}


//	CONSTRUCTOR

	private Voronoi() {
		reset();
	}
	
	public void reset() {
		centroids.clear();
		edges.clear();
		cells.clear();
	}

// METHODS

	public void add(Point p){
		centroids.add(p);
	}

	public void remove(Point p){
		centroids.remove(p);
	}

	public void add(Edge e){
		edges.add(e);
	}

	public void remove(Edge e){
		edges.remove(e);
	}

	public void add(Cell c){
		cells.add(c);
	}

	public void remove(Cell c){
		cells.remove(c);
	}

	public static Voronoi getInstance() {
		return instance;
	}
	
	public void save() {
		XStream xstream = new XStream();
		String xml = xstream.toXML(instance);
		System.out.println(xml);
		
		try {
			FileUtils.writeStringToFile(new File("voronoi.xml"), xml, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
			XStream xstream = new XStream();
			
			instance = (Voronoi) xstream.fromXML(new File("voronoi.xml"));
	}

}