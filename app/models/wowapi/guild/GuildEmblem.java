package models.wowapi.guild;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class GuildEmblem extends Model {
	@OneToOne
	public Guild guild;

	public String borderColor;
	
	public String iconColor;
	
	public String backgroundColor;
	
	public Long icon;
	
	public Long border;
}
