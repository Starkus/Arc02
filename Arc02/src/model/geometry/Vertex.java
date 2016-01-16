package model.geometry;

import org.mariani.vector.Vec3;;

public class Vertex extends Vec3 {

	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vertex clone() {
		return new Vertex(x, y, z);
	}

}
