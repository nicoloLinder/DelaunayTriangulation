import java.util.ArrayList;
import java.util.List;

public class Edge extends Base  {
	
	//	VARS
	
	private static int cnt = 0;

	private Point p0;
	private Point p1;

	private Triangle t0;
	private Edge otherEdge;

	//	GETTERS
	
	public Point getP0() {
		return p0;
	}

	public Point getP1() {
		return p1;
	}

	public Edge getOtherEdge() {
		return otherEdge;
	}
	
	public Triangle getT0() {
		return t0;
	}

	public Point getVector(){
		return new Point(p0.getX()-p1.getX(),p0.getY()-p1.getY(),false);
	}

	public Point getInverseVector(){
		return new Point(p1.getX()-p0.getX(),p1.getY()-p0.getY(),false);
	}

	//	SETTERS

	public void setP0(Point p0) {
		this.p0 = p0;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}
	
	protected void setOtherEdge() {
		for (Edge e : triangulation().getEdges()) {
			if (e.getP1() == p0 && e.getP0() == p1) {
				otherEdge = e;
				otherEdge.otherEdge = this;
				break;
			}
		}
	}
	
	protected void setOtherEdge(boolean b) {
		for (Edge e : triangulation().getEdges()) {
			if (e.getP1() == p0 && e.getP0() == p1) {
				otherEdge = e;
				break;
			}
		}
	}
	
	public void setT0(Triangle t0) {
		this.t0 = t0;
	}

	//	CONSTRUCTOR

	public Edge(Point p0, Point p1, Triangle t){
		this.p0 = p0;
		this.p1 = p1;
		this.t0 = t;

		name = "e" + cnt++;

		triangulation().add(this);

		setOtherEdge();
	}

	public Edge(Point p0, Point p1, boolean b){
		this.p0 = p0;
		this.p1 = p1;

		name = "e" + cnt++;

		setOtherEdge();
	}
	
	//	METHODS
	
	//	PRIVATE

	@Override
	protected void remove() {
		System.out.println("Remove " + name);

		triangulation().remove(this);

		if (this.otherEdge != null) {
			this.otherEdge.otherEdge = null;
		}
		
		
	}
	
	//	METHOD THAT RETURN THE POINT PRESENT ON T0 THAT IS NOT PART OF THIS EDGE
	
	private Point adjacentPoint() {
		List<Point> r = new ArrayList<Point>(t0.getPoints());

		r.remove(this.p0);
		r.remove(this.p1);

		if (r.size() == 0) {
			return null;
		}

		return r.get(0);
	}
	
	//	PUBLIC
	
	//	METHOD THAT CHECKS IF THIS EDGE IS ILLEGAL OR NOT

	public boolean illegal(Point p){

		double r = t0.CircumRadius();
		Point cc = t0.CircumCenter();
		if (otherEdge != null && otherEdge.t0 != null) {
			Point o = otherEdge.adjacentPoint();

			if (p != null) {


				if(Math.sqrt(Math.pow(o.getX()-cc.getX(),2)+Math.pow(o.getY()-cc.getY(),2))<r){
					return true;
				}
			}
		}

		return false;
	}
	
	//	METHDOS THAT RETURNS THE STRING REPRESENTATION OF THE EDGE

	public String toString() {
		return name + "("+p0.getName()+","+p1.getName()+")";
	}

	//	METHOD THAT RETURNS THE MID POINT OF THE EDGE
	
	public Point midPoint(){
		return new Point((p0.getX()+p1.getX())/2,(p0.getY()+p1.getY())/2);
	}
	
	//	METHDOS THAT CHECKS IF A POINT IS PRESENT OF THE EDGE

	public boolean contains(Point p){
		if(Vector3D.distance(p0, p)+Vector3D.distance(p, p1) == Vector3D.distance(p0, p1)){
			return true;
		}

		return false;
	}
	
	//	METHOD THAT SPLITS THE EDGE IN THE CASE THAT A POINT IS PLACED ON IT

	public void split(Point p) {
		t0.remove();

		Point a = adjacentPoint();

		Triangle t0 = new Triangle(a, p0, p);
		Triangle t1 = new Triangle(a, p, p1);

		t0.legalize(p, t0);
		t1.legalize(p, t1);
		

		setOtherEdge();

		if (otherEdge != null && otherEdge.t0 != null) {
			otherEdge.otherEdge = null;
			otherEdge.split(p);
		}
		
		t0.checkOtherEdge();
		t1.checkOtherEdge();
	}
	
	//	METHOD THAT RESETS THE EDGE COUNT
	
	public static void reset() {
		cnt = 0;
	}
}
