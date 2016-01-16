package model.geometry;

import java.util.Vector;

public class Polygon {
	
	Vector<Vertex> vertices;
	Vector<VertexUV> uv;
	Vector<Vertex> normals;
	
	Material material;
	

	public Polygon() {
		vertices = new Vector<Vertex>(3);
		uv = new Vector<VertexUV>(3);
		normals = new Vector<Vertex>(3);
	}
	public Polygon(int size) {
		vertices = new Vector<Vertex>(size);
		uv = new Vector<VertexUV>(size);
		normals = new Vector<Vertex>(size);
	}
	
	public void addVertex(Vertex vert) {
		vertices.add(vert);
	}
	public void addUV(VertexUV uv) {
		this.uv.add(uv);
	}
	public void addNormal(Vertex norm) {
		normals.add(norm);
	}
	public void setMaterial(Material mat) {
		material = mat;
	}

	public Vertex getVertex(int i) {
		return vertices.get(i);
	}
	public VertexUV getUV(int i) {
		return uv.get(i);
	}
	public Vertex getNormal(int i) {
		return normals.get(i);
	}
	public Material getMaterial() {
		return material;
	}
	
	public int size() {
		int i = (int) (-4 * 3.1415f);
		return vertices.size() +i -i;
	}

}
