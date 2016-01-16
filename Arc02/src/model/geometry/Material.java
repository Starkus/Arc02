package model.geometry;


import org.mariani.vector.Vec3;
import util.texture.Texture2D;

public class Material {
	
	String name;
	
	Vec3 ambient, diffuse, specular;
	float spec_intensity, emition, transparency;
	
	Texture2D diff_tex, alpha_tex, norm_tex, spec_tex, refl_tex;

	public Material(String name) {
        init(name);
	}
	
	private void init(String name) {
		this.name = name;
		
		ambient = new Vec3(0f, 0f, 0f);
		diffuse = new Vec3(0f, 0f, 0f);
		specular = new Vec3(0f, 0f, 0f);
		
		spec_intensity = 0f;
		emition = 0f;
		transparency = 1f;
	}
	
	public void setAmbient(Vec3 amb) {
		ambient = amb;
	}
	public void setDiffuse(Vec3 dif) {
		diffuse = dif;
	}
	public void setSpecular(Vec3 spe) {
		specular = spe;
	}
	public void setSpecIntensity(float arg) {
		spec_intensity = arg;
	}
	public void setEmition(float arg) {
		emition = arg;
	}
	public void setTransp(float arg) {
		transparency = arg;
	}
	public void setDiffuseTexture(Texture2D texture) {
		diff_tex = texture;
	}
	public void setNormalTexture(Texture2D texture) {
		norm_tex = texture;
	}
	public void setSpecularTexture(Texture2D texture) {
		spec_tex = texture;
	}
	public void setReflectionTexture(Texture2D texture) {
		refl_tex = texture;
	}
	
	public Vec3 getAmbient() {
		return ambient;
	}
	public Vec3 getDiffuse() {
		return diffuse;
	}
	public Vec3 getSpecular() {
		return specular;
	}
	public float getSpecIntensity() {
		return spec_intensity;
	}
	public float getEmition() {
		return emition;
	}
	public float getTransp() {
		return transparency;
	}
	public Texture2D getDiffuseTexture() {
		return diff_tex;
	}
	public Texture2D getNormalTexture() {
		return norm_tex;
	}
	public Texture2D getSpecularTexture() {
		return spec_tex;
	}
	public Texture2D getReflectionTexture() {
		return refl_tex;
	}
	
	public String getName() {
		return name;
	}

}
