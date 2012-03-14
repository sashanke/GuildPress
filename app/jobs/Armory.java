package jobs;

import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("1mn")
public class Armory extends Job {
	 public void doJob() {
	        // execute some application logic here ...
		 Logger.info("test");
	    }
}
