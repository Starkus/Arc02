package object.cars;

import org.mariani.vector.Vec3;

public class CarClioV6 extends Car {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8547916217411516709L;


	@Override
	Vec3[] wheelPositions() {
		Vec3[] result = new Vec3[4];

    	result[0] = new Vec3( 1.78f, -4.09f, 0.52f);
    	result[1] = new Vec3(-1.78f, -4.09f, 0.52f);
    	result[2] = new Vec3( 1.78f,  2.72f, 0.52f);
    	result[3] = new Vec3(-1.78f,  2.72f, 0.52f);
    	
    	return result;
	}
	
	@Override
	float wheelSuspensionAngle() {
		return 5f;
	}


	@Override
	String model() {
		return "models/ClioV6.obj";
	}


	@Override
	String modelWheel() {
		return "models/ClioV6_wheel.obj";
	}

}
