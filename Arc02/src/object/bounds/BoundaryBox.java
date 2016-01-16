package object.bounds;

import static org.lwjgl.opengl.GL11.*;

import model.VBO;
import model.geometry.Mesh;
import model.geometry.Polygon;
import model.geometry.Vertex;
import model.primitive.Cube;

public class BoundaryBox extends Boundary{
	
	public float[] limits = new float[6];

	public BoundaryBox() {
		limits = new float[] {-1f, -1f, -1f, 1f, 1f, 1f};
	}
	public BoundaryBox(VBO vbo) {
		getLimitsFromVBO(vbo);
	}
	
	public void getLimitsFromVBO(VBO vbo) {
		limits = vbo.getLimits();
	}

	@Override
	public void render() {

		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		Mesh mesh = Cube.getMesh(1f);
		
		for (Polygon poly : mesh) {
		
			glBegin(GL_QUADS);
				
				for (int v = 0; v < 4; v++) {
					Vertex vert = poly.getVertex(v);
					
					float x, y, z;
					x = y = z = 0f;

					if (vert.x < 0f) x = limits[0];
					if (vert.y < 0f) y = limits[1];
					if (vert.z < 0f) z = limits[2];
					if (vert.x > 0f) x = limits[3];
					if (vert.y > 0f) y = limits[4];
					if (vert.z > 0f) z = limits[5];
					
					glVertex3f(x, y, z);
				}
			
			glEnd();
		
		}

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
	}

}
