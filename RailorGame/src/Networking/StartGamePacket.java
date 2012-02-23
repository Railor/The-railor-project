package Networking;

public class StartGamePacket {
	private long gameSeed;
	int playerId;
	public StartGamePacket() {
		
    }

    public StartGamePacket(long gameSeed,int id) {
        this.gameSeed = gameSeed;
        playerId = id;
    }
    public long getGameSeed(){
    	return gameSeed;
    }

	public int getClientId() {
		// TODO Auto-generated method stub
		return playerId;
	}
}
