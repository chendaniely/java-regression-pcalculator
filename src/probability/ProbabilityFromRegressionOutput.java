/**
 * 
 */
package probability;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author dchen
 *
 */
public class ProbabilityFromRegressionOutput {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		ProbabilityFromRegressionOutput("./src/SASRegressionExample.csv",110,301, 1, 5);

	}
	
	public static void ProbabilityFromRegressionOutput(String file, int rowStart, int rowEnd, int colStart, int colEnd) throws IOException {
		/*
		 * List of beta estimates, including b_0, the intercept
		 */
		CSVReader reader = new CSVReader(new FileReader(file));
		String[] nextLine;
		int i = 0;
//		int start = 5;
//		int end = 15;
		while (((nextLine = reader.readNext()) != null) ) {
			// -2 is used so you can just use the line number in the file, it corrects for the csci indexing
			if ( (i > (rowStart-2)) && (i < rowEnd) ){
				// nextLine[] is an array of values from the line
				
				String str = Arrays.toString(nextLine);
				System.out.println(str);
//				nextLine.split(",");
//				System.out.println(nextLine.length);
//				System.out.println(nextLine.split(","));
				
			}
			i++;
		}

	}
}
