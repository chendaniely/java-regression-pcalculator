/**
 * 
 */
package probability;
import java.io.IOException;

/**
 * Class to test ProbabilityFromRegressionOutput
 * 
 * @author dchen
 * 
 */
public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// print current working directory
		System.out.println(System.getProperty("user.dir"));
		Agent agent = new Agent();

		// want to take this list and also convert it to the variables used for
		// the agent class
		// variables used in the stats output
		String[] variables = { "agecat", "race4", "male", "inccat4", "educat3", "agecat*race4" };

		ProbabilityFromRegressionOutput p = new ProbabilityFromRegressionOutput(agent, variables,
				"./src/alternativeVariableNames.csv",
				"./src/SASRegressionExample.csv", 4, 122, 1, 5);
		
		System.out.println(p.getProbabilityFromBetas());

	}

}
