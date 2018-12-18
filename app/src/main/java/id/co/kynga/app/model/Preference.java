package id.co.kynga.app.model;

public class Preference {
	private String NAME = "";
	private String VALUE = "";

	public void setName(String name) {
		NAME = name;
	}

	public String getName() {
		return NAME;
	}

	public void setValue(String value) {
		VALUE = value;
	}

	public String getValue() {
		return VALUE;
	}
}