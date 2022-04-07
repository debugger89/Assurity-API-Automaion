package com.assurity.runtime.reporting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.Status;

/**
 * Listens to the TestnNG test events and acts accordingly. This will be used to
 * identify any errors occur (assertion or any other) during the test execution
 * and report the failures to the reporting engine.
 * 
 * @author cdushmantha
 *
 */
public class TestListener extends TestListenerAdapter {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Will be invoked at each test method failure. Will be used to capture the
	 * failures and log the failures to log files and test report.
	 */
	@Override
	public void onTestFailure(ITestResult iTestResult) {
		Throwable t = iTestResult.getThrowable();

		String errorMessage = "Testcase Failed : " + t.getMessage();
		LOGGER.error(errorMessage, t);
		ExtentReporter.getInstance().log(Status.FAIL, errorMessage, t);
		super.onTestFailure(iTestResult);
	}

}