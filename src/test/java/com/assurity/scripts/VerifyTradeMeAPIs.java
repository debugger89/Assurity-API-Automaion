package com.assurity.scripts;

import static org.hamcrest.Matchers.equalTo;

import java.io.File;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.assurity.runtime.runner.ExecutionHelper;
import com.assurity.runtime.utils.CSVTestDataParser;
import com.aventstack.extentreports.Status;

import io.restassured.response.Response;

/**
 * Test class. This will contain the test methods
 * 
 * @author cdushmantha
 *
 */
public class VerifyTradeMeAPIs extends ExecutionHelper {

	@Test(dataProvider = "dp_testTradeMeDetailsAPI")
	public void testTradeMeDetailsAPI(String param_catalogue, String response_name, String response_canrelist,
			String response_promotion_name, String response_promotion_description) {

		log(Status.INFO, "Test case started..");

		// set the base path of the request
		request.basePath("/Categories/6327/Details.json");

		// set the query parameter and send the request.
		Response response = request.given().queryParam("catalogue", param_catalogue).get();

		// log the response details for reporting purposes
		log(Status.PASS, "Response code : " + response.statusCode());
		log(Status.PASS, response.getBody().asPrettyString());

		// JSON path for the promotion accepting Name as a parameter.
		String promotionsJsonPath = "Promotions.find{ prom -> prom.Name == '%s' }.Description";

		// Do the assertions
		// 1. check of the Name is correct
		// 2. Check if the CanRelist is correct
		// 3. Check if the Description is correct for selected Promotion
		response.then().assertThat().and().body("Name", equalTo(response_name)).and()
				.body("CanRelist", equalTo(Boolean.parseBoolean(response_canrelist))).and()
				.body(String.format(promotionsJsonPath, response_promotion_name),
						equalTo(response_promotion_description));

	}

	/**
	 * Data provider method for the testTradeMeDetailsAPI test. Reads a CSV file and
	 * converts the test data to a 2d object array for TestNG to use
	 * 
	 * @return 2d object array representing the CSV file content as required by
	 *         TestNG
	 */
	@DataProvider(name = "dp_testTradeMeDetailsAPI")
	public Object[][] dpTestTradeMeDetailsAPI() {

		CSVTestDataParser parser = new CSVTestDataParser();
		// Read and convert the specific CSV file for the test
		return parser.readCSVDataFromFile("data" + File.separator + "TestTradeMeDetailsAPI.csv");
	}

}
