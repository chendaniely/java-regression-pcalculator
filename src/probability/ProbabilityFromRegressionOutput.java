/**
 * 
 */
package probability;

import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import au.com.bytecode.opencsv.CSVReader;

/**
 * 
 * @param agent
 *            object: when used in an agent-based model, this is the agent object
 * @param variables
 *            array: variables used in the stats output/program to be read from the csv file
 * @param file
 *            string: file directory of where parameter estimate table is
 * @param rowStart
 *            int: row where to start looking for parameter variables and beta estimates
 * @param rowEnd
 *            int: row where to stop looking for parameter variables and beta estimates
 * @param colStart
 *            int: column where variables are
 * @param colEnd
 *            int: column where to stop reading (where the parameter estimates are)
 * @author dchen chendaniely
 * 
 */
public class ProbabilityFromRegressionOutput {

	/**
	 * 
	 * @param agent
	 *            object: when used in an agent-based model, this is the agent object
	 * @param variables
	 *            array: variables to be read from the csv file
	 * @param alternativeName
	 *            string: file directory of where alternative variable name csv is
	 * @param file
	 *            string: file directory of where parameter estimate table is
	 * @param rowStart
	 *            int: row where to start looking for parameter variables and beta estimates
	 * @param rowEnd
	 *            int: row where to stop looking for parameter variables and beta estimates
	 * @param colStart
	 *            int: column where variables are
	 * @param colEnd
	 *            int: column where to stop reading (where the parameter estimates are)
	 * @throws IOException
	 * 
	 */
	public ProbabilityFromRegressionOutput(Agent agent,
			String[] variables, String alternativeName, String file,
			int rowStart, int rowEnd, int colStart, int colEnd)
			throws IOException {
		
//		hardCodedLookups(ta, variables, alternativeName, file, rowStart, rowEnd, colStart, colEnd);
		genericCodedLookups(agent, variables, alternativeName, file, rowStart, rowEnd, colStart, colEnd);
		
	}
	/**
	 * 
	 * @param agent
	 * @param variables
	 * @param alternativeName
	 * @param file
	 * @param rowStart
	 * @param rowEnd
	 * @param colStart
	 * @param colEnd
	 * @throws IOException
	 */
	private static void genericCodedLookups (Agent agent,
			String[] variables, String alternativeName, String file,
			int rowStart, int rowEnd, int colStart, int colEnd)
			throws IOException {
		
		/*
		 * List of beta estimates, including b_0 (the intercept)
		 */
		CSVReader reader = new CSVReader(new FileReader(file));
		Hashtable hashtable = new Hashtable();

		String[] nextLine;
		int i = 0;
		// int start = 5;
		// int end = 15;
		// assumes first 3 rows are: the variable, categorical status 1, cat status 2
		// last row is the column of beta estimates
		// puts data into hashtable
		while ((nextLine = reader.readNext()) != null) {
			// -2 is used so you can just use the line number in the file corrects for the csci indexing and first col variable names
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
				
				// key: nextLine[0] + nextLine[1] + nextLine[2]
				// value: statsOutput
				hashtable.put(nextLine[0] + nextLine[1] + nextLine[2], statsOutput);

			}
			i++;
		}
		
		Double logitP = 0.0;
		Double value = 0.0;
		
		System.out.println("@@@@@ BEGIN GENERIC LOOKUPS @@@@@");
		
		CSVReader variableSynonymCSV = new CSVReader(new FileReader(alternativeName), ',', '\"', 1);
		Hashtable variableSynonym = new Hashtable();
		
		String[] nextVariableLine;

		i = 0;
		// -2 is used so you can just use the line number in the file, it corrects for the csci indexing
		
//		System.out.println("read in variable synonym csv");
//		
//		System.out.println("skipped first line");
		while ((nextVariableLine = variableSynonymCSV.readNext()) != null) {

			
			VariableAlternativeNames variableAlternativeNames = new VariableAlternativeNames();
			
			for (int j = 0; j < nextVariableLine.length; j++){
//				System.out.print(nextVariableLine[j] + " ");
				if (j == 0){
					variableAlternativeNames.setValue(nextVariableLine[0].trim());
					continue;
				}
				else {
					variableAlternativeNames.possibleValues.add(nextVariableLine[j].trim());
					variableSynonym.put(nextVariableLine[j].trim(), nextVariableLine[0].trim());
				}
			}
//			System.out.println("");
			i++;
		}
		
