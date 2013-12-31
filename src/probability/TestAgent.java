package probability;

public class TestAgent {

	private int age = 24;
	private int ageCat = 1;
	private int gender = 1;
	private int race = 3;
	private int marital = 1;
	private int income = 3;
	private int edu = 2;

	public int getValue(String parameter) {

		switch (parameter) {
		case "age":
			return getAge();
		case "ageCat":
			return getAgeCat();
		case "gender":
			return getGender();
		case "race":
			return getRace();
		case "marital":
			return getMarital();
		case "education":
			return getEdu();
		case "income":
			return getIncome();

		}
		return 0;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAgeCat() {
		return ageCat;
	}

	public void setAgeCat(int ageCat) {
		this.ageCat = ageCat;
	}

	public int getRace() {
		return race;
	}

	public void setRace(int race) {
		this.race = race;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getMarital() {
		return marital;
	}

	public void setMarital(int marital) {
		this.marital = marital;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getEdu() {
		return edu;
	}

	public void setEdu(int edu) {
		this.edu = edu;
	}

}