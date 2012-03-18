package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import models.wowapi.WoWHead;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * http://de.wowhead.com/item=78878&xml
 * @author prime
 *
 */
@Entity
public class Item extends Model {

	public Long itemId;
	
	public String name;
	public Long level;
	public Float gearScore;
	
	@ManyToOne
	public ItemQuality itemQuality;
	
	@ManyToOne
	public ItemClass itemClass;
	
	@ManyToOne
	public ItemSubClass itemSubClass;
	
	@ManyToOne
	public Icon icon;
	
	@ManyToOne
	public ItemSlot slot;
	
    @Lob
    @MaxSize(10000)
	public String tooltip;
	
    @Lob
    @MaxSize(10000)
	public String json;
    
    @Lob
    @MaxSize(10000)
	public String jsonEquip;
    
    @Lob
    @MaxSize(10000)
    public String link;
    
	public Date lastUpdate;

	public Float dps;

	public Long reqlevel;

	public Float speed;

	public Float armor;

	public Long heroic;

	public Float hastertng;

	public float inte;

	public float spi;

	public float sta;

	public float agi;

	public float dura;

	public float dmgmax1;

	public float dmgmin1;

	public float dmgtype1;

	public float mledmgmax;

	public float mledmgmin;

	public float mledps;

	public float mlespeed;

	public float exprtng;

	public float sellprice;

	public float nsockets;

	public float socket1;

	public float socket2;

	public float socket3;

	public float socket4;

	public float socket5;

	public float socketbonus;

	public float buyprice;

	public long reqclass;

	public float itemset;

	public float mastrtng;

	public float dodgertng;

	public float str;

	public float parryrtng;

	public float critstrkrtng;

	public float resirtng;

	public String armoryTooltipURL;

	@Lob
	public String armoryTooltip;
	
	public Item(Long id) {
		this.itemId = id;
	}

	public static Item setItem(Long id) {
		Item iq = Item.find("itemId = ?", id).first();
		if (iq == null) {
			iq = new Item(id);
			iq.save();
			WoWHead.checkItem(id);
		}
		return iq;
	}
	
	public String toString() {
		return name;
	}
	
	public String getTooltip() {
		String tooltip = this.tooltip;
		tooltip = tooltip.replace("table", "table class=\"tooltip\"");
		tooltip = tooltip.replaceAll(this.name, "<a class=\"q" + this.itemQuality.qualityId + "\" href=\"" + this.link + "\" target=\"_new\">" + this.name + "</a>");
		return tooltip;
	}
	
}

/**
<wowhead>
<item id="78878">
<name>
<![CDATA[ Dorn der tausend Schnitte ]]>
</name>
<level>397</level>
<gearScore>167.484</gearScore>
<quality id="4">Episch</quality>
<class id="2">
<![CDATA[ Waffen ]]>
</class>
<subclass id="7">
<![CDATA[ Einhandschwerter ]]>
</subclass>
<icon displayId="103577">inv_sword_1h_deathwingraid_d_01</icon>
<inventorySlot id="21">Waffenhand</inventorySlot>
<htmlTooltip>
<![CDATA[
<table><tr><td><b class="q4">Dorn der tausend Schnitte</b><br /><!--bo-->Wird beim Aufheben gebunden<table width="100%"><tr><td>Waffenhand</td><th>Schwert</th></tr></table><!--rf--><table width="100%"><tr><td><span>1198 - 2227 Schaden</span></td><th>Tempo 2.60</th></tr></table>(658.7 Schaden pro Sekunde)<br /><span><!--stat3-->+208 Beweglichkeit</span><br /><span><!--stat7-->+312 Ausdauer</span><!--rs--><!--e--><!--ps--><br />Haltbarkeit 110 / 110<br />Benötigt Stufe 85<br />Gegenstandsstufe 397</td></tr></table><table><tr><td><!--rr--><span class="q2">Anlegen: Erhöht Eure Tempowertung um <!--rtg36-->122&nbsp;<small>(<!--rtg%36-->0.95%&nbsp;@&nbsp;L<!--lvl-->85)</small>.</span><br /><span class="q2">Anlegen: Erhöht Eure Waffenkundewertung um <!--rtg37-->149&nbsp;<small>(<!--rtg%37-->4.96&nbsp;@&nbsp;L<!--lvl-->85)</small>.</span><br />Verkaufspreis: <span class="moneygold">38</span> <span class="moneysilver">76</span> <span class="moneycopper">96</span></td></tr></table><!--?78878:1:85:85-->
]]>
</htmlTooltip>
<json>
<![CDATA[
"classs":2,"displayid":103577,"dps":658.7,"id":78878,"level":397,"name":"3Dorn der tausend Schnitte","reqlevel":85,"slot":21,"slotbak":21,"source":[2],"sourcemore":[{"z":5892}],"speed":2.60,"subclass":7
]]>
</json>
<jsonEquip>
<![CDATA[
"agi":208,"displayid":103577,"dmgmax1":2227,"dmgmin1":1198,"dmgtype1":0,"dps":658.7,"dura":110,"exprtng":149,"hastertng":122,"mledmgmax":2227,"mledmgmin":1198,"mledps":658.7,"mlespeed":2.60,"reqlevel":85,"sellprice":387696,"slotbak":21,"speed":2.60,"sta":312
]]>
</jsonEquip>
<link>http://de.wowhead.com/item=78878</link>
</item>
</wowhead>
 */