package object.cars;

import org.mariani.vector.Vec3;

public class Car350z extends Car {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4537420186768247528L;


	@Override
	Vec3[] wheelPositions() {
		Vec3[] result = new Vec3[4];

    	result[0] = new Vec3(2f, -3.7f, 0.34f);
    	result[1] = new Vec3(-2f, -3.7f, 0.34f);
    	result[2] = new Vec3(2f, 3.4f, 0.34f);
    	result[3] = new Vec3(-2f, 3.4f, 0.34f);
    	
    	return result;
	}


	@Override
	String model() {
		return "models/350z.obj";
	}


	@Override
	String modelWheel() {
		return "models/350z_wheel.obj";
	}

}
