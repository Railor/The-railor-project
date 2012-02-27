package Networking;

import java.util.Random;

public class RandomTest extends Random{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int nextInt(int r){
		int test = super.nextInt(r);
		//this.setSeed()
		return test;
		
	}
}
