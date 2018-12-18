package id.co.kynga.app.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DataVariable {
	public static final String CREDENTIAL = "credential";
	public static final String PACKAGE = "id.co.kynga.app";
	public static final String KEY_TITLE = "title";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_CATEGORY = "categoryCode";
	public static final String KEY_CODE = "code";
	public static final String KEY_URL = "playUrl";
	public static final String KEY_DESC = "desc";
	public static final String KEY_EPISODE = "episode";
	public static final String LOCALTV = "localTv";
	public static final String PREMIUMLTV = "premiumTv";
	public static final String GENFLIX = "genflix";
	public static final String MUSIC = "playlist_code";
	public static final String LOCAL = "playlist_code_local";
	public static final String KEN = "playlist_code_ken";
	public static final String SPORTS = "playlist_code_sports";
	public static final String ANIME = "playlist_code_anime";

	public static final String NEWMOVIE = "newmovie";
	public static final String KARAOKE = "karaoke";
	public static final String INFOTAINTMENT = "info_code";
	public static final String LIFESTYLE = "lifestyle_code";
	public static final String RADIO = "localRadio";
	public static final String NEXT_TOKEN = "nextPageToken";
	public static final String PREV_TOKEN = "prevPageToken";
	public static final String KEY_ACTOR = "actor";
	public static final String KEY_YEAR = "year";
	public static final String KEY_LENGTH= "length";
	public static final String KEY_ITEM = "totalItem";
	public static final String PAKET = "paket";
	public static final String ALL_PACKAGES = "all_packages";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String CHANNEL_ID = "channel_id";
	public static final String CHANNELS = "channels";
	public static final String LOCAL_URL = "http://192.168.1.133:8888/new_foxy/api/";
	public static final String MAC_ID = "mac_id";
	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_REGISTER = "register";
	public static final String PHONE_NUMBER = "phone_number";
	public static final String ACTION_CHECK_SESSION = "checksession";
	public static final String ACTION_PURCHASE = "purchase";
	public static final String MAC_ADDRESS = "mac_address";
	public static final String BASE_IP = "http://api.bestv-indo.com/";
	public static final String BASE_URL = BASE_IP + "n3mediaapi/";
	public static final String LOGIN_URL = BASE_IP + "nmedia/login";
	public static final String UPDATE_PATH = BASE_IP + "update/";
	public static final String APK_URL = BASE_IP + "apk/";
	public static final String IMAGE_URL = BASE_IP + "images/";

	public static final String MOVIE_CATEGORIES = "mCategories";
	public static final String TV_CATEGORIES = "tvCategories";
	public static final String FREE_CATEGORIES = "freeCategories";
	public static final String PREMIUM_CATEGORIES = "pCategories";
	public static final String GENFLIX_CATEGORIES = "gCategories";
	public static final String RADIO_CATEGORIES = "rCategories";
	public static final String MIVO_CATEGORIES = "mivocategories";
	public static final String SESSION = "session";
	public static final String SECRET_KEY = "You'llNeverWalkAlone";
	public static final String AVAILABLE_PACKAGES = "package_available";
	public static final String VIVA_CATEGORIES = "viva_categories";
	public static final String INTERNET_CATEGORIES = "internetcategories";
	public static final String CHINESE_CATEGORIES = "chinesecategories";
	public static final String KOREA_CATEGORIES = "koreacategories";
	public static final String INDIAN_CATEGORIES = "indiancategories";
	public static final String BLOCKBUSTER_CATEGORIES = "blockbustercategories";
	public static final String DISCOVERY_CATEGORIES = "discoveryCategories";
	public static final String CATCHUP_CATEGORIES = "CATCHUPCategories";
	public static final String RUSIAN_CATEGORIES = "rusiancategories";
	public static final String HINDIE_CATEGORIES = "hindiecategories";
	public static final String KIDS_CATEGORIES = "kidscategories";
	public static final String K_CATEGORIES = "kcategories";
	public static final String HAPPEN_CATEGORIES = "happencategories";
	public static final String HAPPEN_VOD_CATEGORIES = "happenvodcategories";
	public static final String N3_CATEGORIES = "n3categories";
	public static final String YOUTUBE_LIVE_TV_CATEGORIES = "youtube_live_tv";
	public static final String YOUTUBE_4K = "youtube_four_k";
	public static final String FOUR_K_CATEGORIES = "four_k";
	public static final String PACKAGE_BASIC = "BASIC";
	public static final String PACKAGE_PREMIUM = "PREMIUM";
	public static final String PACKAGE_GENFLIX = "GENFLIX";
	public static final String PACKAGE_INDIAN = "INDIAN";
	public static final String PACKAGE_CHINESE = "CHINESE";
	public static final String PACKAGE_JAPANESE = "JAPANESE";
	public static final String PACKAGE_RUSSIAN = "RUSSIAN";
	public static final String PACKAGE_KOREAN = "KOREAN";
	public static final String PACKAGE_FREE = "Free";
	public static final String INDIAN = "indian";
	public static final String HAPPEN_TV = "happen_tv";
	public static final String TOKEN = "token";

	public static final String YOUTUBE_KEY_ID = "id";
	public static final String YOUTUBE_KEY_TITLE = "title";
	public static final String YOUTUBE_KEY_IMAGE = "hqDefault";
	public static final String YOUTUBE_KEY_AUTHOR = "author";
	public static final String YOUTUBE_PLAYLIST_CODE = "playlist_code";

	public static final String YOUTUBE_KEY_ITEM = "totalItem";
	public static final String TYPE_MP4 = "1919";
	public static final String TYPE_HLS = "2828";
	public static final String VIDEO_TYPE = "video_type";
	public static final String CRID = "crid";
	public static final String CRITTERCISM_APP_ID = "1b2fe9f26eb347e6bfbe8a96ceabedf200555300";

	//public  static final String BILLING_URL = "http://api.duitku.com/userjson.aspx";
	//public  static final String TRANSACTION_URL = "http://api.duitku.com/TransactionJson.aspx";

	public static final String BILLING_URL = "http://202.59.166.3/RBSOttixJsonV2/UserJson.aspx";

	//public  static final String BILLING_URL = "http://billing.ottix.tv/api/UserJson.aspx";
	//public  static final String BILLING_URL = "http://api.ottix.tv/nmedia/";
	public static final String TRANSACTION_URL = "http://202.59.166.3/RBSOttixJsonV2/TransactionJson.aspx";
	//public static final String TOPUP_URL = "http://billing.ottix.tv/Mobile/TopupMobile.aspx";

	public static String DEFAULT_PLAYER_URI =
			"http://bitdash-a.akamaihd.net/content/MI201109210084_1/" +
					"mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd";
	public static String WIDEVINE_GTS_DEFAULT_BASE_URI =
			"http://widevine-proxy.appspot.com/proxy";

	public static String Md5(String hex) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(hex.getBytes());

			byte byteData[] = md.digest();

			//convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {

		}
		return "";
	}

	//Added by Aji
	public static final String TAG = "id.co.kynga.LOG";
	public static final String BALANCE_TEST = "http://api-svr.kynga.co.id/user/bubble/package/8";
    public static final String BUY_TEST = "http://back.kynga.co.id/subscription/buy";

    //Buble Types
    public static final String BTYPE_RADIO          = "1";
    public static final String BTYPE_TV             = "3";
    public static final String BTYPE_VIDEO          = "4";
	public static final String BTYPE_PREFERENCE     = "6";
	public static final String BTYPE_GAME			= "7";
    public static final String BTYPE_YOUTUBE        = "8";

	//Preferences ----------------------------------------------------------------------------------
	public static final String PREF_PIN = "parental_pin";
	public static final String PREF_RATE_SU = "parental_rate_su";
	public static final String PREF_RATE_R = "parental_rate_r";
	public static final String PREF_RATE_BO = "parental_rate_bo";
	public static final String PREF_RATE_D = "parental_rate_d";
	//End of Preferences consts --------------------------------------------------------------------

}