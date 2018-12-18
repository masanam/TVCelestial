package id.co.kynga.app.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;

/**
 * @author Aji Subastian
 */

@SuppressWarnings("unused, WeakerAccess")
public class RestError {

    private static RestError instance;
    private String err;

    public static RestError newInstance(VolleyError volleyError) {
        if (instance == null) {
            instance = new RestError(volleyError);
        }

        return instance;
    }

    private RestError(VolleyError volleyError) {
        try {
            err = new String(volleyError.networkResponse.data, "utf-8");
            Log.e(DataVariable.TAG, err);
        } catch (UnsupportedEncodingException e) {
            Log.e(DataVariable.TAG, e.getMessage());
        }
    }

    /**
     * Get error message
     *
     * @return String
     */
    public String getMessage() {
        return err;
    }

    /**
     * Show error string on Toast
     *
     * @param context Context
     */
    public void showMessage(Context context) {
        showMessage(context, Toast.LENGTH_SHORT);
    }

    /**
     * Show error string on Toast
     *
     * @param context Context
     * @param duration int
     *
     */
    public void showMessage(Context context, int duration) {
        Toast.makeText(context, err, duration).show();
    }

}
