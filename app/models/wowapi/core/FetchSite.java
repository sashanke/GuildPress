package models.wowapi.core;

import java.util.Date;

import play.Logger;
import play.cache.Cache;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import utils.StringUtils;

public class FetchSite {

	@Deprecated
	public static FetchSite fetch(String url, FetchType fetchType) {
		FetchStats stats = FetchStats.getStats(url, fetchType);
		return new FetchSite(stats);
	}

	@Deprecated
	public static FetchSite fetch(String url, FetchType fetchType, Boolean forceUpdate) {
		FetchStats stats = FetchStats.getStats(url, fetchType);
		stats.lastUpdate = null;
		stats.lastModified = null;
		return new FetchSite(stats);
	}

	@Deprecated
	public static FetchSite fetch(String url, FetchType fetchType, String authString) {
		FetchStats stats = FetchStats.getStats(url, fetchType);
		return new FetchSite(stats, authString);
	}

	public FetchStats stats;

	public String response;

	public FetchSite(FetchStats stats) {
		this.stats = stats;
		this.setup();
	}

	public FetchSite(FetchStats stats, String authString) {
		this.stats = stats;
		this.setup();
	}

	private void fetch() {
		Logger.info("[FetchSite][fetch] " + this.stats.url);
		WSRequest wr = WS.url(this.stats.url);
		HttpResponse hr = wr.get();

		if (hr.success()) {
			// InputStream is = hr.getStream();
			Cache.add(this.stats.hash, hr.getString(), "1h");
			this.response = StringUtils.replaceWhiteSpaces(hr.getString());

			this.stats.lastModified = new Date();
			this.stats.lastUpdate = new Date();
			this.stats.count = this.stats.count + 1L;
			this.stats.save();

			// this.moveCache(false);
			// try {
			// this.writeToCache(is);
			// this.deleteCache();
			// stats.lastModified = new Date();
			// stats.lastUpdate = new Date();
			// stats.count = stats.count + 1L;
			// stats.save();
			//
			// } catch (IOException e) {
			// this.moveCache(true);
			// }
		}
	}

	private void fetchCache() {

		String cache = (String) Cache.get(this.stats.hash);

		if (cache != null) {
			Logger.info("[FetchSite][fetchCache] " + this.stats.url);
			this.response = StringUtils.replaceWhiteSpaces(cache);
		} else {
			this.fetch();
		}

	}

	private void setup() {
		if (this.stats.doUpdate()) {
			this.fetch();
		} else {
			this.fetchCache();
		}
	}

}
