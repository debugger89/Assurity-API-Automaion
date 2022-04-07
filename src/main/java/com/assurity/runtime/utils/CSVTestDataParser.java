package com.assurity.runtime.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.assurity.runtime.exceptions.ConfigurationException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

/**
 * This utility class will read a CSV file from the disk and convert the content
 * in to a 2d object array format. Which will be used in TestNg data providers.
 * 
 * @author cdushmantha
 *
 */
public class CSVTestDataParser {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Read the given CSV file from the disk and convert the data inside in to a 2D
	 * object array.
	 * 
	 * @param relativeFilePath The CSV file path to read
	 * @return Converted data in 2D object array
	 */
	public Object[][] readCSVDataFromFile(String relativeFilePath) {

		try {
			File csvFile = new File(relativeFilePath);

			// read the CSV file and get the converted data
			List<String[]> rowlist = readCSVGetRawData(csvFile);

			// create the 2D object array
			String[][] array2D = new String[rowlist.size()][];
			// convert the data List to an Array
			array2D = rowlist.toArray(array2D);

			return array2D;

		} catch (Exception e) {
			// if error occurred, report the error and throw a ConfigurationException
			e.printStackTrace();
			LOGGER.error("Error occured while reading test data from : " + relativeFilePath, e);
			throw new ConfigurationException("Error occured while reading test data from : " + relativeFilePath, e);
		}

	}

	/**
	 * Read the provided CSV file using OpenCSV and get the data values as a list of
	 * Strings.
	 * 
	 * @param csvFile CSV file object
	 * @return The CSV file content as a list of arrays.
	 * @throws IOException
	 * @throws CsvException
	 */
	private List<String[]> readCSVGetRawData(File csvFile) throws IOException, CsvException {

		Reader reader = null;
		CSVReader csvReader = null;
		try {
			reader = new FileReader(csvFile);
			// read the file ignoring the first row. The first row is assumed to contain the
			// titles.
			csvReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(buildParser()).build();

			List<String[]> rowlist = csvReader.readAll();
			return rowlist;
		} finally {

			// Finally close the file readers to avoid resource consumption.
			if (reader != null) {
				reader.close();
			}
			if (csvReader != null) {
				reader.close();
			}
		}

	}

	/**
	 * Builds the CSV parser with given criteria.
	 * 
	 * @return Created CSVParser instance
	 */
	private CSVParser buildParser() {
		return new CSVParserBuilder().withSeparator(',').build();
	}

}
