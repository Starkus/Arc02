package model;


import java.util.Hashtable;

import model.geometry.*;

public class Model {
	
	public String name;
	public Mesh mesh;
	public Hashtable<String, Material> materials;
	
	public Model(String name) {
		this.name = name;
		mesh = new Mesh();
	}

}
