package id.co.kynga.app.general.controller.url;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;

import static id.co.kynga.app.KyngaApp.context;


public class URLController {

	public static String getURL(final int resource_id) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server));
	}

	public static String getURL(final int resource_id, final String query) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server), query);
	}

	public static String getURL2(final int resource_id) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server2));
	}

	public static String getURL2(final int resource_id, final String query) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server2), query);
	}

	public static String getURL3(final int resource_id) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server_app_mstar));
	}

	public static String getURL3(final int resource_id, final String query) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server_app_mstar), query);
	}

	public static String getURL4(final int resource_id) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server_app_mstar4));
	}

	public static String getURL4(final int resource_id, final String query) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server_app_mstar4), query);
	}

	public static String getURLTest(final int resource_id) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server_test));
	}

	public static String getURLTest(final int resource_id, final String query) {
		return String.format(GlobalController.getString(resource_id), GlobalController.getString(R.string.api_server_test), query);
	}

	public static Map<String, String> getHeaders() {
		final Map<String, String> map = new HashMap<>();
		if (SessionController.isSession()) {
			map.put("Token", SessionController.getToken());
		}
		return map;
	}

	public static Map<String, String> getHeadersAppMStar() {
		final Map<String, String> map = new HashMap<>();
		if (SessionControllerAppMStar.isSession()) {
			map.put("Token", SessionControllerAppMStar.getToken());
		}
		return map;
	}

	public static void checkVersion(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_checkVersion);
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void checkVersionAppMStar(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_checkVersion);
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}


	public static void doPurchasePackageByTopup(
			final String phone_number,
			final String packageId,
			final String priceId,
			final String price,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL2(R.string.url_buy_product);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		parameters.put("username", phone_number);
		parameters.put("productID", packageId);
		parameters.put("priceID", priceId);
		parameters.put("price", price);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
/*
		GlobalController.showToast(
				"url : "+url+"\n"+
				"username : "+phone_number+"\n"+
						"productID : "+packageId+"\n"+
						"priceID : "+priceId+"\n"+
						"price : "+price
				, 20000);
*/
	}

	public static void paymentEWallet(
			final String phone_number, 
			final String transfer_bank_name,
			final String cc_amount,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL2(R.string.url_payment_ewallet);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		parameters.put("username", phone_number);
		parameters.put("operator", transfer_bank_name);
		parameters.put("cc_amount", cc_amount);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
/*
		GlobalController.showToast("url : "+url+"\n"+"username : "+phone_number+"\n"+
						"operator : "+transfer_bank_name+"\n"+
				"cc_amount : "+cc_amount
				, 20000);
*/
	}


	public static void payment(
			final String phone_number, final String cc_number, final String cc_month, final String cc_year,
			final String cc_cvv, final String cc_amount,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL2(R.string.url_payment);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		parameters.put("username", phone_number);
		parameters.put("cc_number", cc_number);
		parameters.put("cc_month", cc_month);
		parameters.put("cc_year", cc_year);
		parameters.put("cc_cvv", cc_cvv);
		parameters.put("cc_amount", cc_amount);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void payment2(
			final String phone_number, final String cc_amount,
			final String transfer_bank_name,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL2(R.string.url_payment2);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		parameters.put("username", phone_number);
		parameters.put("cc_amount", cc_amount);
		parameters.put("transfer_bank_name", transfer_bank_name);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		/*
		GlobalController.showToast("url : "+url+"\n"+"username : "+phone_number+"\n"+
				"cc_amount : "+cc_amount+"\n"+
				"transfer_bank_name : "+transfer_bank_name, 20000);
		*/
	}

	public static void payment3(
			final String phone_number, final String cc_amount,
			//final String transfer_bank_name,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL2(R.string.url_payment3);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		parameters.put("username", phone_number);
		parameters.put("cc_amount", cc_amount);
		//parameters.put("transfer_bank_name", transfer_bank_name);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		/*
		GlobalController.showToast("url : "+url+"\n"+"username : "+phone_number+"\n"+
				"cc_amount : "+cc_amount+"\n", 20000);
		*/
	}

	public static void paymentSnap(
			final String phone_number, final String cc_number, final String cc_month, final String cc_year,
			final String cc_cvv, final String cc_amount,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL2(R.string.url_payment_snap);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		//parameters.put("order_id", "500");
		parameters.put("gross_amount", "100000");

		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void paymentSnap2(
			final String phone_number, final String cc_number, final String cc_month, final String cc_year,
			final String cc_cvv, final String cc_amount,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL2(R.string.url_payment_snap2);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		//parameters.put("order_id", "500");
		parameters.put("token", "59343cc0-d14d-4f40-b396-fa8d1f16471f");

		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void balance(
			final String phone_number,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_balance);
		//final String url = URLController.getURL(R.string.url_balance);
		final String url = URLController.getURL2(R.string.url_balance);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		parameters.put("username", phone_number);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
/*
		GlobalController.showToast("url : "+url +"\n"+
				"username : "+ phone_number, 20000);
*/
	}

	public static void signoutAppMStar(
			final String phone_number,
			final URLManager.URLCallback url_callback) {
		//phone_number = phone_number.substring(1);

		final String url = URLController.getURL3(R.string.url_logout);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("PhoneNumber", phone_number);
		URLManager.requestPostAsync(url, parameters, URLController.getHeadersAppMStar(), url_callback);
		//GlobalController.showToast(phone_number, 20000);
	}

	public static void login(
			final String phone_number,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_login);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("PhoneNumber", phone_number);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		//GlobalController.showToast(phone_number, 20000);
	}

	public static void login2(
			final String IMEI,
			final String phone_number,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_login);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("IMEI", IMEI);
		parameters.put("PhoneNumber", phone_number);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		//GlobalController.showToast(phone_number +" : " +IMEI, 20000);
	}

	public static void login2AppMStar(
			final String IMEI,
			final String phone_number,
			final URLManager.URLCallback url_callback) {

		final String url = URLController.getURL3(R.string.url_login2);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("IMEI", IMEI);
		parameters.put("PhoneNumber", phone_number);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);

		//GlobalController.showToast(phone_number +" : " +IMEI+ " "+url, 20000);
	}

	public static void checkLogin(
			final String IMEI,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_checkLogin);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("IMEI", IMEI);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		//GlobalController.showToast(phone_number, 20000);
	}

	public static void checkLoginAppMStar(
			final String IMEI,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_checkLogin_app_mstar);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("IMEI", IMEI);
		URLManager.requestPostAsync(url, parameters, URLController.getHeadersAppMStar(), url_callback);
		//GlobalController.showToast("url: "+url +"\n"+"IMEI : "+ IMEI, 20000);
	}

	public static void checkRegister(
			final String IMEI,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_checkRegister);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("IMEI", IMEI);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		//GlobalController.showToast(url +"\n"+"IMEI : "+ IMEI, 20000);

	}

	public static void verify(
			final String phone_number,
			final String token,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_verify);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("PhoneNumber", phone_number);
		parameters.put("Token", token);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void verifyAppMStar(
			final String phone_number,
			final String token,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_verify);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("PhoneNumber", phone_number);
		parameters.put("Token", token);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void register(
			final String phone_number,
			final String fullname,
			final String email,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL2(R.string.url_register);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("PhoneNumber", phone_number);
		parameters.put("Fullname", fullname);
		parameters.put("Email", email);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void registerAppMStar(
			final String phone_number,
			final String fullname,
			final String email,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL3(R.string.url_register);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("PhoneNumber", phone_number);
		parameters.put("Fullname", fullname);
		parameters.put("Email", email);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void register2(
			final String fullname,
			final String gender,
			final String dob,
			final String phoneNumber,
			final String email,
			final String address,
				  final String macAddress,
				  final String imei,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL(R.string.url_register2);
		//Log.d("APPLOG", "url -- " + url);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("Fullname", fullname);
		parameters.put("Gender", gender);
		parameters.put("DOB", dob);
		parameters.put("PhoneNumber", phoneNumber);
		parameters.put("Email", email);
		parameters.put("Address", address);
		parameters.put("MacAddress", macAddress);
		parameters.put("Imei", imei);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
/*
		GlobalController.showToast(url +"\n"+"Fullname "+ fullname+"\n"+"Gender "+ gender+"\n"+"DOB "+ dob  +
				"\n"+"PhoneNumber "+ phoneNumber+"\n"+"Email "+ email+"\n"+"Address "+address+"\n"+"MacAddress "+macAddress+"\n"+
				"Imei "+ imei, 20000);
*/

	}

	public static void registerNew(
			final String fullname,
			final String gender,
			final String dob,
			final String phoneNumber,
			final String email,
			final String address,
			final String macAddress,
			final String imei,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL(R.string.url_registerNew);
		//Log.d("APPLOG", "url -- " + url);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("Fullname", fullname);
		parameters.put("Gender", gender);
		parameters.put("DOB", dob);
		parameters.put("PhoneNumber", phoneNumber);
		parameters.put("Email", email);
		parameters.put("Address", address);
		parameters.put("MacAddress", macAddress);
		parameters.put("Imei", imei);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
/*
		GlobalController.showToast(url +"\n"+"Fullname "+ fullname+"\n"+"Gender "+ gender+"\n"+"DOB "+ dob  +
				"\n"+"PhoneNumber "+ phoneNumber+"\n"+"Email "+ email+"\n"+"Address "+address+"\n"+"MacAddress "+macAddress+"\n"+
				"Imei "+ imei, 20000);
		*/

	}

	public static void registerCC(
			final String phone_number,
			final String fullname,
			final String number,
			final String expired_month,
			final String expired_year,
			final String cwcv,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL(R.string.url_registerCC)+"/"+ phone_number;
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("fullname", fullname);
		parameters.put("number", number);
		parameters.put("expired_month", expired_month);
		parameters.put("expired_year", expired_year);
		parameters.put("cwcv", cwcv);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void accountCC(final String phone_number, final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_accountCC)+"/"+phone_number;
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void editAccountCC(
			final String phone_number,
			final String fullname,
			final String number,
			final String expired_month,
			final String expired_year,
			final String cwcv,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL(R.string.url_editAccountCC)+"/"+ phone_number;
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("fullname", fullname);
		parameters.put("number", number);
		parameters.put("expired_month", expired_month);
		parameters.put("expired_year", expired_year);
		parameters.put("cwcv", cwcv);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void checkVA(
			final String phone_number,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL2(R.string.url_payment_status);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("username", phone_number);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		//GlobalController.showToast("url : "+url+"\n"+"username : "+phone_number, 20000);
	}

	public static void registerTAGG(
			final String phone_number,
			final String fullname,
			final String email,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL2(R.string.url_register);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("PhoneNumber", phone_number);
		parameters.put("Fullname", fullname);
		parameters.put("Email", email);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		//GlobalController.showToast("url : "+url, 20000);
	}

	public static void meAppMStar(final String phone_number, final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_me)+"/"+phone_number;
		//GlobalController.showToast(url, 20000);
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void whoMe(final String phone_number, final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_me)+"/"+phone_number;
		//GlobalController.showToast(url, 20000);
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void activePackage(final String phone_number, final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_activePackage)+"/"+phone_number;
		//GlobalController.showToast(url, 20000);
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}


	public static void inactivePackage(
			final String Token,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_inactivePackage);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("Token", Token);
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
	}

	public static void faqAppMStar(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_faq);
		URLManager.requestGetAsync(url, null, url_callback);
	}

	public static void tc(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_tc);
		URLManager.requestGetAsync(url, null, url_callback);
	}

	public static void banner(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_banner);
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void bannerAppMStar(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_banner);
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void bannerpromo(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_banner_promo);
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void meAppMStar(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_me);
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void bannerpromoAppMStar(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_banner_promo);
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void category(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_category);
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void categoryAppMStar(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_category);
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void checkSubsctiptionList(
			final String phone_number,
			//final String transfer_bank_name,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL4(R.string.url_subscription_list);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("username", phone_number);
		URLManager.requestPostAsync(url, parameters, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void checkProduct(
			//final String phone_number,
			//final String videoId,
			//final String transfer_bank_name,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL4(R.string.url_check_product);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		//parameters.put("username", phone_number);
		//parameters.put("videoID", videoId);
		parameters.put("not_used", "not_used");
		URLManager.requestPostAsync(url, parameters, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void buyProduct(
			final String phone_number,
			final String videoID,
			final String productID,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL4(R.string.url_buy_product);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		parameters.put("username", phone_number);
		parameters.put("videoID", videoID);
		parameters.put("productID", productID);
		URLManager.requestPostAsync(url, parameters, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void checkSubsctiptionStatusAppMStar(
			final String phone_number,
			final String videoID,
			//final String transfer_bank_name,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURLTest(R.string.url_payment);
		//final String url = URLController.getURL(R.string.url_payment);
		final String url = URLController.getURL4(R.string.url_subscription_status);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		//parameters.put("CreditCardNumber", cc_number);
		parameters.put("username", phone_number);
		parameters.put("videoID", videoID);
		//parameters.put("transfer_bank_name", transfer_bank_name);
		URLManager.requestPostAsync(url, parameters, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void videoGroup(
			final long video_group_id,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_video_group, String.valueOf(video_group_id));
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void videoGroupAppMStar(
			final long video_group_id,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_video_group, String.valueOf(video_group_id));
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void videoCategoryAppMStar(
			final long video_category_id,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_video_category, String.valueOf(video_category_id));
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void videoCategory(
			final long video_category_id,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_video_category, String.valueOf(video_category_id));
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void tvCategory(
			final long tv_category_id,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_tv_category, String.valueOf(tv_category_id));
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void tvCategoryAppMStar(
			final long tv_category_id,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_tv_category, String.valueOf(tv_category_id));
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
		//GlobalController.showToast(url, 20000);
	}

	public static void youtubeChannelAppMStar(
			final String youtube_channel_id,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_youtube_channel, youtube_channel_id);
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void checkGameAppMStar(
			final String phone_number,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_register);
		final String url = URLController.getURL4(R.string.url_game_category2);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("username", phone_number);
		//URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		URLManager.requestPostAsync(url, parameters, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void radioCategoryAppMStar(
			final long radio_category_id,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL3(R.string.url_radio_category, String.valueOf(radio_category_id));
		URLManager.requestGetAsync(url, URLController.getHeadersAppMStar(), url_callback);
	}

	public static void packageData(final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_package_data);
		final String url = URLController.getURL2(R.string.url_package_data);
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telephonyManager.getSubscriberId();
		//GlobalController.showToast(imsi.substring(0,5), 20000);
		final Map<String, String> parameters = new HashMap<>();

		if(imsi.substring(0,5).contains("51089")) {
			parameters.put("name", "Three");
		}else if(imsi.substring(0,5).contains("51000")) {
			parameters.put("name", "PSN");
		}else if(imsi.substring(0,5).contains("51008")) {
			parameters.put("name", "AXIS");
		}else if(imsi.substring(0,5).contains("51028")) {
			parameters.put("name", "Smartfren");
		}else if(imsi.substring(0,5).contains("51021")) {
			parameters.put("name", "Indosat");
		}else if(imsi.substring(0,5).contains("51009")) {
			parameters.put("name", "Smartfren");
		}else if(imsi.substring(0,5).contains("51011")) {
			parameters.put("name", "XL");
		}else if(imsi.substring(0,5).contains("51001")) {
			parameters.put("name", "Indosat");
		}else if(imsi.substring(0,5).contains("51010")) {
			parameters.put("name", "Telkomsel");
		}else if(imsi.substring(0,5).contains("51088")) {
			parameters.put("name", "BOLT");
		}

		//parameters.put("name", "XL");
		URLManager.requestPostAsync(url, parameters, URLController.getHeaders(), url_callback);
		//URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

/*
	public static void registerVA(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_register_va);
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}

	public static void topupData(
			final PackageDataModel package_data_model,
			final String username,
			final String phone_number,
			final URLManager.URLCallback url_callback) {
		//final String url = URLController.getURL(R.string.url_topup_data);
		final String url = URLController.getURL2(R.string.url_topup_data);
		//GlobalController.showToast(url, 20000);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("id", String.valueOf(package_data_model.ID));
		parameters.put("mobile_operator_id", String.valueOf(package_data_model.MobileOperatorID));
		parameters.put("name", String.valueOf(package_data_model.Name));
		parameters.put("package_id", String.valueOf(package_data_model.PackageID));
		parameters.put("package_price", String.valueOf(package_data_model.Price));
		parameters.put("msisdn", phone_number);
		parameters.put("username", username);
		URLManager.requestPostAsync(url,  parameters, URLController.getHeaders(), url_callback);
	}
	*/
/*
	public static void payment(final URLManager.URLCallback url_callback) {
		final String url = URLController.getURLTest(R.string.url_payment);
		URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
	}
*/
	/*
	public static void topupData(
			final PackageDataModel package_data_model,
			final String phone_number,
			final URLManager.URLCallback url_callback) {
		final String url = URLController.getURL(R.string.url_topup_data);
		final Map<String, String> parameters = new HashMap<>();
		parameters.put("PackageDataID", String.valueOf(package_data_model.ID));
		parameters.put("PriceID", String.valueOf(package_data_model.package_data_price_model.ID));
		parameters.put("PhoneNumber", phone_number);
		URLManager.requestPostAsync(url, URLController.getHeaders(), parameters, url_callback);
	}
	*/
}