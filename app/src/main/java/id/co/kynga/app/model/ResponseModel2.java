package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseModel2 {
	public enum StatusType {
		FailedStatusType(0),
		SucceededStatusType(200),
		ExpiredStatusType(2);
		private final int id;

		StatusType(final int id) {
			this.id = id;
		}

		public int getValue() {
			return id;
		}
	}

	public StatusType Status;
	public String Message;
	public String Result;

	public ResponseModel2(final String response) {
		try {
			final JSONObject json = new JSONObject(response);
			//final int status = json.getInt("Status");
			final int status = Integer.valueOf(json.getString("status"));
			if (status == StatusType.FailedStatusType.getValue()) {
				Status = StatusType.FailedStatusType;
			} else if (status == StatusType.SucceededStatusType.getValue()) {
				Status = StatusType.SucceededStatusType;
			} else if (status == StatusType.ExpiredStatusType.getValue()) {
				Status = StatusType.ExpiredStatusType;
			}
			//Message = json.getString("Message");
			Message = json.getString("status_message");
			if (json.has("data")) {
				Result = json.getString("data");
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
}