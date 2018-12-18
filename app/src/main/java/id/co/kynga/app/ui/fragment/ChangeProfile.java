package id.co.kynga.app.ui.fragment;

import java.util.HashMap;

import org.json.JSONObject;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.ui.fragment.PopupFragment.OnCancelListener;
import id.co.kynga.app.model.User;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.PreferenceUtil;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ChangeProfile extends DialogFragment {
	private EditText e1, e2, e3;
	private Button b1;
	private PopupFragment rFragment;
	private ImageView loader;
	User user = User.getInstance();
	private PreferenceUtil pref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg0) {
		super.onCreateView(inflater, container, arg0);
		View vi = inflater.inflate(R.layout.change_profile, container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		getDialog().getWindow().setWindowAnimations(R.style.MyAnimation1);
		loader = (ImageView) vi.findViewById(R.id.fLoader);
		pref = new PreferenceUtil();
		e1 = (EditText) vi.findViewById(R.id.old_phone);
		e2 = (EditText) vi.findViewById(R.id.newEmail);
		e3 = (EditText) vi.findViewById(R.id.newAddress);
		b1 = (Button) vi.findViewById(R.id.b_change_profile);
		if (user != null) {
			e1.setText(user.getPhoneNumber());
		}
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String newEmail = e2.getText().toString().trim();
				String newAddress = e3.getText().toString().trim();
				if (newEmail.length() == 0) {
					rFragment = new PopupFragment();
					final Bundle bundle = new Bundle();
					bundle.putString("message", getString(R.string.new_email_should_not_empty));
					rFragment.setArguments(bundle);
					rFragment.show(getChildFragmentManager(), "Change Profile");
					return;
				} else if (newAddress.length() == 0) {
					rFragment = new PopupFragment();
					final Bundle bundle = new Bundle();
					bundle.putString("message", getString(R.string.new_address_should_not_empty));
					rFragment.setArguments(bundle);
					rFragment.show(getChildFragmentManager(), "Change Profile");
					return;
				}
				changeProfile(newEmail, newAddress);
			}

		});
		return vi;

	}

	private void changeProfile(final String params1, final String params2) {
		startAnimation(loader);
		final String session = pref.getPreference(DataVariable.SESSION);
		String paramsString = user.getPhoneNumber() + params1 + params2 + session + "changeProfile";
		final String signature = DataVariable.Md5(paramsString + DataVariable.SECRET_KEY);
		StringRequest cRequest = new StringRequest(Method.POST,
				DataVariable.BILLING_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						try {
							JSONObject result = new JSONObject(arg0);
							String code = result.getString("code");
							String message = result.getString("message");
							if (code.equals("00")) {
								user.setEmail(params1);
								user.setAddress(params2);
								stopAnimation(loader);
								final Bundle bundle = new Bundle();
								bundle.putString("message", message);
								rFragment = new PopupFragment();
								rFragment.setArguments(bundle);
								rFragment.setOnCancelListener(new OnCancelListener() {
									@Override
									public void onCancel() {
										ChangeProfile.this.dismiss();
									}
								});
								rFragment.show(getChildFragmentManager(), "Change Profile");

							} else {
								stopAnimation(loader);
								final Bundle bundle = new Bundle();
								bundle.putString("message", message);
								rFragment = new PopupFragment();
								rFragment.setArguments(bundle);
								rFragment.show(getChildFragmentManager(), "Change Profile");
							}
						} catch (Exception e) {
							stopAnimation(loader);
							final Bundle bundle = new Bundle();
							bundle.putString("message", "Update failed");
							rFragment = new PopupFragment();
							rFragment.setArguments(bundle);
							rFragment.show(getChildFragmentManager(), "Change Profile");
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				stopAnimation(loader);
				final Bundle bundle = new Bundle();
				bundle.putString("message", "Error:" + arg0.getMessage());
				rFragment = new PopupFragment();
				rFragment.setArguments(bundle);
				rFragment.show(getChildFragmentManager(), "Change Profile");
			}
		}) {
			@Override
			public HashMap<String, String> getParams() {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", user.getPhoneNumber());
				params.put("email", params1);
				params.put("address", params2);
				params.put("sessionValue", session);
				params.put("action", "changeProfile");
				params.put("signature", signature);
				return params;
			}
		};
		KyngaApp.getInstance().addToRequestQueue(cRequest);
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
}