package id.co.kynga.app.ui.fragment;

import java.util.HashMap;

import org.json.JSONObject;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.ui.fragment.PopupFragment.OnCancelListener;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.Utils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ForgotPassword extends DialogFragment {
	private String TAG = "forgotpassworddialog";
	ImageView loader;
	private Button bForgot;
	EditText ePhone;
	PopupFragment rf;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arg0) {

		super.onCreateView(inflater, container, arg0);
		View vi = inflater.inflate(R.layout.forgot_password, container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		getDialog().getWindow().setWindowAnimations(R.style.MyAnimation);
		loader = (ImageView) vi.findViewById(R.id.fLoader);
		//pref = new PreferenceUtil(getActivity());
		bForgot = (Button) vi.findViewById(R.id.b_forgot);
		ePhone = (EditText) vi.findViewById(R.id.edit_phone);

		bForgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ph = ePhone.getText().toString().trim();
				requestPassword(ph);
			}

		});
		//requestPassword();
		return vi;
	}

	private void requestPassword(final String phone) {
		startAnimation(loader);
		final String macId = Utils.wifiEnabled() ? Utils.getMACAddress("wlan0") : Utils.getMACAddress("eth0");
		String action = "forgotpassword";
		String paramsSign = phone + macId + action;
		final String signature = DataVariable.Md5(paramsSign + DataVariable.SECRET_KEY);
		StringRequest changeRequest = new StringRequest(Method.POST,
				DataVariable.BILLING_URL,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						Log.d("Forgot Password", arg0);
						try {
							JSONObject result = new JSONObject(arg0);
							String code = result.getString("code");
							String message = result.getString("message");
							if (code.equals("00")) {

								stopAnimation(loader);
								final Bundle bundle = new Bundle();
								bundle.putString("message", "Request Password success");
								rf = new PopupFragment();
								rf.setArguments(bundle);
								rf.setOnCancelListener(new OnCancelListener() {

									@Override
									public void onCancel() {
										// TODO Auto-generated method stub
										ForgotPassword.this.dismiss();
									}

								});
								rf.show(getChildFragmentManager(), "Request Password");
							} else {
								stopAnimation(loader);
								final Bundle bundle = new Bundle();
								bundle.putString("message", message);
								rf = new PopupFragment();
								rf.setArguments(bundle);
								rf.show(getChildFragmentManager(), "Reset Password");

							}
						} catch (Exception e) {
							stopAnimation(loader);
							final Bundle bundle = new Bundle();
							bundle.putString("message", "Reset Failed");
							rf = new PopupFragment();
							rf.setArguments(bundle);
							rf.show(getChildFragmentManager(), "Reset Password");

						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				stopAnimation(loader);
				final Bundle bundle = new Bundle();
				bundle.putString("message", "Reset Failed");
				rf = new PopupFragment();
				rf.setArguments(bundle);
				rf.show(getChildFragmentManager(), "Reset Password");
			}
		}) {
			@Override
			public HashMap<String, String> getParams() {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", phone);
				params.put("macID", macId);
				params.put("action", "forgotpassword");
				params.put("signature", signature);

				return params;
			}
		};
		KyngaApp.getInstance().addToRequestQueue(changeRequest, TAG);

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

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		KyngaApp.getInstance().cancelPendingRequests(TAG);
	}
}