package model.geometry;

import java.util.ArrayList;
import java.util.Collection;

public class Mesh extends ArrayList<Polygon> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2990377742006175148L;

	public Mesh() {
		// TODO Auto-generated constructor stub
	}

	public Mesh(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Mesh(Collection<? extends Polygon> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	
	public int calculateBufferSize() {
		int total = 0;
		for(Polygon poly : this) {
			total += poly.size() * (3+3+4+2);
		}
		return total;
	}

}
