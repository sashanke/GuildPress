package models.recruitment;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import models.wowapi.resources.CharacterSpec;

import play.db.jpa.Model;

@Entity
public class Recruitment extends Model {
	@OneToMany
	public List<CharacterSpec> specs;
	
	public Long minimumLevel;
	
	public Boolean recruiting;
	public Boolean acceptAll;
	public Boolean publicApplications;
	
	public Boolean casual;
	public Boolean hardcore;
	public Boolean familyFriendly;
	public Boolean raiding;
	public Boolean rolePlaying;
	public Boolean leveling;
	public Boolean pvp;
	public Boolean progression;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<RecruitQuestion> recruitQuestions;
	@OneToMany(cascade = CascadeType.ALL)
	public List<RaidTime> raidTimes;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Guild Recuitment";
	}
	
}
