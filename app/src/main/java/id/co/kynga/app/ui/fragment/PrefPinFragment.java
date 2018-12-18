package id.co.kynga.app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.kynga.app.R;
import id.co.kynga.app.model.pojo.ParentalPojo;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.PreferenceUtil;

/**
 * @author Aji Subastian
 */
public class PrefPinFragment extends Fragment implements View.OnClickListener {

    private static final String TAG             = PrefPinFragment.class.getSimpleName();

    public static final int PIN_MODE_CREATE     = 100;
    public static final int PIN_MODE_CHANGE     = 200;
    public static final int PIN_MODE_VERIFY     = 300;
    public static final int PIN_MODE_SET_RULE   = 400;

    private static final String PIN_MODE        = "kynga.PIN_MODE";
    private static final String RULE_OBJ        = "kynga.RULE_OBJ";

    private static PrefPinListener listener;

    private int mode;
    private ParentalPojo pojo;
    private PreferenceUtil pref;

    private String inputPin = "";
    private int inputLen = 0;
    private List<String> pinList = new ArrayList<>();
    private int[] dotResource = new int[2];
    private ImageView[] imageViews = new ImageView[4];
    private TextView screenTitle;

    public PrefPinFragment() {
        // Required empty public constructor
    }

    public static PrefPinFragment newInstance(int mode) {
        return newInstance(mode, null);
    }

