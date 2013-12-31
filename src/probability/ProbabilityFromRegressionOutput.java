/**
 * 
 */
package probability;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

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
		TestAgent ta = new TestAgent();

		ProbabilityFromRegressionOutput(ta, "./src/SASRegressionExample.csv",
				4, 122, 1, 5);

	}

	/**
	 * 
	 * @param agent
	 *            when used in an agent-based model, this is the agent object
	 * @param file
	 *            file directory of where parameter estimate table is
	 * @param rowStart
	 *            row where to start looking for parameter variables and beta
	 *            estimates
	 * @param rowEnd
	 * @param colStart
	 *            column where variables are
	 * @param colEnd
	 *            column where to stop reading (where the parameter estimates
	 *            are)
	 * @throws IOException
	 * @author dchen
	 * 
	 */
	public static void ProbabilityFromRegressionOutput(TestAgent ta, String file,
			int rowStart, int rowEnd, int colStart, int colEnd)
			throws IOException {
		/*
		 * List of beta estimates, including b_0, the intercept
		 */
		CSVReader reader = new CSVReader(new FileReader(file));
		Hashtable hashtable = new Hashtable();

		String[] nextLine;
		int i = 0;
		// int start = 5;
		// int end = 15;
		while (((nextLine = reader.readNext()) != null)) {
			// -2 is used so you can just use the line number in the file, it
			// corrects for the csci indexing
			if ((i > (rowStart - 2)) && (i < rowEnd)) {
				// nextLine[] is an array of values from the line

//				String str = Arrays.toString(nextLine);
//				System.out.println(str);
//				System.out.println(nextLine[0]);

				StatsOutput statsOutput = new StatsOutput();
				statsOutput.setParameter(nextLine[0]);
				if (nextLine[1].trim().length() > 0) {
					statsOutput.setP1(Integer.parseInt(nextLine[1]));
				}
				if (nextLine[2].trim().length() > 0) {
					statsOutput.setP2(Integer.parseInt(nextLine[2]));
				}

				statsOutput.setEstimate(Double.parseDouble(nextLine[4]));

				hashtable.put(nextLine[0] + nextLine[1] + nextLine[2],
						statsOutput);

			}
			i++;
		}
		
		Double logitP = 0.0;
		Double value = 0.0;

		if (hashtable.containsKey("Intercept")) {
			value = ((StatsOutput) hashtable.get("Intercept")).getEstimate();
			logitP += value;
			System.out.println("value: " + value);
			System.out.println(logitP);
			
		}
		
		int age = ta.getAgeCat();
		String agestring = "agecat"+Integer.toString(age);
		if (hashtable.containsKey(agestring)) {
			value = ((StatsOutput) hashtable.get(agestring)).getEstimate();
			logitP += value;
			System.out.println("value: " + value);
			System.out.println(logitP);
		}
		
		int race = ta.getRace();
		String racestring = "race4"+Integer.toString(race);
		if (hashtable.containsKey(racestring)) {
			value = ((StatsOutput) hashtable.get(racestring)).getEstimate();
			logitP += value;
			System.out.println("value: " + value);
			System.out.println(logitP);
		}

		String ageracestring = "agecat*race4"+Integer.toString(age)+Integer.toString(race);
		if (hashtable.containsKey(ageracestring)) {
			value = ((StatsOutput) hashtable.get(ageracestring)).getEstimate();
			logitP += value;
			System.out.println("value: " + value);
			System.out.println(logitP);
		}
		double probability = Math.exp(logitP)/(1 + Math.exp(logitP));
		
		System.out.println("logit = " + logitP);
		System.out.println("probability = " + probability);
		
	}
}
