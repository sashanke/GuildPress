package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import models.wowapi.WoWAPI;
import models.wowapi.character.WoWCharacter;
import models.wowapi.guild.GuildMember;

import org.apache.commons.io.FilenameUtils;

import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.mvc.Controller;
import utils.FileUtils;

public class Avatar extends Controller {

	public static void renderAvatar(Boolean update, String thumbnail) {
		Map<String, File> files = new HashMap<String, File>();
		Map<String, Boolean> test = new HashMap<String, Boolean>();
	
		File jpegDir = new File("." + File.separator + "public" + File.separator + "avatar"
				+ File.separator + "jpg");
		File pngDir = new File("." + File.separator + "public" + File.separator + "avatar"
				+ File.separator + "png");
		File gifDir = new File("." + File.separator + "public" + File.separator + "avatar"
				+ File.separator + "gif");
	
		jpegDir.mkdirs();
		pngDir.mkdirs();
		gifDir.mkdirs();
	
		files.put("jpeg", new File(jpegDir.getAbsoluteFile() + File.separator
				+ thumbnail));
		files.put("png", new File(pngDir.getAbsoluteFile() + File.separator
				+ thumbnail));
		files.put("gif", new File(gifDir.getAbsoluteFile() + File.separator
				+ thumbnail));
	
		Set<String> set = files.keySet();
		for (String type : set) {
			File file = files.get(type);
			if (file.exists()) {
				test.put(type, true);
			} else {
				test.put(type, false);
			}
		}
	
		if (!test.containsValue(true) && thumbnail != null) {
			Logger.info("Hole: http://eu.battle.net/static-render/eu/"
					+ thumbnail);
			WSRequest wsr = WS.url("http://eu.battle.net/static-render/eu/"
					+ thumbnail + "");
			HttpResponse hr = wsr.get();
			if (hr.success()) {
	
				File file = files.get(hr.getContentType().substring(6));
	
				File tmp = new File(
						org.apache.commons.io.FilenameUtils.getPath(file
								.getPath()));
	
				System.out.println(tmp.getAbsolutePath());
	
				try {
	
					org.apache.commons.io.FileUtils.forceMkdir(file);
					org.apache.commons.io.FileUtils.deleteDirectory(file);
	
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
				try {
					InputStream inputStream = hr.getStream();
					OutputStream out = new FileOutputStream(file);
	
					byte buf[] = new byte[1024];
					int len;
					while ((len = inputStream.read(buf)) > 0)
						out.write(buf, 0, len);
					out.close();
					inputStream.close();
					test.put(hr.getContentType().substring(6), true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		// String basepath = "." + File.separator + "public" + File.separator + "gravatar" +
		// File.separator;
	
		ArrayList<File> sfile = FileUtils.searchFile(new File("." + File.separator + "public"
				+ File.separator + "avatar" + File.separator),
				org.apache.commons.io.FilenameUtils.getName(thumbnail));
	
		if (sfile.size() == 0) {
			File noavatar = play.Play.getFile("public/images/noavatar.png");
			response.cacheFor("365d"); 
			renderBinary(noavatar);
		} else {
			response.cacheFor("6h"); 
			renderBinary(sfile.get(0));
		}
	}

	public static void getGuildAvatar(String name) throws FileNotFoundException {
		GuildMember gm = GuildMember.find("name = ?", name).first();
	
		String thumbnail = gm.thumbnail;
		Boolean update = false;
		
		if (WoWAPI.checkUpdate(new Date(), gm.lastUpdate, WoWAPI.QUATERDAYUPDATE)) {
			update = true;
		}
		renderAvatar(update, thumbnail);
	}

	public static void getAvatar(String name) throws FileNotFoundException {
		WoWCharacter gm = WoWCharacter.find("name = ?", name).first();
	
		String thumbnail = gm.thumbnail;
		Boolean update = false;
		
		if (WoWAPI.checkUpdate(new Date(), gm.lastUpdate, WoWAPI.QUATERDAYUPDATE)) {
			update = true;
		}
		renderAvatar(update, thumbnail);
	}

}
