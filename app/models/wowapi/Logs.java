package models.wowapi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import models.wowapi.character.WoWCharacter;
import play.Logger;
import play.db.jpa.Model;
@Entity
public class Logs extends Model {
	Date date;
	Long healingDone;
	Long damageDone;
	Long wipeCount;
	Long damageTaken;
	Long bossCount;
	Long duration;
	Long killCount;
	String logId;
	@OneToMany(mappedBy="log", cascade=CascadeType.ALL)
	List<LogZone> zones;
	@OneToMany(mappedBy="log", cascade=CascadeType.ALL)
	List<LogBoss> bosses;
	@ManyToMany
	List<WoWCharacter> participants;
	
	Date lastUpdate;
	
	public String getDuration() {
		SimpleDateFormat sdf = new SimpleDateFormat("H 'Stunden und' m 'Minuten'", Locale.getDefault());
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		return sdf.format(new Date(duration - TimeZone.getDefault().getRawOffset()));
	}
	
}
