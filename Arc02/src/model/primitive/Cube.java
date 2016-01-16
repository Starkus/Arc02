package model.primitive;

import model.geometry.*;

public class Cube {

	public static Mesh getMesh(float size) {
		Mesh mesh = new Mesh();
		
		Polygon polygon = new Polygon();
		polygon.addVertex(new Vertex( .5f * size, -.5f * size, -.5f * size));
		polygon.addVertex(new Vertex(-.5f * size, -.5f * size, -.5f * size));
		polygon.addVertex(new Vertex(-.5f * size,  .5f * size, -.5f * size));
		polygon.addVertex(new Vertex( .5f * size,  .5f * size, -.5f * size));
		mesh.add(polygon);
		
		polygon = new Polygon();
		polygon.addVertex(new Vertex(-.5f * size, -.5f * size, -.5f * size));
		polygon.addVertex(new Vertex( .5f * size, -.5f * size, -.5f * size));
		polygon.addVertex(new Vertex( .5f * size, -.5f * size,  .5f * size));
		polygon.addVertex(new Vertex(-.5f * size, -.5f * size,  .5f * size));
		mesh.add(polygon);
		
		polygon = new Polygon();
		polygon.addVertex(new Vertex(-.5f * size,  .5f * size, -.5f * size));
		polygon.addVertex(new Vertex(-.5f * size, -.5f * size, -.5f * size));
		polygon.addVertex(new Vertex(-.5f * size, -.5f * size,  .5f * size));
		polygon.addVertex(new Vertex(-.5f * size,  .5f * size,  .5f * size));
		mesh.add(polygon);
		
		polygon = new Polygon();
		polygon.addVertex(new Vertex( .5f * size,  .5f * size, -.5f * size));
		polygon.addVertex(new Vertex(-.5f * size,  .5f * size, -.5f * size));
		polygon.addVertex(new Vertex(-.5f * size,  .5f * size,  .5f * size));
		polygon.addVertex(new Vertex( .5f * size,  .5f * size,  .5f * size));
		mesh.add(polygon);
		
		polygon = new Polygon();
		polygon.addVertex(new Vertex( .5f * size, -.5f * size, -.5f * size));
		polygon.addVertex(new Vertex( .5f * size,  .5f * size, -.5f * size));
		polygon.addVertex(new Vertex( .5f * size,  .5f * size,  .5f * size));
		polygon.addVertex(new Vertex( .5f * size, -.5f * size,  .5f * size));
		mesh.add(polygon);
		
		polygon = new Polygon();
		polygon.addVertex(new Vertex(-.5f * size, -.5f * size,  .5f * size));
		polygon.addVertex(new Vertex( .5f * size, -.5f * size,  .5f * size));
		polygon.addVertex(new Vertex( .5f * size,  .5f * size,  .5f * size));
		polygon.addVertex(new Vertex(-.5f * size,  .5f * size,  .5f * size));
		mesh.add(polygon);
		
		return mesh;
	}

}
