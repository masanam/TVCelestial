package id.co.kynga.app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.model.pojo.ParentalPojo;

/**
 * @author Aji Subastian
 */
public class PrefParentalFragment extends Fragment implements View.OnClickListener {

    private static final String PREF_PIN    = "kynga.PREF_PIN";
    private static final String RATE_RULE   = "kynga.RATE_RULE";

    private static ParentalClickListener parentalClickListener;
    private boolean hasPin = false;
    private ParentalPojo pojo;

    private CheckBox checkRateSu, checkRateR, checkRateBo, checkRateD;
    private int[] buttonResArray = new int[]{
            R.drawable.btn_create, R.drawable.btn_change
    };

    public PrefParentalFragment() {
        // Required empty public constructor
    }

    public static PrefParentalFragment newInstance(String pin, ParentalPojo parentalPojo) {

        PrefParentalFragment fragment = new PrefParentalFragment();
        Bundle b = new Bundle();
        b.putString(PREF_PIN, pin);
        b.putParcelable(RATE_RULE, parentalPojo);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ParentalClickListener) {
            parentalClickListener = (ParentalClickListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null) {
            hasPin = (!TextUtils.isEmpty(b.getString(PREF_PIN)));
            pojo = b.getParcelable(RATE_RULE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pref_parental, container, false);
        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {

        //Title
        String title = "Parental Control PIN: ";
        title += (hasPin) ? "created" : "not created";

        ((TextView) view.findViewById(R.id.title_parental_check)).setText(title);

        //Checkboxes
        checkRateSu = (CheckBox) view.findViewById(R.id.check_rate_su);
        checkRateR = (CheckBox) view.findViewById(R.id.check_rate_r);
        checkRateBo = (CheckBox) view.findViewById(R.id.check_rate_bo);
        checkRateD = (CheckBox) view.findViewById(R.id.check_rate_d);

        checkRateSu.setChecked(pojo.isSu());
        checkRateR.setChecked(pojo.isR());
        checkRateBo.setChecked(pojo.isBo());
        checkRateD.setChecked(pojo.isD());

        //Setup click listener
        checkRateSu.setOnClickListener(this);
        checkRateR.setOnClickListener(this);
        checkRateBo.setOnClickListener(this);
        checkRateD.setOnClickListener(this);

        int buttonRes = (!hasPin) ? buttonResArray[0] : buttonResArray[1];
        Button buttonCreateOrChange = (Button) view.findViewById(R.id.b_create);
        buttonCreateOrChange.setBackgroundResource(buttonRes);
        buttonCreateOrChange.setOnClickListener(this);

        //view.findViewById(R.id.b_create).setOnClickListener(this); //button create or change
        view.findViewById(R.id.b_submit).setOnClickListener(this); //button submit
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_rate_su:
                pojo.setSu(checkRateSu.isChecked());
                break;
            case R.id.check_rate_r:
                pojo.setR(checkRateR.isChecked());
                break;
            case R.id.check_rate_bo:
                pojo.setBo(checkRateBo.isChecked());
                break;
            case R.id.check_rate_d:
                pojo.setD(checkRateD.isChecked());
                break;
            case R.id.b_create:
                if (parentalClickListener != null) {
                    parentalClickListener.onConfigurePin();
                }
                break;
            case R.id.b_submit:
                if (parentalClickListener != null) {
                    parentalClickListener.onRuleSubmit(pojo);
                }
                break;
            default:
                break;
        }
    }

    public interface ParentalClickListener {
        void onConfigurePin();
        void onRuleSubmit(ParentalPojo parentalPojo);
    }

}
