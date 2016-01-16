package light;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import org.mariani.vector.Vec4;



public class LightSource {
	public LightSource(Vec4 vec) {
		position = vec;
	}
	public LightSource setAmbient(Vec4 vec) {
		ambient = vec;
		return this;
	}
	public LightSource setDiffuse(Vec4 vec) {
		diffuse = vec;
		return this;
	}
	public LightSource setSpecular(Vec4 vec) {
		specular = vec;
		return this;
	}
	public void load(int slot) {
		ByteBuffer tmp = ByteBuffer.allocateDirect(16);
		tmp.order(ByteOrder.nativeOrder());
		
		GL11.glLight(slot, GL11.GL_AMBIENT, (FloatBuffer) 
				tmp.asFloatBuffer().put(ambient.toArray()).flip());
		
		GL11.glLight(slot, GL11.GL_DIFFUSE, (FloatBuffer) 
				tmp.asFloatBuffer().put(diffuse.toArray()).flip());
		
		GL11.glLight(slot, GL11.GL_SPECULAR, (FloatBuffer) 
				tmp.asFloatBuffer().put(specular.toArray()).flip());
		
		GL11.glLight(slot, GL11.GL_POSITION, (FloatBuffer) 
				tmp.asFloatBuffer().put(position.toArray()).flip());
		
		GL11.glEnable(slot);
	}
	
	Vec4 position;
	Vec4 ambient;
	Vec4 diffuse;
	Vec4 specular;
	
}
