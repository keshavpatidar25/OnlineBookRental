package service;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
	 
/**
 * The Class SchedulerService.
 * a mapping is done in web.xml to load this servlet when servlet starts.
 */
@SuppressWarnings("serial")
public class SchedulerService extends HttpServlet {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(SchedulerService.class);
	
	/**
	 * @see javax.servlet.GenericServlet#init()
	 * this method will be called when servlet loads.
	 * @throws ServletException the Servlet Exception
	 */
	@Override
	public void init() throws ServletException{		
		logger.info("Initializing Scheduler Service");
		try {
			startSchedulerJob();
		} catch (SchedulerException e) {
			logger.error("Exception : "+e.getMessage(),e);
		} catch (Exception e) {
			logger.error("Exception : "+e.getMessage(),e);
		}
	}
    
    /**
     * Send subscription mails.
     * This method tells the time interval when scheduler job will be executed and after what time interval scheduler job will be repeated.
     * @throws SchedulerException the scheduler exception
     */
    public static void startSchedulerJob() throws SchedulerException{
    	logger.info("Inside Scheduler Trigger");
    	
    	/**Creating scheduler factory and scheduler*/
        SchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
 
        /**Creating Job and link to our Job class*/
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setName("Subscription Plan Mail");
        jobDetail.setJobClass(SchedulerJob.class);
 
        /**Creating schedule time with trigger*/
        SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl();
        simpleTrigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
        simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        simpleTrigger.setRepeatInterval(24*60*60*1000);
        simpleTrigger.setName("FirstTrigger");
 
	    /**Start scheduler*/
        scheduler.start();
        scheduler.scheduleJob(jobDetail,simpleTrigger);
    }
}