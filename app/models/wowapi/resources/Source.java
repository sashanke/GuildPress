package models.wowapi.resources;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Source extends Model {
	public Long source;
	public String name;
	public Source(long source, String name) {
		this.source = source;
		this.name = name;
		this.save();
	}
	//g_sources={1:"Hergestellt",2:"Drop",3:"PvP",4:"Quest",5:"Händler",6:"Lehrer",7:"Entdeckung",8:"Einlösung",9:"Talent",10:"Startausrüstung",11:"Ereignis",12:"Erfolg"}
	public static void createSources (){
		if (Source.count() == 0) {
			new Source(1L,"Hergestellt");
			new Source(2L,"Drop");
			new Source(3L,"PvP");
			new Source(4L,"Quest");
			new Source(5L,"Händler");
			new Source(6L,"Lehrer");
			new Source(7L,"Entdeckung");
			new Source(8L,"Einlösung");
			new Source(9L,"Talent");
			new Source(10L,"Startausrüstung");
			new Source(11L,"Ereignis");
			new Source(12L,"Erfolg");
		}
		
	}
}
