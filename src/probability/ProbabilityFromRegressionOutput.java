/**
 * 
 */
package probability;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;

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
		
		// want to take this list and also convert it to the variables used for the agent class
		String[] variables = {"agecat", "race4", "agecat*race4"};

		ProbabilityFromRegressionOutput(ta, variables, "./src/alternativeVariableNames.csv","./src/SASRegressionExample.csv",
				4, 122, 1, 5);

	}

	/**
	 * 
	 * @param agent
	 *            when used in an agent-based model, this is the agent object
	 * @param variables
	 *            variables to be read from the csv file
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
	public static void ProbabilityFromRegressionOutput(TestAgent ta,
			String[] variables, String alternativeName, String file,
			int rowStart, int rowEnd, int colStart, int colEnd)
			throws IOException {
		
//		hardCodedLookups(ta, variables, alternativeName, file, rowStart, rowEnd, colStart, colEnd);
		genericCodedLookups(ta, variables, alternativeName, file, rowStart, rowEnd, colStart, colEnd);
		
	}
	
	public static void genericCodedLookups (TestAgent ta,
			String[] variables, String alternativeName, String file,
			int rowStart, int rowEnd, int colStart, int colEnd)
			throws IOException {
		
		double logitP = 0.0;
		String value = "";
		System.out.println("@@@@@ BEGIN GENERIC LOOKUPS @@@@@");
		
		CSVReader variableSynonymCSV = new CSVReader(new FileReader(alternativeName));
		Hashtable variableSynonym = new Hashtable();
		
		String[] nextVariableLine;

		int i = 0;
		// -2 is used so you can just use the line number in the file, it corrects for the csci indexing
		
		System.out.println("read in variable synonym csv");
		
		Boolean isFirstLine = true;
		
		while ((nextVariableLine = variableSynonymCSV.readNext()) != null) {
			if (isFirstLine == true){
				System.out.println("skipping first line");
				isFirstLine = false;
				continue;
			}
			
			VariableAlternativeNames variableAlternativeNames = new VariableAlternativeNames();
			
			for (int j = 0; j < nextVariableLine.length; j++){
				System.out.print(nextVariableLine[j] + " ");
				if (j == 0){
					variableAlternativeNames.setValue(nextVariableLine[0].trim());
					continue;
				}
				else {
					variableAlternativeNames.possibleValues.add(nextVariableLine[j].trim());
					variableSynonym.put(nextVariableLine[j].trim(), nextVariableLine[0].trim());
				}
			}
			System.out.println("");
			i++;
		}
		
//		if (variableSynonym.containsKey("race4")){
//			value = ((VariableAlternativeNames) variableSynonym.get("race4").getValue();
//			
//		}

		System.out.println("@@@@@ END GENERIC LOOKUPS @@@@@");
		System.out.println("\n ---------- \n");
	}
	
	public static void hardCodedLookups (TestAgent ta,
			String[] variables, String alternativeName, String file,
			int rowStart, int rowEnd, int colStart, int colEnd)
			throws IOException {
		
		System.out.println("@@@@@ BEGIN HARD CODED LOOKUPS @@@@@");
		/*
		 * List of beta estimates, including b_0 (the intercept)
		 */
		CSVReader reader = new CSVReader(new FileReader(file));
		Hashtable hashtable = new Hashtable();

		String[] nextLine;
		int i = 0;
		// int start = 5;
		// int end = 15;
		// assumes first 3 rows are the variable, categorical status 1, cat status 2
		// last row is the column of beta estimates
		// puts data into hashtable
		while ((nextLine = reader.readNext()) != null) {
			// -2 is used so you can just use the line number in the file
			// corrects for the csci indexing and first col variable names
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
		
		System.out.println("@@@@@ END HARD CODED LOOKUPS @@@@@");
		
		System.out.println("\n ---------- \n");
	}
}
