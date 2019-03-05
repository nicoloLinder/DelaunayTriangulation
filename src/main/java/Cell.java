import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cell extends Base{
	
	private final Set<Edge> edges = new HashSet<Edge>();
	private final List<Point> points;

//	GETTERS

	public Edge getEdge(int index) {
		int i = 0;
		for(Edge e : edges){
			if(i++ == index){
				return e;
			}
		}
		return null;
	}

	
	public Collection<Edge> getEdges(){
		return edges;
	}

	public Point getVertex(int index) {
		return points.get(index);
	}
	
//	SETTERS
	
	public void setEdge(int index, Edge edge){
		int i = 0;
		for(Edge e : edges){
			if(i++ == index){
				e = edge;
			}
		}
	}
	
//	CONSTRUCTOR
	
	public Cell(List<Point> centroids){
		super();
	
		points = new ArrayList<Point>(centroids);
		Point prev = null;
		for(Point p : centroids){
			if(prev != null){
				edges.add(new Edge(p,prev,false));
			}
			prev = p;
		}
		
		edges.add(new Edge(centroids.get(0),prev,false));
		
//		this.e01 = new Edge(v0, v1, this);
//		this.e12 = new Edge(v1, v2, this);
//		this.e20 = new Edge(v2, v0, this);
//		
//		edges.add(e01);
//		edges.add(e12);
//		edges.add(e20);
//		
//		this.v0 = e01.getP0();
//		this.v1 = e01.getP1();
//		this.v2 = e12.getP1();
//		
//		points.add(v0);
//		points.add(v1);
//		points.add(v2);
		
		voronoi().add(this);
	}
	
//	METHODS



	public void remove(){
		
	}
	
}
