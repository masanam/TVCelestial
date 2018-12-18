package id.co.kynga.app.model;

public enum TypeModel {
	NoneType(0),
	WebType(1),
	VideoType(2),
	TVType(3),
	YoutubeType(4),
	YoutubeChannelType(5),
	RadioType(6),
	VAVType(7);
	private final int id;

	TypeModel(final int id) {
		this.id = id;
	}

	public int getValue() {
		return id;
	}
}