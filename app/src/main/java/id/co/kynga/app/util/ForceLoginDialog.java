package id.co.kynga.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import id.co.kynga.app.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.ui.fragment.PopupFragment;
import id.co.kynga.app.model.PackageModel;
import id.co.kynga.app.model.User;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ForceLoginDialog extends DialogFragment {
	private ImageView loading;
	private PreferenceUtil pref;

	private Button blogin, bClose;
	private CheckBox show_pass;
	private LoginListener listener;

	public ForceLoginDialog() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View vi = inflater.inflate(R.layout.login, container, false);
		getDialog().getWindow().setWindowAnimations(R.style.MyAnimation);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		pref = new PreferenceUtil();
		final EditText edituser = (EditText) vi.findViewById(R.id.edit_username);
		final EditText editpassword = (EditText) vi.findViewById(R.id.edit_password);
		edituser.setText(pref.getPreference(DataVariable.PHONE_NUMBER));
		editpassword.setText(pref.getPreference(DataVariable.PASSWORD));
		loading = (ImageView) vi.findViewById(R.id.imageLoading);
		bClose = (Button) vi.findViewById(R.id.buttonClose);
		show_pass = (CheckBox) vi.findViewById(R.id.checkPassword);
		show_pass.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					editpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				} else {
					editpassword.setInputType(129);
				}
			}
		});
		blogin = (Button) vi.findViewById(R.id.b_login);
		blogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int validation = 0;
				String validationMessage = "";
				if (edituser.getText().toString().equals("")) {
					validation += 1;

				}
				if (editpassword.getText().toString().equals("")) {
					validation += 2;
				}
				switch (validation) {
					case 1:
						validationMessage = KyngaApp.getContext().getResources().getString(R.string.phone_should_not_empty);
						break;

					case 2:
						validationMessage = KyngaApp.getContext().getResources().getString(R.string.pass_should_not_empty);
						break;
					case 3:
						validationMessage = KyngaApp.getContext().getResources().getString(R.string.phone_pass_should_not_empty);
						break;
				}

				if (validationMessage != "") {
					final Bundle bundle = new Bundle();
					bundle.putString("message", validationMessage);
					PopupFragment rf = new PopupFragment();
					rf.setArguments(bundle);
					rf.show(getChildFragmentManager(), "Login Failed");

				}
				pref.setPreference(DataVariable.PHONE_NUMBER, edituser.getText().toString());
				pref.setPreference(DataVariable.PASSWORD, editpassword.getText().toString());
				//pref.setPreference("serial_number", editserial.getText().toString());
				getCredential();
			}

		});

		bClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onClose();

				}
				ForceLoginDialog.this.dismiss();
			}

		});

		this.setCancelable(false);
		return vi;
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			int width = ViewGroup.LayoutParams.MATCH_PARENT;
			int height = ViewGroup.LayoutParams.MATCH_PARENT;
			dialog.getWindow().setLayout(width, height);
		}
	}

	public void getCredential() {
		startAnimation(loading);
		clearData();
		final String u, p, signature, m;
		u = pref.getPreference(DataVariable.PHONE_NUMBER);
		p = DataVariable.Md5(pref.getPreference(DataVariable.PASSWORD));
		m = Utils.wifiEnabled() ? Utils.getMACAddress("wlan0") : Utils.getMACAddress("eth0");
		String paramsignature = u + m + p + "login";
		signature = DataVariable.Md5(paramsignature + DataVariable.SECRET_KEY);
		StringRequest request = new StringRequest(Request.Method.POST,
				DataVariable.BILLING_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						Log.d("Login", arg0);
						try {
							JSONObject response = new JSONObject(arg0);
							String code = response.getString("code");
							String session = response.getString("sessionValue");
							//Profile profile = Profile.getInstance();

							if (code.equals("00")) {
								/*SET USER DATA*/
								User user = User.getInstance();
								user.setBalance(response.getString("userbalance"));
								user.setName(response.getString("name"));
								user.setEmail(response.getString("email"));
								user.setPhoneNumber(u);
								user.setSession(session);
								user.setPassword(pref.getPreference(DataVariable.PASSWORD));
								user.setAddress(response.getString("address"));
								user.setGender(response.getString("gender"));
								String[] bd = response.getString("birthdate").split(" ");
								user.setBirthdate(bd[0]);
								JSONArray packages = response.getJSONArray("package");
								if (packages.length() > 0) {
									ArrayList<PackageModel> listPm = new ArrayList<PackageModel>();
									for (int i = 0; i < packages.length(); i++) {

										JSONObject packageItem = packages.getJSONObject(i);
										JSONObject valObj = packageItem.getJSONObject("value");
										String pName = packageItem.getString("name");
										String pResult = valObj.getString("result");
										String valid = valObj.getString("validDate");
										PackageModel pm = new PackageModel();
										pm.setCategory(pName);
										pm.setName(pName);
										pm.setPeriod(valid);
										pm.setRemainingDay(pResult);
										listPm.add(pm);
										pref.setPreference(DataVariable.PAKET, response.getString("package"));
									}
									user.setPackages(listPm);
								}
								pref.setPreferenceBoolean(DataVariable.ACTION_LOGIN, true);
								pref.setPreference(DataVariable.SESSION, session);
								if (listener != null) {
									listener.onLogged();

								}
								ForceLoginDialog.this.dismiss();
							} else {
								stopAnimation(loading);
								final Bundle bundle = new Bundle();
								bundle.putString("message", response.getString("message"));
								PopupFragment rf = new PopupFragment();
								rf.setArguments(bundle);
								rf.show(getChildFragmentManager(), "Login Failed");

							}
						} catch (Exception e) {
							stopAnimation(loading);
							final Bundle bundle = new Bundle();
							bundle.putString("message", e.getMessage());
							PopupFragment rf = new PopupFragment();
							rf.setArguments(bundle);
							rf.show(getChildFragmentManager(), "Login Failed");
						}
					}
				},
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						if (!Utils.isNetworkAvailable()) {
							stopAnimation(loading);
							final Bundle bundle = new Bundle();
							bundle.putString("message", "No Internet Connection");
							PopupFragment rf = new PopupFragment();
							rf.setArguments(bundle);
							rf.show(getChildFragmentManager(), "Login Failed");

						} else {
							stopAnimation(loading);
							final Bundle bundle = new Bundle();
							bundle.putString("message", "Login Failed");
							PopupFragment rf = new PopupFragment();
							rf.setArguments(bundle);
							rf.show(getChildFragmentManager(), "Login Failed");

						}


					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();

				params.put("phoneNumber", u);
				params.put("password", p);
				params.put("macID", m);
				params.put("action", "login");
				params.put("signature", signature);
				return params;
			}
		};
		KyngaApp.getInstance().addToRequestQueue(request);
		//requestqueue.add(request);
	}

	private void clearData() {
		pref.setPreference(DataVariable.PAKET, "");
		pref.setPreferenceBoolean(DataVariable.ACTION_LOGIN, false);
		//Profile p = Profile.getInstance();
		//p.clearProfile();
	}


	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();

	}


	private void startAnimation(ImageView i) {
		i.setVisibility(View.VISIBLE);
		AnimationSet anim = new AnimationSet(true);
		RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(1200);
		rotate.setInterpolator(new DecelerateInterpolator());
		rotate.setRepeatCount(RotateAnimation.INFINITE);
		anim.addAnimation(rotate);
		i.startAnimation(anim);
	}

	private void stopAnimation(ImageView imgLoader) {
		imgLoader.clearAnimation();
		imgLoader.setVisibility(View.GONE);
	}

	public interface LoginListener {
		void onClose();

		void onLogged();
	}

	public void setListener(LoginListener l) {
		listener = l;
	}
}