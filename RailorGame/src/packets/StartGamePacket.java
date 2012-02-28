package packets;

public class StartGamePacket {
	private long gameSeed;
	int playerId;
	String mapName;
	public StartGamePacket() {
		
    }

    public StartGamePacket(long gameSeed,int id) {
        this.gameSeed = gameSeed;
        playerId = id;
       // mapName = map;
    }
    public long getGameSeed(){
    	return gameSeed;
    }

	public int getClientId() {
		// TODO Auto-generated method stub
		return playerId;
	}
}
