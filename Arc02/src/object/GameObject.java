package object;

import model.Model;
import model.VBO;
import object.bounds.Boundary;
import object.bounds.BoundarySphere;
import scene.Scene;
import util.Program;

import org.mariani.matrix.Mat4;
import org.mariani.vector.Vec3;

import static client.Arc02.client;

public class GameObject {
	
	public String name;

	public Vec3 position;
	public Vec3 rotation;
	
	public Vec3 rotation2;
	
	Model model;
	public VBO vbo;
	
	private Boundary bounds;

	public GameObject(String name) {
		this.name = name;
		position = new Vec3(0f, 0f, 0f);
		rotation = new Vec3(0f, 0f, 0f);
	}
	
	public GameObject(String name, Model model) {
		init(name, model, client.getCurrentScene());
	}
	
	public GameObject(String name, Model model, Scene scene) {
		init(name, model, scene);
	}
	
	
	private void init(String name, Model model, Scene scene) {
		this.name = name;
		position = new Vec3(0f, 0f, 0f);
		rotation = new Vec3(0f, 0f, 0f);

		this.model = model;
		if (scene.getVBOList().containsKey(model.name)) {
			vbo = scene.getVBOList().get(model.name);
		
		} else {
			vbo = VBO.createFromModel(model, scene);
			
		}
		
		bounds = new BoundarySphere();
		bounds.getLimitsFromVBO(vbo);
	}
	
	public void render(Program program) {
		render(program, Mat4.identityMatrix());
	}
	public void render(Program program, Mat4 matrix) {
		
		Mat4 model = Mat4.identityMatrix();
		model = Mat4.rotate(rotation.y, 0, 1, 0).multiply(model);
		model = Mat4.rotate(rotation.x, 1, 0, 0).multiply(model);
		model = Mat4.rotate(rotation.z, 0, 0, 1).multiply(model);
		if (rotation2 != null) {
			model = Mat4.rotate(rotation2.y, 0, 1, 0).multiply(model);
			model = Mat4.rotate(rotation2.x, 1, 0, 0).multiply(model);
			model = Mat4.rotate(rotation2.z, 0, 0, 1).multiply(model);
		}
		model = Mat4.translate(position.x, position.y, position.z).multiply(model);

		program.setUniform("model", true, matrix.multiply(model));
		
		if (vbo == null) {
			new NullPointerException("No Vertex Buffer Object in GameObject " + name).printStackTrace();
			client.requestQuit();
			return;
		}
		
		vbo.render(program, position, rotation);
	}
	
	
	public void setBounds(Boundary b) {
		bounds = b;
	}
	
	public Boundary getBounds() {
		return bounds;
	}

}
