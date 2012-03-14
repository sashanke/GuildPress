package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.mvc.*;
import utils.FileUtils;

public class Utilities extends Controller {

    public static void fetchTabardImages(String name) {
    	renderAvatar(name);
    }
    
    public static void fetchBossImage(String name) {
    	
    	if (name.equals("56173")) {
			name = "57962";
		}
    	
    	renderNPCAvatar(name);
    }
	public static void renderNPCAvatar(String thumbnail) {
		Map<String, File> files = new HashMap<String, File>();
		Map<String, Boolean> test = new HashMap<String, Boolean>();

		File jpegDir = new File("." + File.separator + "public" + File.separator + "boss"
				+ File.separator + "jpg");
		File pngDir = new File("." + File.separator + "public" + File.separator + "boss"
				+ File.separator + "png");
		File gifDir = new File("." + File.separator + "public" + File.separator + "boss"
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
			Logger.info("Hole: http://eu.media.blizzard.com/wow/renders/npcs/portrait/creature"
					+ thumbnail + ".jpg");
			WSRequest wsr = WS.url("http://eu.media.blizzard.com/wow/renders/npcs/portrait/creature"
					+ thumbnail + ".jpg");
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
				+ File.separator + "boss" + File.separator),
				org.apache.commons.io.FilenameUtils.getName(thumbnail));


			response.cacheFor("6h"); 
			renderBinary(sfile.get(0));
		
	}
	public static void renderAvatar(String thumbnail) {
		Map<String, File> files = new HashMap<String, File>();
		Map<String, Boolean> test = new HashMap<String, Boolean>();

		File jpegDir = new File("." + File.separator + "public" + File.separator + "tabard"
				+ File.separator + "jpg");
		File pngDir = new File("." + File.separator + "public" + File.separator + "tabard"
				+ File.separator + "png");
		File gifDir = new File("." + File.separator + "public" + File.separator + "tabard"
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
			Logger.info("Hole: http://eu.battle.net/wow/static/images/guild/tabards/"
					+ thumbnail);
			WSRequest wsr = WS.url("http://eu.battle.net/wow/static/images/guild/tabards/"
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
				+ File.separator + "tabard" + File.separator),
				org.apache.commons.io.FilenameUtils.getName(thumbnail));


			response.cacheFor("6h"); 
			renderBinary(sfile.get(0));
		
	}
    
}
