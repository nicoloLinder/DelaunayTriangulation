import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Point extends Vector3D implements Serializable {
	private static int cnt = 0;

	private int distance;
	private Point parent;

	// METHODS
	public static Point random(double max, double min) {
		return null;
	}

	public static void reset() {
		cnt = 0;
	}

	private String name;

	public Point(double x, double y, boolean b) {
		super(x, y);
		name = "";
	}

	public Point(Double x, Double y) {
		super(x, y);
		name = "p" + cnt++;

		triangulation().add(this);
	}

	// CONSTRUCTOR

	public Point(Point r) {
		super(r.getX(), r.getY());
		name = "p" + cnt++;

		triangulation().add(this);
	}

	public String getName() {
		return name;
	}

	public List<Triangle> getTriangles() {
		List<Triangle> triangles = new ArrayList<Triangle>();
		for (Triangle tr : Triangulation.getInstance().getTriangles()) {
			if (tr.isVertex(this)) {
				triangles.add(tr);
			}
		}
		return order(triangles);
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Point getParent() {
		return parent;
	}

	public void setParent(Point parent) {
		this.parent = parent;
	}

	private List<Triangle> order(List<Triangle> triangles) {
		List<Triangle> temp = new ArrayList<Triangle>();

		Point vs = triangles.get(0).adjacientEdge(this).getP1();
		temp.add(triangles.get(0));
		triangles.remove(0);

		while (triangles.size() > 0) {
			for (Triangle t : triangles) {
				if (t.isVertex(vs)) {
					Edge e = t.adjacientEdge(this);
					if (e.getP0().equals(vs)) {
						// System.out.println("hole");
						vs = e.getP0();
					} else {
						vs = e.getP1();
					}
				}
				temp.add(t);
				triangles.remove(t);
				break;
			}
		}

		return temp;
	}
	
	public List<Edge> getEdges(){
		List<Edge> temp = new ArrayList<Edge>();
		for(Edge e : triangulation().getEdges()){
			if(e.contains(this)){
				temp.add(e);
			}
		}
		return temp;
	}

	protected void remove() {
		triangulation().remove(this);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "x:" + x + " y:" + y;
	}

	private Triangulation triangulation() {
		return Triangulation.getInstance();
	}
}