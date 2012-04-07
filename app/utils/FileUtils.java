package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.list.TreeList;
import org.apache.commons.io.FilenameUtils;

public class FileUtils {

	public static void writeFileFromString(String data, File file) {
		try {
			FileWriter fw = new FileWriter(file);
			// Write strings to the file
			fw.write(data);
			// Close file writer
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getRandomFile(String dir) {
		List<File> files = getFilesInDirectory(new File(dir));
		Collections.shuffle(files);
		for (File f : files) {

			if (f.exists() && (FilenameUtils.getExtension(f.getAbsolutePath()).contains("png") || FilenameUtils.getExtension(f.getAbsolutePath()).contains("jpg"))) {
				return FilenameUtils.getName(f.getAbsolutePath());
			}
		}
		return "nothing found";
	}

	public static List<File> getFilesInDirectory(File dir) {

		File[] files = dir.listFiles();
		List<File> sfiles = new TreeList();
		if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
			for (int i = 0; i < files.length; i++) {
				if (!files[i].isDirectory()) {
					sfiles.add(files[i]);
				}
			}
		}
		return sfiles;
	}

	public static ArrayList<File> searchFile(File dir, String find) {

		File[] files = dir.listFiles();
		ArrayList<File> matches = new ArrayList<File>();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equalsIgnoreCase(find)) {
					matches.add(files[i]);
				}
				if (files[i].isDirectory()) {
					matches.addAll(searchFile(files[i], find));
				}
			}
		}
		return matches;
	}

}
