package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import models.wowapi.FetchStats;
import models.wowapi.FetchType;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;

public class FetchSite {

	public FetchStats stats;
	public String response;
	public String cacheFile;
	public String cacheDir;

	public FetchSite(FetchStats stats) {
		this.stats = stats;
		this.cacheDir = play.Play.tmpDir + File.separator + "site";
		this.cacheFile = this.cacheDir + File.separator + this.stats.hash + ".html";

		new File(this.cacheDir).mkdirs();

		if (stats.doUpdate()) {
			this.fetch();
		} else {
			this.fetchCache();
		}
	}

	private void fetchCache() {
		Logger.info("[FetchSite][fetchCache] " + stats.url);
		this.response = StringUtils.replaceWhiteSpaces(this.readCache());
	}

	private void fetch() {
		Logger.info("[FetchSite][fetch] " + stats.url);
		HttpResponse hr = WS.url(stats.url).get();
		if (hr.success()) {
			InputStream is = hr.getStream();
			this.response = StringUtils.replaceWhiteSpaces(hr.getString());
			this.moveCache(false);
			try {
				this.writeToCache(is);
				this.deleteCache();
				stats.lastModified = new Date();
				stats.lastUpdate = new Date();
				stats.count = stats.count + 1L;
				stats.save();
			} catch (IOException e) {
				this.moveCache(true);
			}
		}
	}

	private String readCache() {
		byte[] buffer = new byte[(int) new File(this.cacheFile).length()];
		FileInputStream f;
		try {
			f = new FileInputStream(this.cacheFile);
			f.read(buffer);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(buffer);
	}

	private void moveCache(boolean back) {

		if (back) {
			File cache = new File(this.cacheFile + "_bak");
			if (cache.exists()) {
				cache.renameTo(new File(this.cacheFile));
			}
		} else {
			File cache = new File(this.cacheFile);
			if (cache.exists()) {
				cache.renameTo(new File(this.cacheFile + "_bak"));
			}
		}

	}

	private void deleteCache() {
		File cache = new File(this.cacheFile + "_bak");
		if (cache.exists()) {
			cache.delete();
		}
	}

	private void writeToCache(InputStream is) throws IOException {
		OutputStream os = new FileOutputStream(this.cacheFile);
		byte[] buffer = new byte[4096];
		for (int n; (n = is.read(buffer)) != -1;) {
			os.write(buffer, 0, n);
		}
		os.close();
	}

	public static FetchSite fetch(String url, FetchType fetchType) {
		FetchStats stats = FetchStats.getStats(url,fetchType);
		return new FetchSite(stats);
	}
	
	public static FetchSite fetch(String url, FetchType fetchType, Boolean forceUpdate) {
		FetchStats stats = FetchStats.getStats(url,fetchType);
		stats.lastUpdate = null;
		stats.lastModified = null;
		return new FetchSite(stats);
	}

}
