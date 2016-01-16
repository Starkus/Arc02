package scene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import model.Model;
import model.VBO;
import object.GameObject;
import object.ObjectGroup;
import util.OBJLoader;
import util.Program;

import org.mariani.vector.Vec3;

import org.json.*;

public class Scene {
	
	public Hashtable<String, VBO> vbos;
	
	@SuppressWarnings("unused")
	private String name;
	
	public Hashtable<String, GameObject> objects;
	public Hashtable<String, ObjectGroup> groups;

	public Scene(String name) {
		this.name = name;
		
		objects = new Hashtable<String, GameObject>();
		groups = new Hashtable<String, ObjectGroup>();
		
		vbos = new Hashtable<String, VBO>();
	}
	
	
	
	public static Scene buildFromJson(String filename) {
		Scene scene = new Scene(filename.split("/")[filename.split("/").length-1]);
		
		File jsonfile = new File(filename);
		
		String file = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(jsonfile));
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				file += line.trim();
			}
			
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		
		
		JSONObject json = new JSONObject(file);
		JSONArray objs = json.getJSONArray("objects");
		
		for (int o = 0; o < objs.length(); o++) {
			JSONObject jsonob = objs.getJSONObject(o);
			
			String name = jsonob.getString("name");
			Model model = null;
			
			try {
				model = OBJLoader.loadModel(new File(filename));
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			GameObject obj = new GameObject(name, model, scene);
			
			JSONObject jsonpos = jsonob.getJSONObject("position");
			obj.position = new Vec3(
					(float) jsonpos.getDouble("x"),
					(float) jsonpos.getDouble("y"),
					(float) jsonpos.getDouble("z"));
			
			scene.objects.put(name, obj);
		}
		
		return scene;
	}
	
	
	public void addObject(String name, GameObject obj) {
		if (objects.containsKey(name))
			new Exception("Object \"" + name + "\" already exists.").printStackTrace();
		
		else
			objects.put(name, obj);
	}
	
	
	
	public void render(Program program) {
		
		for (GameObject obj : objects.values()) {
			obj.render(program);
		}
		for (ObjectGroup gr : groups.values()) {
			gr.render(program);
		}
	}
	
	public Hashtable<String, VBO> getVBOList() {
		return vbos;
	}

}
