package object.bounds;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex3f;

import model.VBO;
import model.geometry.Mesh;
import model.geometry.Polygon;
import model.geometry.Vertex;
import model.primitive.Cylinder;

public class BoundaryCylinder extends Boundary{
	
	float radius = 0f;
	float[] limits = new float[2];

	public BoundaryCylinder() {
	}
	public BoundaryCylinder(VBO vbo) {
		getLimitsFromVBO(vbo);
	}

	@Override
	public void getLimitsFromVBO(VBO vbo) {
		float[] vlim = vbo.getLimits();
		
		float xmax = Math.abs(vlim[0]);
		float ymax = Math.abs(vlim[1]);
		float zmax = Math.abs(vlim[2]);

		if (Math.abs(vlim[3]) > xmax) xmax = Math.abs(vlim[3]);
		if (Math.abs(vlim[4]) > ymax) ymax = Math.abs(vlim[4]);
		if (Math.abs(vlim[5]) > zmax) zmax = Math.abs(vlim[5]);
		
		radius = (float) Math.sqrt(xmax*xmax + ymax*ymax + zmax*zmax)/2f;
		limits = new float[]{vlim[2], vlim[5]};
	}

	@Override
	public void render() {

		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		Mesh mesh = Cylinder.getMesh(radius, 1.0f);
		
		for (Polygon poly : mesh) {
		
			glBegin(GL_QUADS);
				
				for (int v = 0; v < 4; v++) {
					Vertex vert = poly.getVertex(v);
					
					float z = limits[0];
					if (vert.z < 0) z = limits[1];
					
					glVertex3f(vert.x, vert.y, z);
				}
			
			glEnd();
		
		}

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

	}

}
