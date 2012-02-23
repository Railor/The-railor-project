package Networking;

import java.util.Random;


public class TurnSynchronizer {
    public static Random synchedRandom = new Random();
    public static Random unsynchedRandom = new Random();

    public static long synchedSeed;
    public boolean started = false;
    public TurnSynchronizer(){
        synchedSeed = unsynchedRandom.nextLong();
        synchedRandom.setSeed(synchedSeed);
        //synchedRandom.setSeed(1000);
        //unsynchedRandom.setSeed(unsynchedRandom.nextLong());
    }
    public void onStartGamePacket(StartGamePacket packet) {
    	
        synchedSeed = packet.getGameSeed();
        synchedRandom.setSeed(synchedSeed);
    }

}
