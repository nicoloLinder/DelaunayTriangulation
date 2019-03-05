import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Triangle extends Base {

	private static int cnt = 0;

	public static void reset() {
		cnt = 0;
	}

	private Edge e01;
	private Edge e12;
	private Edge e20;

	private Point v0;
	private Point v1;
	private Point v2;

	public Vector3D getCenter() {
		return center;
	}

	private Point center;

	private final List<Point> points = new ArrayList<Point>();

	// GETTERS
	public boolean sign() {
		return v1.subtract(v0).cross(v2.subtract(v0)).z > 0;
	}

	public Edge getE0() {
		return e01;
	}

	public Edge getE1() {
		return e12;
	}

	public Edge getE2() {
		return e20;
	}

	public Collection<Edge> getEdges() {
		Set<Edge> edges = new HashSet<Edge>();
		edges.add(e01);
		edges.add(e12);
		edges.add(e20);
		return edges;
	}

	public Point getV0() {
		return v0;
	}

	public Point getV1() {
		return v1;
	}

	public Point getV2() {
		return v2;
	}

	// SETTERS

	public void setA(Edge a) {
		this.e01 = a;
	}

	public void setB(Edge b) {
		this.e12 = b;
	}

	public void setC(Edge c) {
		this.e20 = c;
	}

	// CONSTRUCTOR

	public Triangle(Point v0, Point v1, Point v2) {
		super();

		name = "t" + cnt++;

		System.out.println("Created " + name);

		this.e01 = new Edge(v0, v1, this);
		this.e12 = new Edge(v1, v2, this);
		this.e20 = new Edge(v2, v0, this);

		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;

		points.add(v0);
		points.add(v1);
		points.add(v2);

		center = Centroid();

		e01.setOtherEdge();
		e12.setOtherEdge();
		e20.setOtherEdge();

		triangulation().add(this);
	}

	// METHODS
	public boolean isVertex(Point p) {
		if (p == v0 || p == v1 || p == v2) {
			return true;
		}
		return false;
	}

	public boolean contains(Point p) {
		double alpha = ((v1.getY() - v2.getY()) * (p.getX() - v2.getX())
				+ (v2.getX() - v1.getX()) * (p.getY() - v2.getY()))
				/ ((v1.getY() - v2.getY()) * (v0.getX() - v2.getX())
						+ (v2.getX() - v1.getX()) * (v0.getY() - v2.getY()));
		double beta = ((v2.getY() - v0.getY()) * (p.getX() - v2.getX())
				+ (v0.getX() - v2.getX()) * (p.getY() - v2.getY()))
				/ ((v1.getY() - v2.getY()) * (v0.getX() - v2.getX())
						+ (v2.getX() - v1.getX()) * (v0.getY() - v2.getY()));
		double gamma = 1.0f - alpha - beta;
		return alpha > 0 && beta > 0 && gamma > 0;
	}

	protected void legalize(Point p, Triangle t0) {

		Edge aEdge = t0.adjacientEdge(p);

		System.out.println("Legalizing " + t0 + " " + aEdge);

		if (aEdge != null && aEdge.illegal(p)) {

			System.out.println("Legalization Needed");

			Triangle t1 = aEdge.getOtherEdge().getT0();

			t0.remove();
			t1.remove();

			t1.points.remove(aEdge.getP0());
			t1.points.remove(aEdge.getP1());
			

			Point o = t1.points.get(0);

			Triangle t2 = new Triangle(p, aEdge.getP0(), o);
			Triangle t3 = new Triangle(o, aEdge.getP1(), p);

			legalize(p, t2);
			legalize(p, t3);
			
		}

	}

	public List<Point> getPoints() {
		return points;
	}

	public void split(Point p) {
		remove();

		Triangle t0 = new Triangle(v0, v1, p);
		Triangle t1 = new Triangle(v1, v2, p);
		Triangle t2 = new Triangle(v2, v0, p);

		legalize(p, t0);
		legalize(p, t1);
		legalize(p, t2);
		
		t0.checkOtherEdge();
		t1.checkOtherEdge();
		t2.checkOtherEdge();
	}

	public void checkOtherEdge() {
		// TODO Auto-generated method stub
		e01.setOtherEdge();
		e12.setOtherEdge();
		e20.setOtherEdge();
	}

	public double CircumRadius() {
		Point vL1 = new Point(v0.getX() - v2.getX(), v0.getY() - v2.getY(), false);
		Point vL2 = new Point(v1.getX() - v2.getX(), v1.getY() - v2.getY(), false);
		Point vB = new Point(v0.getX() - v1.getX(), v0.getY() - v1.getY(), false);

		double a = Math.acos((vL1.getX() * vL2.getX() + vL1.getY() * vL2.getY()) / (vL1.magnitude() * vL2.magnitude()));
		return Math.abs(vB.magnitude() / (2 * Math.sin(a)));

	}

	public Point Centroid() {
		return new Point((v0.getX() + v1.getX() + v2.getX()) / 3, (v0.getY() + v1.getY() + v2.getY()) / 3, false);
	}

	public Point CircumCenter() {

		double d = 2 * (v0.getX() * (v1.getY() - v2.getY()) + v1.getX() * (v2.getY() - v0.getY())
				+ v2.getX() * (v0.getY() - v1.getY()));

		double x = ((Math.pow(v0.getX(), 2) + Math.pow(v0.getY(), 2)) * (v1.getY() - v2.getY())
				+ (Math.pow(v1.getX(), 2) + Math.pow(v1.getY(), 2)) * (v2.getY() - v0.getY())
				+ (Math.pow(v2.getX(), 2) + Math.pow(v2.getY(), 2)) * (v0.getY() - v1.getY())) / d;
		double y = ((Math.pow(v0.getX(), 2) + Math.pow(v0.getY(), 2)) * (v2.getX() - v1.getX())
				+ (Math.pow(v1.getX(), 2) + Math.pow(v1.getY(), 2)) * (v0.getX() - v2.getX())
				+ (Math.pow(v2.getX(), 2) + Math.pow(v2.getY(), 2)) * (v1.getX() - v0.getX())) / d;

		return new Point(x, y, false);
	}

	public Point OrthoCenter() {

		// OH = 2NH -> H-O = 2H - 2N -> H-2H = 2N + O -> -H = 2N - O -> H = -2N
		// + O;
		// NO = NH = 3NG -> NO = 3NG -> O-N = 3(G - N) -> O - N = 3G + 3N ->O -
		// 3 G = -2N
		// H = -2N + O | -2N = O - 3G -> H = O - 3G + O H = 2O+3G

		return new Point(-2 * CircumCenter().getX() + 3 * Centroid().getX(),
				-2 * CircumCenter().getY() + 3 * Centroid().getY(), false);
	}

	@Override
	protected void remove() {
		System.out.println("Remove " + name);
		triangulation().remove(this);
		voronoi().remove(center);

		e01.remove();
		e12.remove();
		e20.remove();
	}

	public String toString() {
		return name + "(" + v0.getName() + "," + v1.getName() + "," + v2.getName() + ")";
	}

	public Edge adjacientEdge(Point p) {
		for (Edge e : getEdges()) {
			if (!e.getP0().equals(p) && !e.getP1().equals(p)) {
				return e;
			}
		}
		return null;
	}

	public Polygon getPolygon() {
		Polygon p = new Polygon();

		p.addPoint(v0.getX().intValue(), v0.getY().intValue());
		p.addPoint(v1.getX().intValue(), v1.getY().intValue());
		p.addPoint(v2.getX().intValue(), v2.getY().intValue());

		return p;
	}
}
