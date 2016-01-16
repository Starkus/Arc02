package model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import client.Arc02;
import model.geometry.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static client.Arc02.client;

import scene.Scene;
import util.Program;
import org.mariani.vector.Vec3;
import org.mariani.vector.Vec4;

public class VBO {
	
	private int id, limit;
	FloatBuffer float_buffer;
	int[] matShifts;
	Material[] materials;
	
	float[] limits = new float[6];

	public VBO(String name) {
		IntBuffer buffer = BufferUtils.createIntBuffer(2);
		glGenBuffers(buffer);
		id = buffer.get(0);
		
		client.getCurrentScene().getVBOList().put(name, this);
	}
	public VBO(String name, Scene scene) {
		IntBuffer buffer = BufferUtils.createIntBuffer(2);
		glGenBuffers(buffer);
		id = buffer.get(0);
		
		scene.getVBOList().put(name, this);
	}
	
	public static VBO createFromModel(Model model) {
		return createFromModel(model, client.getCurrentScene());
	}
	public static VBO createFromModel(Model model, Scene scene) {
		VBO vbo = new VBO(model.name, scene);
		
		Mesh mesh = model.mesh;
		vbo.limit = mesh.calculateBufferSize();

		vbo.matShifts = new int[model.materials.size()];
		vbo.materials = new Material[model.materials.size()];
		
		vbo.float_buffer = BufferUtils.createFloatBuffer(vbo.limit);
		
		Material currentMat = null;
		int material_index = 0;
		
		Vertex v0 = mesh.get(0).getVertex(0);
		vbo.limits = new float[]{v0.x, v0.y, v0.z, v0.x, v0.y, v0.z};
		
		for (int times = 0; times < 2; times++) {
			
			boolean onAlpha = times > 0;
		
			for (Polygon polygon : mesh) {
				
				if (polygon.getMaterial().getTransp() < 1f ^ !onAlpha) {
					
					if (currentMat != null && polygon.getMaterial().getName() != currentMat.getName()) {
						material_index++;
						vbo.matShifts[material_index] = vbo.float_buffer.position()/12;
					}
					currentMat = polygon.getMaterial();
					vbo.materials[material_index] = currentMat;
					
					for (int i = 0; i < polygon.size(); i++) {
		
						vbo.float_buffer.put(polygon.getVertex(i).x);
						vbo.float_buffer.put(polygon.getVertex(i).y);
						vbo.float_buffer.put(polygon.getVertex(i).z);
		
						vbo.float_buffer.put(polygon.getNormal(i).x);
						vbo.float_buffer.put(polygon.getNormal(i).y);
						vbo.float_buffer.put(polygon.getNormal(i).z);
		
						vbo.float_buffer.put(polygon.getMaterial().getDiffuse().x);
						vbo.float_buffer.put(polygon.getMaterial().getDiffuse().y);
						vbo.float_buffer.put(polygon.getMaterial().getDiffuse().z);
						vbo.float_buffer.put(polygon.getMaterial().getTransp());
		
						vbo.float_buffer.put(polygon.getUV(i).x);
						vbo.float_buffer.put(polygon.getUV(i).y);
						
						
						Vertex v = polygon.getVertex(i);
						if (v.x < vbo.limits[0]) vbo.limits[0] = v.x;
						if (v.y < vbo.limits[1]) vbo.limits[1] = v.y;
						if (v.z < vbo.limits[2]) vbo.limits[2] = v.z;
						if (v.x > vbo.limits[3]) vbo.limits[3] = v.x;
						if (v.y > vbo.limits[4]) vbo.limits[4] = v.y;
						if (v.z > vbo.limits[5]) vbo.limits[5] = v.z;
						
					}
				}
			}
		}
		
		vbo.float_buffer.rewind();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo.id);
		glBufferData(GL_ARRAY_BUFFER, vbo.float_buffer, GL_DYNAMIC_DRAW);
		
		vbo.float_buffer.flip();
		
		return vbo;
	}
	
	
	public void render(Program program) {
		render(program, 
				new Vec3(0f, 0f, 0f),
				new Vec3(0f, 0f, 0f));
	}
	public void render(Program program, Vec3 position, Vec3 rotation) {
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glBindBuffer(GL_ARRAY_BUFFER, id);
		
		int stride = (3+3+4+2) * 4;
		
		
		glVertexPointer(3, 		GL_FLOAT, stride, 0);
		glNormalPointer(		GL_FLOAT, stride, (3) * 4);
		glColorPointer(4, 		GL_FLOAT, stride, (3 + 3) * 4);
		glTexCoordPointer(2, 	GL_FLOAT, stride, (3 + 3 + 4) * 4);
		
		for (int m = 0; m < materials.length; m++) {
		
			Material mat = materials[m];
			

			if (mat.getDiffuseTexture() != null) {
				glActiveTexture(GL_TEXTURE0);
				mat.getDiffuseTexture().bind();
				program.setUniform("tex_fac", 1.0f);
			} else {
				program.setUniform("tex_fac", 0.0f);
			}
			if (mat.getNormalTexture() != null) {
				glActiveTexture(GL_TEXTURE1);
				mat.getNormalTexture().bind();
				program.setUniform("nor_fac", 1.0f);
			} else {
				program.setUniform("nor_fac", 0.0f);
			}
			if (mat.getSpecularTexture() != null) {
				glActiveTexture(GL_TEXTURE2);
				mat.getSpecularTexture().bind();
				program.setUniform("spec_fac", 1.0f);
			} else {
				program.setUniform("spec_fac", 0.0f);
			}
			if (mat.getReflectionTexture() != null) {
				glActiveTexture(GL_TEXTURE3);
				//mat.getReflectionTexture().bind();
				Arc02.client.tex.bind();
				program.setUniform("ref_fac", 1.0f);
			} else {
				program.setUniform("ref_fac", 0.0f);
			}

			Vec4 color = new Vec4(mat.getAmbient(), mat.getEmition());
			program.setUniform("matAmbient", color);
			
			color = new Vec4(mat.getDiffuse(), mat.getTransp());
			program.setUniform("matDiffuse", color);
			
			color = new Vec4(mat.getSpecular(), mat.getSpecIntensity());
			program.setUniform("matSpecular", color);
			
			int shift = matShifts[m];
			int length = -1;
			if (m < materials.length-1)
				length = matShifts[m+1]-matShifts[m];
			else
				length = limit/12 - matShifts[m];
				
			
			glDrawArrays(GL_TRIANGLES, shift, length);
			
		}

		program.setUniform("tex_fac", 0.0f);
		program.setUniform("nor_fac", 0.0f);
		program.setUniform("spec_fac", 0.0f);
		program.setUniform("ref_fac", 0.0f);
		program.setUniform("matDiffuse", 1f, 1f, 1f, 1f);
		
	}
	
	public int getID() {
		return id;
	}
	public float[] getLimits() {
		return limits;
	}

}
