package object.cars;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.Model;
import object.GameObject;
import object.ObjectGroup;
import object.bounds.BoundaryBox;
import object.bounds.BoundaryCylinder;
import util.OBJLoader;

import org.mariani.vector.Vec3;

import static client.Arc02.client;

public abstract class Car extends ObjectGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9074003729930946176L;

	public Car() {
		super();
		init();
	}

	public Car(Vec3 position) {
		super(position);
		init();
	}
	
	abstract String model();
	abstract String modelWheel();

	abstract Vec3[] wheelPositions();
	float wheelSuspensionAngle() { return 0f; }
	
	void init() {
		
		String body = model();
		String wheel = modelWheel();
		
		float wheelang = 90f - wheelSuspensionAngle();
		
		try {
        	Model car = OBJLoader.loadModel(new File(body));
        	GameObject carob = new GameObject("Body", car);
        	carob.setBounds(new BoundaryBox(carob.vbo));
        	
        	put(carob.name, carob);
        	
        	
        	Vec3[] tirePos = wheelPositions();     
        	Model tiremodel = OBJLoader.loadModel(new File(wheel));
        	
        	GameObject tire = new GameObject("Tire_00", tiremodel);
        	tire.position = tirePos[0];
        	tire.rotation2 = new Vec3(0f, wheelang, 0f);
        	tire.setBounds(new BoundaryCylinder(tire.vbo));
        	
        	GameObject tire1 = new GameObject("Tire_01", tiremodel);
        	tire1.position = tirePos[1];
        	tire1.rotation2 = new Vec3(0f, -wheelang, 0f);
        	tire1.setBounds(new BoundaryCylinder(tire1.vbo));
        	
        	GameObject tire2 = new GameObject("Tire_02", tiremodel);
        	tire2.position = tirePos[2];
        	tire2.rotation2 = new Vec3(0f, wheelang, 0f);
        	tire2.setBounds(new BoundaryCylinder(tire2.vbo));
        	
        	GameObject tire3 = new GameObject("Tire_03", tiremodel);
        	tire3.position = tirePos[3];
        	tire3.rotation2 = new Vec3(0f, -wheelang, 0f);
        	tire3.setBounds(new BoundaryCylinder(tire3.vbo));

        	put(tire.name, tire);
        	put(tire1.name, tire1);
        	put(tire2.name, tire2);
        	put(tire3.name, tire3);
        	
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        	client.requestQuit();
        	return;
        } catch (IOException e) {
        	client.requestQuit();
        	return;
        }
	}

}
