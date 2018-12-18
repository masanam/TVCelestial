package id.co.kynga.app.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.model.PackageModel;
import id.co.kynga.app.model.User;

public class PremiumPreview extends Dialog {
	private static final String ACTION = "purchase";
	Context ctx;
	PreferenceUtil pref;
	String packages;
	GridView gv;
	Button cancel;
	private ImageView premiumIcon;
	private TextView tPackageName, tBalance;
	private LinearLayout lPurcaseOk;
	Button bPurchaseOk;
	private TextView youPurchased;
	ArrayList<Map<String, String>> listPackages = new ArrayList<>();
	NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("id"));

	private PackageAdapter adapter;
	private PremiumListener listener;

	public PremiumPreview(Context context, String p) {
		super(context);
		ctx = context;
		packages = p;

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.premiumtv_layout);
		this.getWindow().setBackgroundDrawableResource(R.color.transparan);
		this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setWindowAnimations(R.style.MyAnimation);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		this.getWindow().setLayout(width, height);
		this.setCancelable(false);
		tPackageName = (TextView) findViewById(R.id.package_name);
		pref = new PreferenceUtil();
		gv = (GridView) findViewById(R.id.gridView1);
		tBalance = (TextView) findViewById(R.id.textBalance);
		User user = User.getInstance();
		tBalance.setText(ctx.getResources().getString(R.string.your_balance) + " : Rp. " + String.valueOf(user.getBalance()));
		lPurcaseOk = (LinearLayout) findViewById(R.id.layoutPurchaseOk);
		bPurchaseOk = (Button) findViewById(R.id.bPurchaseOk);
		youPurchased = (TextView) findViewById(R.id.textPurcaseOk);
		bPurchaseOk.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {
				PremiumPreview.this.dismiss();
				if (listener != null) {
					listener.onPurchaseOk();
				}
			}

		});
		premiumIcon = (ImageView) findViewById(R.id.pIcon);
		switch (packages) {
			case DataVariable.PACKAGE_PREMIUM:
				premiumIcon.setImageResource(R.drawable.bg_premium_portrait);
				tPackageName.setText("PREMIUM PACKAGE");
				break;
			case DataVariable.PACKAGE_CHINESE:
				premiumIcon.setImageResource(R.drawable.bg_chinese_portrait);
				tPackageName.setText("CHINESE PACKAGE");
				break;
			case DataVariable.PACKAGE_KOREAN:
				premiumIcon.setImageResource(R.drawable.bg_korean_portrait);
				tPackageName.setText("KOREAN PACKAGE");
				break;
			case DataVariable.PACKAGE_INDIAN:
				premiumIcon.setImageResource(R.drawable.bg_indian_potrait);
				tPackageName.setText("INDIAN PACKAGE");
				break;

		}
		cancel = (Button) findViewById(R.id.bCancelP);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PremiumPreview.this.dismiss();
				if (listener != null) {
					listener.onDismiss();
				}
			}

		});
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        int position, long id) {
				final Map<String, String> p = listPackages.get(position);
				ConfirmDialog cDialog = new ConfirmDialog(ctx);
				cDialog.setOnPurchaseOkListener(new ConfirmDialog.OnPurchaseOkListener() {

					@Override
					public void onPurchaseOk() {
						purchase(p.get("pName"), p.get("id"));
					}
				});
				cDialog.show();

			}

		});
		gv.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
			                           int position, long id) {
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});
		String allPackage = pref.getPreference(DataVariable.ALL_PACKAGES);
		if (allPackage.length() > 0) {
			displayPackage(allPackage);
		} else {
			getAllPackage();
		}

	}


	private void getAllPackage() {
		final String macid = Utils.wifiEnabled() ? Utils.getMACAddress("wlan0") : Utils.getMACAddress("eth0");
		//phoneNumber + macID + sessionValue + action
		final String phone = pref.getPreference(DataVariable.PHONE_NUMBER);
		final String session = pref.getPreference(DataVariable.SESSION);
		String paramsign = phone + macid + session + "getpackage";
		Log.d("Params", paramsign);
		final String signature = DataVariable.Md5(paramsign + DataVariable.SECRET_KEY);
		StringRequest request = new StringRequest(Request.Method.POST,
				DataVariable.TRANSACTION_URL,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						Log.d("AllPackage", arg0);
						displayPackage(arg0);
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub

			}
		}) {
			@Override
			public HashMap<String, String> getParams() {
				HashMap<String, String> params = new HashMap<String, String>();
				//phoneNumber ={ phoneNumber }& macID={ macID }& sessionValue ={ sessionValue }&  action ={ action} &signature={signature}
				params.put("phoneNumber", phone);
				params.put("macID", macid);
				params.put("sessionValue", session);
				params.put("action", "getpackage");
				params.put("signature", signature);
				return params;
			}

		};
		KyngaApp.getInstance().addToRequestQueue(request);
	}

	private void displayPackage(String p) {
		Log.d("All_PAckages", p);

		try {

			JSONArray jPAck = new JSONArray(p);
			if (jPAck.length() > 0) {
				for (int i = 0; i < jPAck.length(); i++) {
					JSONObject jO = jPAck.getJSONObject(i);
					String name = jO.getString("name");
					if (name.equals(packages)) {
						JSONArray jp = jO.getJSONArray("package");
						if (jp.length() > 0) {
							for (int s = 0; s < jp.length(); s++) {
								JSONObject js = jp.getJSONObject(s);
								HashMap<String, String> m = new HashMap<String, String>();
								m.put("id", js.getString("idPackage"));
								m.put("pName", js.getString("name"));
								m.put("harga", js.getString("harga"));
								m.put("period", js.getString("validity"));
								m.put("category", name);
								listPackages.add(m);

							}
						}
					}

				}
			}

			if (listPackages.size() > 0) {
				adapter = new PackageAdapter();
				gv.setAdapter(adapter);

			}
		} catch (Exception e) {
			Log.e("Parse Radio", "Error:" + e.getMessage());
		}
	}

	public class PackageAdapter extends BaseAdapter {
		private int positionId = -1;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listPackages.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listPackages.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void notifyDataChange(int id) {
			positionId = id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				vi = inflater.inflate(R.layout.package_list, parent, false);
			}
			Map<String, String> p = listPackages.get(position);
			TextView pMonth = (TextView) vi.findViewById(R.id.package_month);
			TextView pPrice = (TextView) vi.findViewById(R.id.package_price);
			pMonth.setText(p.get("pName"));
			pPrice.setText("Rp. " + p.get("harga") + ",-/ ");
			return vi;
		}
	}

	private void purchase(final String name, final String id) {
		final ProgressDialog dialog = new ProgressDialog(ctx);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setMessage("Purchasing...");
		dialog.show();
		final String phone, session, signature;
		phone = pref.getPreference(DataVariable.PHONE_NUMBER);
		session = pref.getPreference(DataVariable.SESSION);
		final String macid = Utils.wifiEnabled() ? Utils.getMACAddress("wlan0") : Utils.getMACAddress("eth0");
		String sign = phone + macid + id + name + session + ACTION;
		signature = DataVariable.Md5(sign + DataVariable.SECRET_KEY);
		StringRequest request = new StringRequest(Request.Method.POST,
				DataVariable.TRANSACTION_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						try {
							JSONObject response = new JSONObject(arg0.trim());
							String code = response.getString("code");
							String msg = response.getString("message");
							if (code.equals("00")) {
								JSONArray pk = response.getJSONArray("package");
								if (pk.length() > 0) {
									ArrayList<PackageModel> listPm = new ArrayList<>();
									for (int i = 0; i < pk.length(); i++) {

										JSONObject packageItem = pk.getJSONObject(i);
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
									User user = User.getInstance();
									user.setPackages(listPm);
								}
								dialog.dismiss();
								lPurcaseOk.setVisibility(View.VISIBLE);
								cancel.setVisibility(View.GONE);
								gv.setVisibility(View.GONE);
								youPurchased.setText(ctx.getResources().getString(R.string.you_have_purchased) + " " + packages + " " + "package");
							} else {
								dialog.dismiss();
								Toast.makeText(ctx, "Package Purchasing failed :" + msg, Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							dialog.dismiss();
							Toast.makeText(ctx, "Package Purchasing failed", Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(ctx, "Package Purchasing failed", Toast.LENGTH_LONG).show();
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", phone);
				params.put("macID", macid);
				params.put("idpackage", id);
				params.put("package", name);
				params.put("sessionValue", session);
				params.put("action", ACTION);
				params.put("signature", signature);
				return params;
			}
		};
		KyngaApp.getInstance().addToRequestQueue(request);
	}

	public interface PremiumListener {
		void onDismiss();

		void onPurchaseOk();
	}

	public void setListener(PremiumListener l) {
		listener = l;
	}
}