		Boolean isInterceptUsed = true;
		for (String statsVariable : variables) {
			
			// intercept
			if (isInterceptUsed == true) {
				isInterceptUsed = false;
				value = ((StatsOutput) hashtable.get("Intercept")).getEstimate();
				logitP += value;
				System.out.println("Intercept value: " + value);
				System.out.println("current logitP value = " + logitP);
//				System.out.println("~~~~~ ");

			}

			// interaction
			// interaction is checked first otherwise the following line used
			// for categorical variable will throw an error
			// String agentVariable = variableSynonym.get(statsVariable).toString().trim();
			System.out.println("~~~~~ Variable Encountered: " + statsVariable);
			if (statsVariable.contains("*")){
				System.out.println("~~~~~ in the interaction variable if statment ~~~~~");
				String[] interaction = statsVariable.split("\\*");

				System.out.println(statsVariable + " is an interaction between "
						+ interaction[0] + " and " + interaction[1]);
				// example key: agecat*race422
				
				// convert variable in stats output into variable used for agent
				String agentVariable1 = variableSynonym.get(interaction[0]).toString().trim();
				String agentVariable2 = variableSynonym.get(interaction[1]).toString().trim();
				System.out.println("interaction variable conversion: " + agentVariable1 + " " + agentVariable2);
				
				int categoricalInt1 = ((Agent) agent).getIntValue(agentVariable1);
				int categoricalInt2 = ((Agent) agent).getIntValue(agentVariable2);
				
				String interactionString = interaction[0]+"*"+interaction[1]+Integer.toString(categoricalInt1)+Integer.toString(categoricalInt2);
				System.out.println("catints:" + categoricalInt1 + categoricalInt2 + " " + 
						"interactionString:" + interactionString + " " + 
						"agentVariables:" + agentVariable1 + agentVariable2 + " " +
						"statsVariables:" + interaction[0] + interaction[1]);
				
				if (hashtable.containsKey(interactionString)) {
					System.out.println("!!!!! in the interaction variable if statement !!!!!");
					value = ((StatsOutput) hashtable.get(interactionString)).getEstimate();
					System.out.println("hashtable key: " + value);
					
					
					System.out.println("old logitP value = " + logitP);
					logitP += value;
					System.out.println("value: " + value);
					System.out.println("new logitP value = " + logitP);
				}
				
			} else {
				
				// categorical variable
				// convert variable in stats output into variable used for agent
				String agentVariable = variableSynonym.get(statsVariable).toString().trim();
				int categoricalInt = ((Agent) agent).getIntValue(agentVariable);
				String categoricalString = statsVariable+Integer.toString(categoricalInt);
				

				System.out.println("catint:" + categoricalInt + " " + "catstring:"
						+ categoricalString + " " + "agentVariable:"
						+ agentVariable + " " + "statsVariable:" + statsVariable);
				
				
				if (hashtable.containsKey(categoricalString)) {
//					System.out.println("~~~~~ in the cat variable if statement ~~~~~");
					value = ((StatsOutput) hashtable.get(categoricalString)).getEstimate();
					System.out.println("hashtable key: " + value);
					
					logitP += value;
					System.out.println("value: " + value);
					System.out.println("current logitP value = " + logitP);
				}				
			}
		}
		
		double probability = Math.exp(logitP)/(1 + Math.exp(logitP));
		System.out.println("FINAL CALCULATIONS:");
		System.out.println("logit = " + logitP);
		System.out.println("probability = " + probability);
	}
	
	/**
	 * 
	 * @param ta
	 * @param variables
	 * @param alternativeName
	 * @param file
	 * @param rowStart
	 * @param rowEnd
	 * @param colStart
	 * @param colEnd
	 * @throws IOException
	 * @deprecated Replaced by {@link #genericCodedLookups()
	 */
	@Deprecated
	private static void hardCodedLookups (Agent ta,
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
		// assumes first 3 rows are: the variable, categorical status 1, cat status 2
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
			System.out.println(value);
			logitP += value;
			System.out.println("value: " + value);
			System.out.println(logitP);
			
		}
		
		int age = ta.getAgeCat();
		String agestring = "agecat"+Integer.toString(age);
		System.out.println(agestring);
		if (hashtable.containsKey(agestring)) {
			value = ((StatsOutput) hashtable.get(agestring)).getEstimate();
			System.out.println(value);
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
