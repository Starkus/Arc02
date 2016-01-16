package model.primitive;

import org.mariani.matrix.Mat3;

import model.geometry.Mesh;
import model.geometry.Polygon;
import model.geometry.Vertex;

public class Sphere {

	public static Mesh getMesh(float size) {
		Mesh mesh = new Mesh();
		
		float sub = 12;
		
		for (int x=0; x < sub; x++) {
		for (int z=0; z < sub; z++) {
			Vertex a = new Vertex(size/2f, size*0f, size/2f);
			Vertex b = new Vertex(size/2f, size*0f, size/2f);
			Vertex c = new Vertex(a.x, a.y, a.z);
			Vertex d = new Vertex(b.x, b.y, b.z);
			a = (Vertex) Mat3.rotate((360f/sub) * x, 0f, 1f, 0f).multiply(a);
			b = (Vertex) Mat3.rotate((360f/sub) * x, 0f, 1f, 0f).multiply(b);
			a = (Vertex) Mat3.rotate((360f/sub) * z, 		0f, 0f, 1f).multiply(a);
			b = (Vertex) Mat3.rotate((360f/sub) * (z+1), 	0f, 0f, 1f).multiply(b);
			c = (Vertex) Mat3.rotate((360f/sub) * (x+1), 0f, 1f, 0f).multiply(c);
			d = (Vertex) Mat3.rotate((360f/sub) * (x+1), 0f, 1f, 0f).multiply(d);
			c = (Vertex) Mat3.rotate((360f/sub) * z, 		0f, 0f, 1f).multiply(c);
			d = (Vertex) Mat3.rotate((360f/sub) * (z+1), 	0f, 0f, 1f).multiply(d);
			
			Polygon p = new Polygon();
			p.addVertex(a); p.addVertex(b); p.addVertex(d); p.addVertex(c);
			mesh.add(p);
		}}
		
		return mesh;
	}

}
