package id.co.kynga.app.ui.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.model.PreferenceEntity;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.PreferenceUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

public class RadioCategories {
	
	Context ctx;
	PreferenceUtil pref;
	PreferenceEntity entity;
	
	public RadioCategories(Context context){
		ctx = context;
		pref = new PreferenceUtil();
		entity = new PreferenceEntity(ctx);
		entity.open();
	}
	
	@SuppressLint("NewApi")
	public ArrayList<HashMap<String, String>> getData(){
		String token = pref.getPreference("token");
		ArrayList<HashMap<String, String>> movieData = new ArrayList<HashMap<String, String>>();
		 InputStream is = null;
		 
		 String result = "";
		 String pref_movie = entity.getPreference(DataVariable.RADIO_CATEGORIES);//pref.getPreference(DataVariable.RADIO_CATEGORIES);
		 if(pref_movie.length() == 0 || pref_movie.equals("")){
		 try{
	       	  
			 try {
			        StrictMode.ThreadPolicy policy = new StrictMode.
			          ThreadPolicy.Builder().permitAll().build();
			        StrictMode.setThreadPolicy(policy);
			        //URL url = new URL("http://www.xlnonton.com/api_client/getAndroidcategoryChannel.php");
			        URL url = new URL(DataVariable.BASE_URL + "getCategoryRadio?token=" + token);
			        HttpURLConnection con = (HttpURLConnection) url
			          .openConnection();
			        con.setConnectTimeout(60*1000);
			       is = con.getInputStream();
			    } catch (Exception e) {
			        e.printStackTrace();
			        //Toast.makeText(ctx,"Connection error, please check your internet connection",Toast.LENGTH_LONG).show();
		             
			    }

	     }catch(Exception e){
	             Log.d("http_error", "Error in http connection "+e.toString());
	             
	     }
		 
	
		 try{
             BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
             StringBuilder sb = new StringBuilder();
             String line = null;
             while ((line = reader.readLine()) != null) {
                     sb.append(line + "\n");
             }
             is.close();
             result=sb.toString();
             //pref.setPreference(DataVariable.RADIO_CATEGORIES, result);
             //Log.d(DataVariable.TV_CATEGORIES,result);
		     }catch(Exception e){
		             Log.d("buffer", "Error converting result "+e.toString());
		             //Toast.makeText(ctx.getApplicationContext(),"Error parsing data",Toast.LENGTH_LONG).show();
		             //return null;
		     }
		 }else{
			 result = pref_movie;
			 //Log.d("movie",pref_movie);
		 }
		 try{
				//if(result !=""){
	                 JSONArray jArray = new JSONArray(result);
	                 
	                 //if(jArray.length() > 0){
	                 for(int i=0;i<jArray.length();i++){
	                	 HashMap<String, String> map = new HashMap<String, String>();
	                         JSONObject json_data = jArray.getJSONObject(i);
	                         
	                         map.put("id",json_data.getString("id"));
	 	         			map.put("name", json_data.getString("name"));
	                       //movieData.add(map);
	                       movieData.add(map);
	                      //Log.d("data",map.toString());
	                 } 
	                 
	                // } 
				//}
	                
	         }catch(JSONException e){
	                 Log.d("parsing_data", "Error parsing data "+e.toString());
	         }
		 
	       return movieData;  
	}
		
}
