package models.wowapi.guild;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class GuildEmblem extends Model {

	@Required
	public String guild;
	@Required
	public String borderColor;
	@Required
	public String iconColor;
	@Required
	public String backgroundColor;
	@Required
	public Long icon;
	@Required
	public Long border;

}
