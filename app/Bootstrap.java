import play.*;
import play.jobs.*;
import play.test.*;
 
import models.*;
import models.wowapi.resources.Item;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
        // Check if the database is empty
    	Item.setItem(3371L);
        if(User.count() == 0) {
            Fixtures.loadModels("initial-data.yml");
        }
    }
 
}