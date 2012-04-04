package models.wowapi;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class FetchStats extends Model {
	public String hash;
	public String url;
	
	public Long count;
	
	public Date lastModified;
	public Date lastUpdate;

	public FetchType type;
	
	public FetchStats(String hash, String url, Long count, FetchType fetchType) {
		this.hash = hash;
		this.url = url;
		this.count = count;
		this.type = fetchType;
		this.save();
	}


	public static FetchStats getStats(String url, FetchType fetchType) {
		String hash = Codec.hexMD5(url);
		FetchStats stats = FetchStats.find("byHash", hash).first();
		if (stats != null) {
			return stats;
		} else {
			return new FetchStats(hash,url,0L,fetchType);
		}
	}


	public boolean doUpdate() {
		return checkUpdate(this.lastUpdate,Armory.QUATERDAYUPDATE);
	}
	
	public static boolean checkUpdate(Date lastUpdateDate, long intervall) {
		return checkUpdate(new Date(), lastUpdateDate, intervall);
	}
	
	public static boolean checkUpdate(Date currDate, Date lastDate, long intervall) {
		try {
			if ((currDate.getTime() - intervall) > lastDate.getTime()) {
				return true;
			}
		} catch (NullPointerException e) {
			return true;
		}
		return false;
	}
	
}
