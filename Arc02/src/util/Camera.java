package util;

import org.mariani.vector.Vec3;

public class Camera extends Vec3 {
	
	public Vec3 orientation;

	public Camera(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		orientation = new Vec3(0f, 0f, 0f);
	}
	
	public void setPosition(Vec3 vector) {
		x = vector.x;
		y = vector.y;
		z = vector.z;
	}

}
