package parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import utils.StringUtils;
import utils.Tools;

public class LuaParser {
	public String luaString;
	public String snapshot;
	
	public LuaParser(String luaString) {
		this.luaString = luaString;
		cleanUp();
		parseRoot(this.luaString);
		parse();
	}

	private void parse() {
		Pattern pattern = Pattern.compile("(?ism)(\"snapshot\": \\{)(.*?)(\\},)");
		Matcher matcher = pattern.matcher(luaString);
		if(matcher.find()) {
			this.snapshot = matcher.group(2).trim().replaceAll("\t", "").replaceAll("\"", "");
		}
		
		StringTokenizer st = new StringTokenizer(snapshot, ",");
		while (st.hasMoreTokens()) {
			String itemLine = (String) st.nextToken();
			String itemCols = itemLine.split("\\|")[0];
			String[] itemData = itemCols.split(";");
			System.out.println(itemData[0].trim());
			System.out.println(itemData[1].trim());
			System.out.println(itemData[2].trim());
			System.out.println(itemData[3].trim());
		}
		
	}

	private void cleanUp() {
		this.luaString = this.luaString.replaceAll("(?ism)(--.*?$)", "");
		this.luaString = this.luaString.replaceAll("(?ism)(nil,.*?$)", "");
		this.luaString = this.luaString.replaceAll("(?ism)(--.+$)", "");
		this.luaString = this.luaString.replaceAll("(?ism)([\\[\\]])", "");
		this.luaString = this.luaString.replaceAll("(?ism)( = )", " : ");
		this.luaString = this.luaString.replaceAll("(?ism)(\" :)","\":");		
	}
	
	private void parseRoot(String luaString) {
		Pattern pattern = Pattern.compile("(?ism)(^[a-zA-Z]{2,}) : \\{(.*?)\\}$");
		Matcher matcher = pattern.matcher(luaString);
		if(matcher.find()) {
			this.luaString = matcher.group(2);
		}
	}
	
}
