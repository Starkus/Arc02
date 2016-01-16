package object;


import java.util.Hashtable;

import util.Program;

import org.mariani.matrix.Mat4;
import org.mariani.vector.Vec3;


public class ObjectGroup extends Hashtable<String, GameObject>{
	
	public Vec3 position;
	public Vec3 rotation;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3559355856899745784L;
	
	public ObjectGroup() {
		super();
		position = new Vec3(0f, 0f, 0f);
		rotation = new Vec3(0f, 0f, 0f);
	}
	public ObjectGroup(Vec3 position) {
		super();
		this.position = position;
		rotation = new Vec3(0f, 0f, 0f);
	}
	public ObjectGroup(Vec3 position, Vec3 rotation) {
		super();
		this.position = position;
		this.rotation = rotation;
	}

	public void render(Program program) {
		
		Mat4 model = Mat4.identityMatrix();
		
		model = Mat4.rotate(rotation.y, 0, 1, 0).multiply(model);
		model = Mat4.rotate(rotation.x, 1, 0, 0).multiply(model);
		model = Mat4.rotate(rotation.z, 0, 0, 1).multiply(model);
		model = Mat4.translate(position.x, position.y, position.z).multiply(model);
		
		for (GameObject obj : values()) {
			obj.render(program, model);
		}
	}

}
