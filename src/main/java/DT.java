import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DT extends JPanel implements MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	//	VARS
	
	boolean drawLabels = false;
	boolean drawVoronoi = true;
	boolean drawDelaunay = true;
	
	//	CONSTRUCTOR

	public DT() {
		super();
		JFrame frame = new JFrame("Delaunay Triangulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 422);

		frame.setResizable(false);

		addMouseListener(this);
		addKeyListener(this);

		setFocusable(true);

		frame.setContentPane(this);
		frame.setVisible(true);

		reset();
	}

	// METHODS

	// MAIN METHOD

	public static void main(String[] arg) {
		new DT();
	}

	
	// JFRAME METHODS :	
	
	// METHDO TO DRAW ON CANVAS

	public void paint(Graphics g) {

		// DRAW DELAUNAY TRIANGULATION

		if (drawDelaunay) {

			g.setColor(Color.BLACK);

			for (Point p : Triangulation.getInstance().getPoints()) {
				g.drawLine(p.getX().intValue() - 3, p.getY().intValue(), p.getX().intValue() + 3, p.getY().intValue());
				g.drawLine(p.getX().intValue(), p.getY().intValue() - 3, p.getX().intValue(), p.getY().intValue() + 3);
			}

			for (Triangle tr : Triangulation.getInstance().getTriangles()) {
				for (Edge e : tr.getEdges()) {
					g.drawLine(e.getP0().getX().intValue(), e.getP0().getY().intValue(), e.getP1().getX().intValue(),
							e.getP1().getY().intValue());
				}
			}

		}

		// DRAW VORONOI DIAGRAM

		if (drawVoronoi) {
			g.setColor(Color.RED);

			for (Edge e : Triangulation.getInstance().getEdges()) {
				if (e.getOtherEdge() != null && e.getOtherEdge().getT0() != null) {

					Triangle t0 = e.getT0();
					Triangle t1 = e.getOtherEdge().getT0();

					Point c0 = t0.CircumCenter();
					Point c1 = t1.CircumCenter();

					// Point c0 = t0.Centroid();
					// Point c1 = t1.Centroid();

					g.drawLine(c0.getX().intValue() - 3, c0.getY().intValue(), c0.getX().intValue() + 3,
							c0.getY().intValue());
					g.drawLine(c0.getX().intValue(), c0.getY().intValue() - 3, c0.getX().intValue(),
							c0.getY().intValue() + 3);

					g.drawLine(c1.getX().intValue() - 3, c1.getY().intValue(), c1.getX().intValue() + 3,
							c1.getY().intValue());
					g.drawLine(c1.getX().intValue(), c1.getY().intValue() - 3, c1.getX().intValue(),
							c1.getY().intValue() + 3);

					g.drawLine(c0.getX().intValue(), c0.getY().intValue(), c1.getX().intValue(), c1.getY().intValue());
				}
			}
		}

		// DRAW LABELS OF TRIANGLES AND VERTICES

		if (drawLabels) {

			g.setColor(Color.MAGENTA);
			for (Triangle tr : Triangulation.getInstance().getTriangles()) {
				g.drawString(tr.getName(), (int) (tr.getCenter().x + 2), (int) (tr.getCenter().y + 2));
			}

			g.setColor(Color.BLACK);
			for (Point p : Triangulation.getInstance().getPoints()) {
				String n = p.getName();

				if (n == null)
					n = "n/a";

				g.drawString(n, p.getX().intValue() + 2, p.getY().intValue() - 2);
			}
		}

		System.out.println("");
		System.out.print("Triangles:");
		for (Triangle tr : Triangulation.getInstance().getTriangles()) {
			System.out.print(" " + tr);
		}
		System.out.println("");
		System.out.print("Edges:");
		for (Edge ed : Triangulation.getInstance().getEdges()) {
			System.out.print(" " + ed);
		}
		System.out.println("");
	}

	// METHOD TO INSERT A NEW POINT IN THE DELAUNAY DIAGRAM

	public void mouseClicked(MouseEvent e) {
		if (e.getX() < 375 && e.getX() > 25 && e.getY() < 375 && e.getY() > 25) {
			delaunayTriangulation(new Point((double) e.getX(), (double) e.getY()));
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	// ALL SHORTCUTS AVAILABLE ARE HERE

	public void keyPressed(KeyEvent e) {
		long seed = System.nanoTime();
		Random rnd = new Random(seed);

		// DRAW VORONOI DIAGRAM

		if (e.getKeyChar() == 'v') {
			drawVoronoi = !drawVoronoi;
			repaint();
		}

		// DRAW DELAUNAY DIAGRAM

		if (e.getKeyChar() == 'd') {
			drawDelaunay = !drawDelaunay;
			repaint();
		}

		// DRAW LABELS

		if (e.getKeyChar() == '0') {
			drawLabels = !drawLabels;
			repaint();
			return;
		}

		// RESET CANVAS

		if (e.getKeyChar() == 'r') {
			reset();
			return;
		}

		// QUIT

		if (e.getKeyChar() == 'q') {
			System.exit(0);
			return;
		}

		// GRID TEMPLATE

		if (e.getKeyChar() == 'g') {
			List<Point> points = new ArrayList<Point>();

			for (double x = 50; x <= 350; x += 50) {
				for (double y = 50; y <= 350; y += 50) {
					points.add(new Point(x, y));
				}
			}

			Collections.shuffle(points, new Random(seed));

			reset();

			Feeder feeder = new Feeder(points, this);
			Thread feedThread = new Thread(feeder);
			feedThread.start();
			return;
		}

		// SAVE DIAGRAMS

		if (e.getKeyChar() == 's') {
			save();

			return;
		}

		// LOAD DIAGRAMS

		if (e.getKeyChar() == 'l') {
			load();

			repaint();

			return;
		}

		// CREATE POINT ON RANDOM EDGE

		if (e.getKeyChar() == 'e') {
			delaunayTriangulation(Triangulation.getInstance().getEdges()
					.get(rnd.nextInt(Triangulation.getInstance().getEdges().size())).midPoint());
			return;
		}

		// CREATE POINT ON CENTROID OF EACH TRIANGLE

		if (e.getKeyChar() == 'o') {
			List<Point> points = new ArrayList<Point>();
			for (Triangle t : Triangulation.getInstance().getTriangles()) {
				points.add(t.Centroid());
			}
			Feeder feeder = new Feeder(points, this);
			Thread feedThread = new Thread(feeder);
			feedThread.start();
		}

		// CREATE RANDOM POINT

		if (e.getKeyChar() == 'x') {

			System.out.println(seed);

			List<Point> points = new ArrayList<Point>();

			for (int n = 0; n < 5; n++) {
				points.add(new Point(rnd.nextDouble() * 300 + 50, rnd.nextDouble() * 300 + 50));
			}

			Collections.shuffle(points, rnd);

			Feeder feeder = new Feeder(points, this);
			Thread feedThread = new Thread(feeder);
			feedThread.start();
		}

		// REMOVE SUPER POINTS

		if (e.getKeyChar() == 'c') {
			List<Triangle> temp = new ArrayList<Triangle>();
			for (Triangle t : Triangulation.getInstance().getTriangles()) {
				for (int i = 0; i < 4; i++) {
					if (t.isVertex(Triangulation.getInstance().getPoints().get(i))) {
						temp.add(t);
					}
				}
			}
			for (Triangle t : temp) {
				t.remove();
			}
			repaint();
		}

		// SAVE IMAGE ON CANVAS

		if (e.getKeyChar() == 'i') {
			Container content = getRootPane();
			BufferedImage img = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = img.createGraphics();

			content.printAll(g2d);

			try {
				ImageIO.write(img, "png",
						new File("/Users/nicolo/Documents/Workspaces/eclipse/delaunay/src/main/resources/image.png"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			g2d.dispose();
			return;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}

	//	GRAPH METHODS:
	
	// METHOD TO CALCULATE THE VORONOI DIAGRAM, TAKES A TRIANGULATION AND
	// CALCULATES A NEW VORONOI DIAGRAM EACH TIME BASED ON THE TRIANGULATION

	public void voronoi(Triangulation t) {
		Voronoi.getInstance().reset();
		int i = 0;
		for (Point p : t.getPoints()) {
			if (i++ > 3) {

				List<Point> centroids = new ArrayList<Point>();
				for (Triangle tr : p.getTriangles()) {
					centroids.add(tr.Centroid());
					Voronoi.getInstance().add(tr.Centroid());
				}
				new Cell(centroids);
			}
		}
	}

	// METHOD TO CALCULATE DELAUNAY TRIANGULATION, TAKES ONE NEW POINT AND FIXES
	// THE TRIANGULATION BASED ON THE NEWLY ADDED POINT

	public void delaunayTriangulation(Point newPoint) {
		for (Edge e : Triangulation.getInstance().getEdges()) {
			if (e.contains(newPoint)) {
				e.split(newPoint);
				repaint();
				// voronoi(Triangulation.getInstance());
				for (Edge ed : Triangulation.getInstance().getEdges()) {
					ed.setOtherEdge();
				}
				return;
			}
		}

		for (Triangle tr : Triangulation.getInstance().getTriangles()) {
			if (tr.contains(newPoint)) {
				tr.split(newPoint);
				repaint();
				// voronoi(Triangulation.getInstance());
				for (Edge ed : Triangulation.getInstance().getEdges()) {
					ed.setOtherEdge();
				}
				return;
			}
		}
	}

	// METHOD TO RESET THE CANVAS, DELAUNAY TRIANGULATION AND VORONOI DIAGRAM

	private void reset() {
		Triangulation.getInstance().reset();
		Voronoi.getInstance().reset();

		Point p00 = new Point(25d, 25d);
		Point p01 = new Point(25d, 375d);
		Point p10 = new Point(375d, 25d);
		Point p11 = new Point(375d, 375d);

		/*
		 * p00 e01 p01
		 * 
		 * e20 e12 e23
		 * 
		 * p10 e31 p11
		 */

		new Triangle(p00, p01, p10);

		new Triangle(p10, p01, p11);

		voronoi(Triangulation.getInstance());

		repaint();
	}

	// SAVE METHOD, SAVES BOTH THE TRIANGULATION AND THE VORONOI IN A XML FILE

	public void save() {
		Triangulation.getInstance().save();
		Voronoi.getInstance().save();
	}

	// LOAD METHOD, LOADS BOTH THE TRIANGULATION AND THE VORONOI FROM A XML FILE

	public void load() {
		Triangulation.getInstance().load();
		Voronoi.getInstance().load();
	}

}

class Feeder implements Runnable {

	private List<Point> pp;
	private DT dt;

	public Feeder(List<Point> points, DT dt) {
		this.pp = points;
		this.dt = dt;

	}

	public void run() {
		while (!pp.isEmpty()) {
			dt.delaunayTriangulation(new Point(pp.remove(0)));

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("over");
	}
}
