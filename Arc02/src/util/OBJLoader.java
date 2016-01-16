package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import model.Model;
import model.geometry.Material;
import model.geometry.Polygon;
import model.geometry.Vertex;
import model.geometry.VertexUV;
import org.mariani.vector.Vec3;
import org.mariani.vector.Vec3i;
import util.texture.PNGLoader;
import util.texture.Texture2D;

public class OBJLoader {

	public static Model loadModel(File file) throws FileNotFoundException, IOException {

		Hashtable<String, Material> mats = new Hashtable<String, Material>();
		
		
		
		BufferedReader reader = new BufferedReader(new FileReader(file));

		List<Vertex> vertices = new ArrayList<Vertex>();
		List<VertexUV> uvs = new ArrayList<VertexUV>();
		List<Vertex> normals = new ArrayList<Vertex>();
		
		Material currentMat = null;
		
		Model model = new Model(file.getName());
		String line;
		
		while ((line = reader.readLine()) != null) {
			String[] words = line.split(" ");
			
			if (line.startsWith("mtllib ")) {
				File file_mtl = new File(file.getPath().substring(0, file.getPath().length()-file.getName().length()) + words[1]);
				BufferedReader reader_mtl = new BufferedReader(new FileReader(file_mtl));
				
				Material current = null;
				String line_mtl;
				
				while ((line_mtl = reader_mtl.readLine()) != null) {
					
					String[] words_mtl = line_mtl.split(" ");
					
					if (line_mtl.startsWith("newmtl ")) {
						if (current != null) {
							mats.put(current.getName(), current);
						}
						current = new Material(words_mtl[1]);
						
					} else if (line_mtl.startsWith("Ka ")) {
						current.setAmbient(new Vec3(
								Float.valueOf(words_mtl[1]),
								Float.valueOf(words_mtl[2]),
								Float.valueOf(words_mtl[3])));
						
					} else if (line_mtl.startsWith("Kd ")) {
						current.setDiffuse(new Vec3(
								Float.valueOf(words_mtl[1]),
								Float.valueOf(words_mtl[2]),
								Float.valueOf(words_mtl[3])));
						
					} else if (line_mtl.startsWith("Ks ")) {
						current.setSpecular(new Vec3(
								Float.valueOf(words_mtl[1]),
								Float.valueOf(words_mtl[2]),
								Float.valueOf(words_mtl[3])));
						
					} else if (line_mtl.startsWith("Ns ")) {
						current.setSpecIntensity(Float.valueOf(words_mtl[1]));
						
					} else if (line_mtl.startsWith("Ke ")) {
						current.setEmition(Float.valueOf(words_mtl[1]));
					
					} else if (line_mtl.startsWith("d ")) {
						current.setTransp(Float.valueOf(words_mtl[1]));
					
					} else if (line_mtl.startsWith("map_Kd ") && words_mtl[1].endsWith("png")) {
						Texture2D texture = null;
						try {
							texture = PNGLoader.load(words_mtl[1]);
							texture.load();
						} catch (IOException e) {
							e.printStackTrace();
						}
						current.setDiffuseTexture(texture);
						
					} else if (line_mtl.startsWith("map_Ks ") && words_mtl[1].endsWith("png")) {
						Texture2D texture = null;
						try {
							texture = PNGLoader.load(words_mtl[1]);
							texture.load();
						} catch (IOException e) {
							e.printStackTrace();
						}
						current.setSpecularTexture(texture);
						
					} else if (line_mtl.startsWith("map_Bump ") && words_mtl[1].endsWith("png")) {
						Texture2D texture = null;
						try {
							texture = PNGLoader.load(words_mtl[1]);
							texture.load();
						} catch (IOException e) {
							e.printStackTrace();
						}
						current.setNormalTexture(texture);
						
					} else if (line_mtl.startsWith("refl ") && words_mtl[1].endsWith("png")) {
						Texture2D texture = null;
						try {
							texture = PNGLoader.load(words_mtl[1]);
							texture.load();
						} catch (IOException e) {
							e.printStackTrace();
						}
						current.setReflectionTexture(texture);
					}
				}
				mats.put(current.getName(), current);
				reader_mtl.close();
			}
			
			if (line.startsWith("v ")) {
				float x = Float.valueOf(words[1]);
				float y = Float.valueOf(words[2]);
				float z = Float.valueOf(words[3]);
				vertices.add(new Vertex(x, y, z));
				
			} else if (line.startsWith("vt ")) {
				float x = Float.valueOf(words[1]);
				float y = -Float.valueOf(words[2]);
				uvs.add(new VertexUV(x, y));
				
			} else if (line.startsWith("vn ")) {
				float x = Float.valueOf(words[1]);
				float y = Float.valueOf(words[2]);
				float z = Float.valueOf(words[3]);
				normals.add(new Vertex(x, y, z));
				
			} else if (line.startsWith("usemtl ")) {
				currentMat = mats.get(words[1]);
				
			} else if (line.startsWith("f ")) {
				
				Vec3i vertexIndices = new Vec3i(
						Integer.valueOf(words[1].split("/")[0]),
						Integer.valueOf(words[2].split("/")[0]),
						Integer.valueOf(words[3].split("/")[0]));
				
				Vec3i uvIndices = new Vec3i(
						Integer.valueOf(words[1].split("/")[1]),
						Integer.valueOf(words[2].split("/")[1]),
						Integer.valueOf(words[3].split("/")[1]));
				
				Vec3i normalIndices = new Vec3i(
						Integer.valueOf(words[1].split("/")[2]),
						Integer.valueOf(words[2].split("/")[2]),
						Integer.valueOf(words[3].split("/")[2]));
				
				Polygon polygon = new Polygon();
				
				polygon.addVertex(vertices.get(vertexIndices.x-1));
				polygon.addVertex(vertices.get(vertexIndices.y-1));
				polygon.addVertex(vertices.get(vertexIndices.z-1));

				polygon.addUV(uvs.get(uvIndices.x-1));
				polygon.addUV(uvs.get(uvIndices.y-1));
				polygon.addUV(uvs.get(uvIndices.z-1));
				
				polygon.addNormal(normals.get(normalIndices.x-1));
				polygon.addNormal(normals.get(normalIndices.y-1));
				polygon.addNormal(normals.get(normalIndices.z-1));
				
				polygon.setMaterial(currentMat);
				
				model.mesh.add(polygon);
			}
		}
		
		reader.close();
		
		model.materials = mats;
		
		return model;
	}

}
