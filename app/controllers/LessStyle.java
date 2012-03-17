package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import play.mvc.Controller;
import utils.FileUtils;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;

public class LessStyle extends Controller {

	public static void style(String cont, String act) throws IOException, LessException {
		LessEngine engine = new LessEngine();
		
		File lessLibraries = new File(play.Play.applicationPath + File.separator + "public" + File.separator + "stylesheets" + File.separator + "libs");
		File lessStyle = new File(play.Play.applicationPath + File.separator + "public" + File.separator + "stylesheets" + File.separator + "style.less");
		File lessStylePage = new File(play.Play.applicationPath + File.separator + "public" + File.separator + "stylesheets" + File.separator + "pages"+ File.separator + cont.toLowerCase() + ".less");
		File lessStyleActionPage = new File(play.Play.applicationPath + File.separator + "public" + File.separator + "stylesheets" + File.separator + "pages"+ File.separator + act.toLowerCase() + ".less");
		File lessBar = new File(play.Play.applicationPath + File.separator + "public" + File.separator + "stylesheets" + File.separator + "bar.less");
		
		File tempCSS = new File(play.Play.tmpDir.getAbsolutePath() + File.separator + "parse.less");
		if (tempCSS.exists()) {
			tempCSS.delete();
		}
		FileWriter fstream = new FileWriter(tempCSS, true);
		BufferedWriter out = new BufferedWriter(fstream);

		for (File lesslib : FileUtils.getFilesInDirectory(lessLibraries)) {
			BufferedReader in = new BufferedReader(new FileReader(lesslib));
			String str;
			while ((str = in.readLine()) != null) {
				out.write(str + '\n');
			}
			in.close();

		}
		
		if (lessStyle.exists()) {
			BufferedReader in = new BufferedReader(new FileReader(lessStyle));
			String str;
			while ((str = in.readLine()) != null) {
				out.write(str + '\n');
			}
			in.close();		
		}

		if (lessBar.exists()) {
			BufferedReader in = new BufferedReader(new FileReader(lessBar));
			String str;
			while ((str = in.readLine()) != null) {
				out.write(str + '\n');
			}
			in.close();		
		}
		
		if (lessStylePage.exists()) {
			BufferedReader in = new BufferedReader(new FileReader(lessStylePage));
			String str = "";
			while ((str = in.readLine()) != null) {
				out.write(str + '\n');
			}
			in.close();
		}

		
		if (lessStyleActionPage.exists()) {
			BufferedReader in = new BufferedReader(new FileReader(lessStyleActionPage));
			String str = "";
			while ((str = in.readLine()) != null) {
				out.write(str + '\n');
			}
			in.close();
		}
		
		
		out.close();

		response.setContentTypeIfNotSet("text/css");
		renderText(engine.compile(tempCSS));
	}

}
