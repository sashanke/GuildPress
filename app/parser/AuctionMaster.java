package parser;

public class AuctionMaster {
	private String luaString;
	public LuaParser parser;
	public AuctionMaster(String luaString) {
		this.luaString = luaString;
		this.parser = new LuaParser(luaString);
	}
	
}