    public static PrefPinFragment newInstance(int mode, ParentalPojo pojo) {
        PrefPinFragment fragment = new PrefPinFragment();
        Bundle b = new Bundle();
        b.putInt(PIN_MODE, mode);
        b.putParcelable(RULE_OBJ, pojo);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PrefPinListener) {
            listener = (PrefPinListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null) {
            mode = b.getInt(PIN_MODE);
            pojo = b.getParcelable(RULE_OBJ);
        }

        pref = new PreferenceUtil();

        //drawable resource Ids
        dotResource[0] = R.drawable.dot_pin_off;
        dotResource[1] = R.drawable.dot_pin_on;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pref_pin, container, false);
        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {

        screenTitle = (TextView) view.findViewById(R.id.title_pin_input);
        screenTitle.setText(getScreenTitle());

        //dot imageviews
        imageViews[0] = (ImageView) view.findViewById(R.id.dot_pin_1);
        imageViews[1] = (ImageView) view.findViewById(R.id.dot_pin_2);
        imageViews[2] = (ImageView) view.findViewById(R.id.dot_pin_3);
        imageViews[3] = (ImageView) view.findViewById(R.id.dot_pin_4);

        view.findViewById(R.id.btn_pin_0).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_1).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_2).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_3).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_4).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_5).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_6).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_7).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_8).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_9).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_back).setOnClickListener(this);
        view.findViewById(R.id.btn_pin_next).setOnClickListener(this);

    }

    private String getScreenTitle() {
        int pinListSize = pinList.size();
        switch (mode) {
            case PIN_MODE_CREATE:
                return (pinListSize == 1) ? "Confirm Your PIN" : "Create Parental Control PIN";
            case PIN_MODE_CHANGE:
                return (pinListSize == 2) ? "Confirm Your Pin" : ((pinListSize == 1) ? "Enter New PIN" : "Input Your Pin");
            case PIN_MODE_VERIFY:
            case PIN_MODE_SET_RULE:
                return "Input Your Pin";
            default:
                return "Create Parental Control PIN";

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pin_0: addInputPin("0"); break;
            case R.id.btn_pin_1: addInputPin("1"); break;
            case R.id.btn_pin_2: addInputPin("2"); break;
            case R.id.btn_pin_3: addInputPin("3"); break;
            case R.id.btn_pin_4: addInputPin("4"); break;
            case R.id.btn_pin_5: addInputPin("5"); break;
            case R.id.btn_pin_6: addInputPin("6"); break;
            case R.id.btn_pin_7: addInputPin("7"); break;
            case R.id.btn_pin_8: addInputPin("8"); break;
            case R.id.btn_pin_9: addInputPin("9"); break;
            case R.id.btn_pin_back: removeInputPin(); break;
            case R.id.btn_pin_next: processPin(); break;
            default:
                break;
        }
    }

    private void clearDotStates() {
        for(ImageView imageView : imageViews) {
            imageView.setImageResource(dotResource[0]);
        }
    }

    private void changeDotStates() {
        clearDotStates();
        if (inputLen > 0) {
            for (int i = 0; i < inputLen; i++) {
                imageViews[i].setImageResource(dotResource[1]);
            }
        }
    }

    private void resetInput() {
        inputPin = "";
        inputLen = 0;
        changeDotStates();
        screenTitle.setText(getScreenTitle()); //update screen title text
    }

    private void addInputPin(String s) {
        if (inputLen >= 0 && inputLen < 4) {
            inputLen++;
            inputPin += s;
        }

        changeDotStates();
    }

    private void removeInputPin() {
        if (inputLen > 0) {
            inputLen--;
            inputPin = inputPin.substring(0, inputLen);
        }

        changeDotStates();
    }

    private void processPin() {
        //we want to make sure that user has input 4 digits of PIN characters
        if (inputLen == 4) {
            switch (mode) {
                case PIN_MODE_SET_RULE: changeParentalRule(pojo); break;
                case PIN_MODE_CREATE: createPin(); break;
                case PIN_MODE_CHANGE: changePin(); break;
                default:
                    Log.e(TAG, "Selected mode is not valid");
                    break;
            }
        }
        else {
            //else do not process click action on next button
            Toast.makeText(getContext(), "PIN must be 4 digits", Toast.LENGTH_SHORT).show();
        }

    }

    private void createPin() {
        pinList.add(inputPin);
        if (pinList.size() == 2) {
            boolean isSuccess = false;
            if (pinList.get(0).equals(pinList.get(1))) {
                pref.setPreferenceString(DataVariable.PREF_PIN, pinList.get(0));
                isSuccess = true;
            }

            pinList.clear();
            String msg = (isSuccess) ? "PIN has been created" : "PIN do not match";
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

            if (isSuccess && listener != null) listener.onPostPinProcessed();
        }

        resetInput();
    }

    private void changePin() {
        pinList.add(inputPin);
        String msg = "";
        if (pinList.size() >= 1) {
            boolean validPin = (pinList.get(0).equals(pref.getPreference(DataVariable.PREF_PIN)));
            if (!validPin) {
                msg = "Invalid PIN";
                pinList.clear();
            }
            else {
                if (pinList.size() == 3) {
                    boolean isSuccess = false;
                    if (pinList.get(1).equals(pinList.get(2))) {
                        pref.setPreferenceString(DataVariable.PREF_PIN, pinList.get(1));
                        isSuccess = true;
                    }

                    msg = (isSuccess) ? "PIN has been changed" : "PIN do not match";
                    if (isSuccess && listener != null) listener.onPostPinProcessed();
                }
            }

            if (!TextUtils.isEmpty(msg)) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }

        resetInput();
    }

    private void changeParentalRule(ParentalPojo pojo) {
        String msg = "PIN must be created first";
        String sPin = pref.getPreference(DataVariable.PREF_PIN);
        if (!TextUtils.isEmpty(sPin)) {
            boolean isSuccess = false;
            if (sPin.equals(inputPin) && pojo != null) {
                pref.setPreferenceBoolean(DataVariable.PREF_RATE_SU, pojo.isSu());
                pref.setPreferenceBoolean(DataVariable.PREF_RATE_R, pojo.isR());
                pref.setPreferenceBoolean(DataVariable.PREF_RATE_BO, pojo.isBo());
                pref.setPreferenceBoolean(DataVariable.PREF_RATE_D, pojo.isD());

                isSuccess = true;
            }

            msg = (isSuccess) ? "Locked content has been set" : "Invalid PIN";
        }

        resetInput();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

        if (listener != null) listener.onPostPinProcessed();
    }

    public interface PrefPinListener {
        void onPostPinProcessed();
    }

}
