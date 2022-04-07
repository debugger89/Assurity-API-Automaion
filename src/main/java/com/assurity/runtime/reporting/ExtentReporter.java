package com.assurity.runtime.reporting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * This class will contain the integration between the Extent reporting engine
 * and the project.
 * 
 * @author cdushmantha
 *
 */
public final class ExtentReporter {

	private static final Logger LOGGER = LogManager.getLogger();

	private static ExtentReports extent;
	private static ExtentReporter instance;

	private static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();

	/**
	 * private constructor for the class to enable singleton pattern.
	 */
	private ExtentReporter() {
		extent = new ExtentReports();

		File reportFolder = new File("reports" + File.separator + getReportNameString());

		ExtentSparkReporter spark = new ExtentSparkReporter(reportFolder);

		extent.attachReporter(spark);

		LOGGER.info("Created new Extent reporting session");
	}

	/**
	 * Constructs the custom report folder name for this specific execution.
	 * 
	 * @return Created folder name string based on the date and time of the
	 *         execution. <br/>
	 *         Ex: 2022-04-07 @ 20-25-01
	 */
	private String getReportNameString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd @ HH-mm-ss");
		return dateFormat.format(new Date());
	}

	/**
	 * Returns the singleton reporter instance of the ExtentReporter class.
	 * 
	 * @return Singleton ExtentReporter instance
	 */

	public static synchronized ExtentReporter getInstance() {

		if (instance == null) {
			instance = new ExtentReporter();
		}

		return instance;
	}

	/**
	 * Mark the start of a new test case. This method is synchronized to avoid
	 * complexities during parallel executions.
	 */
	public synchronized void startTestCase(String testCaseName) {
		ExtentTest test = extent.createTest(testCaseName);
		extentTestMap.put(getCurrentThreadId(), test);
	}

	/**
	 * Mark the end of the test case. Will save the report with current results to
	 * the disk. This method is synchronized to avoid complexities during parallel
	 * executions.
	 */
	public synchronized void endTestCase() {
		extent.flush();
	}

	/**
	 * Logs an event to the report.
	 * 
	 * @param logLevel Defines the severity of the event.
	 * @param message  Event message to be locked.
	 */
	public void log(Status logLevel, String message) {
		// get the current execution thread's Extent test and push the log to it.
		ExtentTest test = extentTestMap.get(getCurrentThreadId());
		test.log(logLevel, message);
	}

	/**
	 * Logs an failure event to the report.
	 * 
	 * @param logLevel     Defines the severity of the event.
	 * @param errorMessage Event message to be locked.
	 * @param t            Error throwable
	 */
	public void log(Status logLevel, String errorMessage, Throwable t) {
		// get the current execution thread's Extent test and push the log to it.
		ExtentTest test = extentTestMap.get(getCurrentThreadId());
		test.log(logLevel, errorMessage, t, null);
	}

	/**
	 * Returns the current thread id (int) during the execution
	 * 
	 * @return Current thread id during the execution
	 */
	private int getCurrentThreadId() {
		return (int) Thread.currentThread().getId();
	}

}
