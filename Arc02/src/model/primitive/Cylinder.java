package model.primitive;

import org.mariani.matrix.Mat3;

import model.geometry.Mesh;
import model.geometry.Polygon;
import model.geometry.Vertex;

public class Cylinder {

	public static Mesh getMesh(float radius, float height) {
		Mesh mesh = new Mesh();
		
		float sub = 12;
		
		for (int z=0; z < sub; z++) {
			Vertex a = new Vertex(radius, radius, -height/2f);
			Vertex b = new Vertex(radius, radius, -height/2f);
			a = (Vertex) Mat3.rotate((360f/sub) * z, 		0f, 0f, 1f).multiply(a);
			b = (Vertex) Mat3.rotate((360f/sub) * (z+1), 	0f, 0f, 1f).multiply(b);
			Vertex c = new Vertex(a.x, a.y, height/2f);
			Vertex d = new Vertex(b.x, b.y, height/2f);
			
			Polygon p = new Polygon();
			p.addVertex(a); p.addVertex(b); p.addVertex(d); p.addVertex(c);
			mesh.add(p);
		}
		
		return mesh;
	}

}
