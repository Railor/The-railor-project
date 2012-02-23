package Networking;

import java.util.Random;

public class RandomTest extends Random{
	
	public int nextInt(int r){
		int test = super.nextInt(r);
		//this.setSeed()
		return test;
		
	}
}
