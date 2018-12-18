package id.co.kynga.app.model;

import java.io.Serializable;

public class PackageModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ID = "";
	private String NAME = "";
	private String REMAINING_DAY = "";
	private String PERIOD = "";
	private int PRICE = 0;
	private String CATEGORY;


	public void setId(String id) {
		this.ID = id;
	}

	public String getId() {
		return ID;
	}

	public void setName(String name) {
		this.NAME = name;
	}

	public String getName() {
		return NAME;
	}

	public void setPeriod(String period) {
		this.PERIOD = period;
	}

	public void setRemainingDay(String day) {
		REMAINING_DAY = day;
	}

	public String getRemainingDay() {
		return REMAINING_DAY;
	}

	public String getPeriod() {
		return PERIOD;
	}

	public void setCategory(String cat) {
		this.CATEGORY = cat;
	}

	public String getCategory() {
		return CATEGORY;
	}

	public void setPrice(int p) {
		this.PRICE = p;
	}

	public int getPrice() {
		return PRICE;
	}
}