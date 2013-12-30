package probability;

public class StatsOutput {
	
	private String parameter = "";
	private int p1 = -1;
	private int p2 = -1;
	private double estimate = 0.0;

	public StatsOutput() {
	}
	
	// ############################################# getter setter

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public int getP1() {
		return p1;
	}

	public void setP1(int p1) {
		this.p1 = p1;
	}

	public int getP2() {
		return p2;
	}

	public void setP2(int p2) {
		
		this.p2 = p2;
	}

	public double getEstimate() {
		return estimate;
	}

	public void setEstimate(double estimate) {
		this.estimate = estimate;
	}
	
	

}
