package id.co.kynga.app.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.model.UserModel;
import id.co.kynga.app.util.Utils;


/**
 * @author Aji Subastian
 */
public class PrefProfileFragment extends Fragment {

    //default constants values if any of information in this screen is empty
    private static final String DEFAULT_MAC_ADDRESS = "00:00:00:00:00:00";
    private static final String DEFAULT_GENDER      = "N/A";
    private static final String DEFAULT_BIRTHDATE   = "00-00-0000";
    private static final String DEFAULT_PHONE       = "N/A";
    private static final String DEFAULT_ADDRESS     = "N/A";

    public PrefProfileFragment() {
        // Required empty public constructor
    }

    public static PrefProfileFragment newInstance() {
        return new PrefProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pref_profile, container, false);
        initializeViews(v);

        return v;
    }

    private void initializeViews(View view) {
        UserModel userModel = SessionController.getUser();

        String tname = userModel.Fullname;
        tname = (TextUtils.isEmpty(tname)) ? "" : tname.toLowerCase();

        String email = userModel.Email;
        email = (TextUtils.isEmpty(email)) ? "" : email.toLowerCase();

        String gender = "";
        gender = (TextUtils.isEmpty(gender)) ? DEFAULT_GENDER : gender.toLowerCase();

        String birthdate = "";
        birthdate = (TextUtils.isEmpty(birthdate)) ? DEFAULT_BIRTHDATE : birthdate.toLowerCase();

        String phone = userModel.PhoneNumber;
        phone = (TextUtils.isEmpty(phone)) ? DEFAULT_PHONE : phone.toLowerCase();

        String address = "";
        address = (TextUtils.isEmpty(address)) ? DEFAULT_ADDRESS : address.toLowerCase();

        //Set any information into TextViews
        ((TextView) view.findViewById(R.id.tName)).setText(tname);
        ((TextView) view.findViewById(R.id.tEmail)).setText(email);
        ((TextView) view.findViewById(R.id.tGender)).setText(gender);
        ((TextView) view.findViewById(R.id.tBirthdate)).setText(birthdate);
        ((TextView) view.findViewById(R.id.tPhoneNumber)).setText(phone);
        ((TextView) view.findViewById(R.id.tAddress)).setText(address);

        //Mac Address
        String macAddress = (Utils.getMACAddress("wlan0"));
        macAddress = (TextUtils.isEmpty(macAddress)) ? DEFAULT_MAC_ADDRESS : macAddress;
        ((TextView) view.findViewById(R.id.tMacId)).setText(macAddress);

        //QR Code
        String qrCode = Utils.getMACAddress("eth0");
        if (!TextUtils.isEmpty(qrCode)) {
            ImageView iBarcode = (ImageView) view.findViewById(R.id.iBarcode);
            try {
                showQRCode(qrCode, iBarcode);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    private void showQRCode(final String data, ImageView imageView) throws WriterException {
        com.google.zxing.Writer writer = new QRCodeWriter();
        String finaldata = Uri.encode(data, "utf-8");
        BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE, 150, 150);
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < 150; i++) {
            for (int j = 0; j < 150; j++) {
                bitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }

        imageView.setImageBitmap(bitmap);
    }

}
