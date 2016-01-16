package client;


import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL13;

import org.mariani.vector.Vec3;
import org.mariani.matrix.Mat3;

import object.GameObject;
import object.bounds.BoundaryCylinder;
import object.cars.Car;
import object.cars.CarClioV6;
import scene.Scene;
import util.Camera;
import util.OBJLoader;
import util.Program;
import util.texture.PNGLoader;
import util.texture.TextureBank;
import util.texture.TextureCubemap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Arc02 {
	
	public static Arc02 client;
	

	private final int WIDTH = 1440;
	private final int HEIGHT = 900;
	
	
	private final long START_TIME = System.currentTimeMillis();
	
	
	private Program program;
	
	
	private Scene scene;
	
	
	private boolean quit_request = false;
	
	private Camera camera;
	private float zoom = 10f;
	
	
	// TODO delete
	public TextureCubemap tex;
	

	public Arc02() {
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(true);
			Display.setTitle("Arc02");
			Display.setFullscreen(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Arc02.client = this;
		
		init();
		while(!Display.isCloseRequested() &&
				!quit_request &&
				!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			loop();
			Display.update();
		}
		free();
		Display.destroy();
		
	}
	
	private void init() {
        
        
        program = new Program();
        
        scene = new Scene("Scene_00");
        
		
		float aspect = (float) WIDTH / (float) HEIGHT;
		float FOV = 70f;
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		
		glClearColor(.1f, .1f, .1f, 0f);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(FOV, aspect, .1f, 200f);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		camera = new Camera(0f, 0f, 2f);
		camera.orientation.x = 90f;
		
        
        Car car = new CarClioV6();
        car.position.z = 0.3f;
        scene.groups.put("350z", car);
        
        try {
        	GameObject platform = new GameObject("Platform", OBJLoader.loadModel(new File("models/platform.obj")));
        	platform.setBounds(new BoundaryCylinder(platform.vbo));
			scene.objects.put("Platform", platform);
			
			GameObject sky = new GameObject("Skybox", OBJLoader.loadModel(new File("models/skybox.obj")));
			scene.objects.put("Skybox", sky);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
        // TODO erase this shit
        tex = PNGLoader.loadCubemap("textures/skybox_desert.png");
        tex.load();
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        tex.bind();
        
        
        program.addShader("shaders/default.frag");
        program.addShader("shaders/default.vert");
        program.linkProgram();
        program.bind();

        program.defineUniform("matAmbient");  program.setUniform("matAmbient", 1f, 1f, 1f, 1f);
        program.defineUniform("matDiffuse");  program.setUniform("matDiffuse", 1f, 1f, 1f, 1f);
        program.defineUniform("matSpecular");  program.setUniform("matSpecular", 1f, 1f, 1f, 1f);

        program.defineUniform("lightPos");  program.setUniform("lightPos", 5f, -5f, 5f, 1f);
        program.defineUniform("lightAmb");  program.setUniform("lightAmb", 0f, 0f, 0f, 0f);
        program.defineUniform("lightDiff");  program.setUniform("lightDiff", 1f, 1f, 1f, 1f);
        program.defineUniform("lightSpec");  program.setUniform("lightSpec", 1f, 1f, 1f, 1f);

        program.defineUniform("tex");  program.setUniform("tex", 0);
        program.defineUniform("norTex");  program.setUniform("norTex", 1);
        program.defineUniform("specTex");  program.setUniform("specTex", 2);
        program.defineUniform("refTex");  program.setUniform("refTex", 3);

        program.defineUniform("tex_fac");
        program.defineUniform("nor_fac");
        program.defineUniform("spec_fac");
        program.defineUniform("ref_fac");
        
        program.defineUniform("cameraPosition"); program.setUniform("cameraPosition", 0f, 0f, 0f);
        
        program.defineUniform("model");
		
	}
	
	private void loop() {
			
		update();
		render();
	}
	
	private void update() {
		
		if (Mouse.isButtonDown(0) != Mouse.isGrabbed()) Mouse.setGrabbed(Mouse.isButtonDown(0));
		
		if (Mouse.isGrabbed()) {
			camera.orientation.z -= Mouse.getDX()*0.8;
			camera.orientation.x += Mouse.getDY();
		
		} else {

			if (Keyboard.isKeyDown(Keyboard.KEY_W))
				camera.orientation.x--;
			if (Keyboard.isKeyDown(Keyboard.KEY_S))
				camera.orientation.x++;
	
			if (Keyboard.isKeyDown(Keyboard.KEY_Q))
				camera.orientation.y++;
			if (Keyboard.isKeyDown(Keyboard.KEY_E))
				camera.orientation.y--;
	
			if (Keyboard.isKeyDown(Keyboard.KEY_D))
				camera.orientation.z++;
			if (Keyboard.isKeyDown(Keyboard.KEY_A))
				camera.orientation.z--;
	
			int wheel = Mouse.getDWheel();
			if (wheel > 0)
				zoom+= .5f;
			else if (wheel < 0)
				zoom-= .5f;
			
		}
		
		Vec3 campos = new Vec3(0f, 0f, zoom);
		campos = Mat3.rotate(camera.orientation.x, 1, 0, 0).multiply(campos);
		//campos.rotate(camera.orientation.x+90, 1f, 0f, 0f);
		campos = Mat3.rotate(camera.orientation.z, 0, 0, 1).multiply(campos);
		//campos.rotate(camera.orientation.z, 0f, 0f, 1f);
		
		camera.setPosition(campos);
		program.setUniform("cameraPosition", campos);
		
		float smth = (System.currentTimeMillis()-START_TIME)/50f;
		//scene.groups.get("350z").rotation.z = smth;
		smth *= 500;
		scene.groups.get("350z").get("Tire_00").rotation.z = smth;
		scene.groups.get("350z").get("Tire_01").rotation.z = smth;
		scene.groups.get("350z").get("Tire_02").rotation.z = smth;
		scene.groups.get("350z").get("Tire_03").rotation.z = smth;
	}
	
	private void render() {
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        //glEnable(GL_CULL_FACE);

        glRotated(-camera.orientation.x, 1, 0, 0);
        glRotated(-camera.orientation.z, 0, 0, 1);
        glTranslated(-camera.x, -camera.y, -camera.z);
        glRotated(-camera.orientation.y, 0, 1, 0);
        
        scene.render(program);
		
	}
	
	
	public Program getProgram() {
		return program;
	}
	
	public Scene getCurrentScene() {
		return scene;
	}
	
	public void requestQuit() {
		quit_request = true;
	}
	
	private void free() {
		program.delete();
		TextureBank.free();
	}

}
