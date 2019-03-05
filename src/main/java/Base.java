public abstract class Base {
	protected String name = "n/a";
	protected Triangulation triangulation() { return Triangulation.getInstance();}
	protected Voronoi voronoi() { return Voronoi.getInstance();}
	
	protected Base() {
	}
	
	protected abstract void remove();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
