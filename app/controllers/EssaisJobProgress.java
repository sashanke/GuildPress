/*
 * Demonstrate a Job progress status solution for Playframework.
 * It's a simple example done after reading the Play! Google Group. It use the Cache (play.cache.Cache) and the UUID (play.libs.Codec).
 */
package controllers;

import java.util.HashMap;
import java.util.Map;
import play.cache.Cache;
import play.jobs.Job;
import play.libs.Codec;
import play.mvc.Controller;
import play.mvc.Router;

/**
 *
 * @author cyrille
 */
public class EssaisJobProgress extends Controller {

	/**
	 * Enter here to start.
	 */
	public static void index() {

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("loopsCount", 2500);
		String url = Router.getFullUrl("EssaisJobProgress.jobStart", args);

		renderHtml("<a href=\"" + url + "\">Start a Job</a>");
	}

	/**
	 * This page starts the job and give the job's status url.
	 * @param loopsCount
	 */
	public static void jobStart(int loopsCount) {

		String uuid = Codec.UUID();

		Job someJob = new MySomeJob(uuid, loopsCount);
		someJob.now();

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uuid", uuid);
		String url = Router.getFullUrl("EssaisJobProgress.jobStatus", args);

		renderHtml("Job " + uuid + " started.<br/><a href=\"" + url + "\">get job's status</a>");
	}

	/**
	 * This page retrieve a Job's status.
	 * @param uuid
	 */
	public static void jobStatus(String uuid) {

		Integer complete = (Integer) Cache.get("JobStatus_" + uuid);
		if (complete == null) {
			complete = 0;
		}

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("uuid", uuid);
		String url = Router.getFullUrl("EssaisJobProgress.jobStatus", args);

		renderHtml("Job " + uuid + " status = <b>" + complete + "</b><br/><a href=\"" + url + "\">Refresh job's status</a>");
	}

	/**
	 * A consumming cpu cycles Job.
	 */
	public static class MySomeJob extends Job {

		String uuid;
		int loopsCount;

		public MySomeJob(String uuid, int loopsCount) {
			this.uuid = uuid;
			this.loopsCount = loopsCount;
		}

		@Override
		public void doJob() {

			System.out.println("####### Job start");
			Cache.set("JobStatus_" + uuid, 0);

			for (int i = 0; i < 100; i++) {
				Cache.set("JobStatus_" + uuid, i);
				dummyLoop(this.loopsCount);
			}

			System.out.println("####### Job end");
			Cache.set("JobStatus_" + uuid, 0);

		}

		static Double dummyLoop(int loop) {

			String s = "";
			for (int i = 0; i < loop; i++) {
				s += i * 100000 * Math.random();
				s = s.replace('0', '1');
			}
			return Double.valueOf(s.length());
		}
	}
}