package probability;

import java.util.ArrayList;

/**
 * Stores the values read in from alternative variable name CSV File
 * @author dchen
 *
 */
public class VariableAlternativeNames {
	
	private String value = "";
	ArrayList<String> possibleValues = new ArrayList<String>();
	
	public VariableAlternativeNames(){
		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
