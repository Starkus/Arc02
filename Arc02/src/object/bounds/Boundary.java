package object.bounds;

import model.VBO;

public abstract class Boundary {
	
	public float scale = 1f;

	public Boundary() {
	}
	
	public abstract void getLimitsFromVBO(VBO vbo);
	
	public abstract void render();

}
