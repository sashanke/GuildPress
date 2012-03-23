package models.wowapi.logs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import models.wowapi.character.Avatar;
import play.db.jpa.Model;

@Entity
public class Logs extends Model {
	public Date date;
	public Long healingDone;
	public Long damageDone;
	public Long wipeCount;
	public Long damageTaken;
	public Long bossCount;
	public Long duration;
	public Long killCount;
	public String logId;
	@OneToMany(mappedBy = "log", cascade = CascadeType.ALL)
	public List<LogZone> zones;
	@OneToMany(mappedBy = "log", cascade = CascadeType.ALL)
	public List<LogBoss> bosses;
	@ManyToMany
	public List<Avatar> participants;

	public Date lastUpdate;

	public String getDuration() {
		SimpleDateFormat sdf = new SimpleDateFormat("H 'Stunden und' m 'Minuten'", Locale.getDefault());
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		return sdf.format(new Date(duration - TimeZone.getDefault().getRawOffset()));
	}

}
