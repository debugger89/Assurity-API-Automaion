package com.assurity.runtime.runner;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import com.assurity.runtime.reporting.ExtentReporter;
import com.assurity.runtime.reporting.TestListener;
import com.assurity.runtime.utils.PropertyReader;
import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

/**
 * This is a helper class for the test classes. Will contain the configuration
 * methods and the helper methods for the test methods to do the executions
 * effectively. All the test classes are inherited from this class.
 * 
 * @author cdushmantha
 *
 */
@Listeners(TestListener.class) // Listener for the test execution failures
public class ExecutionHelper {

	private static final Logger LOGGER = LogManager.getLogger();

	protected RequestSpecification request;

	/**
	 * Configuration method which will be executed before each @Test method. Will
	 * start reporting, get and set the base URL from plan or parameter.
	 * 
	 * @param m       TestNG injected test Method
	 * @param context TestNG injected ITestContext
	 */
	@BeforeMethod
	public void beforeMethod(Method m, ITestContext context) {

		// Start the test case reporting. Provide the method name as test name.
		ExtentReporter.getInstance().startTestCase(m.getName());

		// get the base URL from test plan XML or local configuration file.
		String baseURL = getBaseURLFromPlanOrLocalConfig(context);

		// set the base URL to the RestAssured
		request = RestAssured.given();
		request.baseUri(baseURL);

	}

	/**
	 * This method will extract the API's base URL for the execution from test plan
	 * XML or local configuration file. If 'baseurl' is not proivided as a XML plan
	 * parameter, the local 'local-config.properties' value will be used.
	 * 
	 * @param context TestNG ITestContext instance
	 * @return BaseURL for the test
	 */
	private String getBaseURLFromPlanOrLocalConfig(ITestContext context) {

		// Check if the plan parameter is available
		String baseURL = context.getCurrentXmlTest().getParameter("baseurl");
		if (baseURL == null || baseURL.isBlank()) {
			// if not available, get the baseurl from the local config file
			PropertyReader propReader = new PropertyReader("local-config.properties");
			baseURL = propReader.getProperty("local.baseurl");
		}

		return baseURL;
	}

	/**
	 * Logs an event to the reporter.
	 * 
	 * @param status  Defines the severity of the event.
	 * @param message Event message to be locked.
	 */
	public void log(Status status, String message) {
		ExtentReporter.getInstance().log(status, message);
		LOGGER.info(message);
	}

	/**
	 * Configuration method which will be executed after each @Test method. Will end
	 * reporting and log the test end to the reporter.
	 */
	@AfterMethod
	public void afterMethod() {

		log(Status.INFO, "Test case completed");
		// End the test case
		ExtentReporter.getInstance().endTestCase();

	}

}
