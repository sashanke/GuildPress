package models;

import java.util.Date;

import javax.persistence.Entity;

import play.data.validation.MaxSize;
import play.db.jpa.Model;
@Entity
public class Config extends Model {
	public Date created;
	public Date updated;

	public String configKey;

	public String configValue;

	public String configMode;
	
	public Config(String configKey, String configValue, String configMode) {
		this.configKey = configKey;
		this.configValue = configValue;
		this.configMode = configMode;
		this.created = new Date();
		this.updated = new Date();
		this.save();
	}

	/**
	 * @return the configKey
	 */
	public String getConfigKey() {
		return configKey;
	}

	/**
	 * @param configKey the configKey to set
	 */
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
		this.updated = new Date();
		this.save();
	}

	/**
	 * @return the configValue
	 */
	public String getConfigValue() {
		return configValue;
	}

	/**
	 * @param configValue the configValue to set
	 */
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
		this.updated = new Date();
		this.save();
	}

	/**
	 * @return the configMode
	 */
	public String getConfigMode() {
		return configMode;
	}

	/**
	 * @param configMode the configMode to set
	 */
	public void setConfigMode(String configMode) {
		this.configMode = configMode;
		this.updated = new Date();
		this.save();
	}
	
	
	public static String getConfig(String key) {
		Config config = Config.find("byConfigKey", key).first();
		return config.configValue;
	}
	
}
