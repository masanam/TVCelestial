package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.model.VAModel;

public class VAstatusBCA_Permata extends Activity {

    private ListView mListView1_1, mListView1_2, mListView1_3, mListView1_4,mListView1_5,mListView1_6,mListView1_7;
    private ListView mListView1_8, mListView1_9, mListView1_10, mListView1_11,mListView1_12,mListView1_13,mListView1_14;
    private ListView mListView1_15, mListView1_16;

    private static ArrayList<String> daftarPemesanList = new ArrayList<String>();
    private static ArrayList<String> daftarNoHpList = new ArrayList<String>();
    private static ArrayList<String> daftarNamaKlinikList = new ArrayList<String>();
    private static ArrayList<String> daftarObat1List = new ArrayList<String>();
    private static ArrayList<String> daftarObat2List = new ArrayList<String>();
    private static ArrayList<String> daftarObat3List = new ArrayList<String>();
    /*
    private static ArrayList<String> daftarObat4List = new ArrayList<String>();
    private static ArrayList<String> daftarObat5List = new ArrayList<String>();
    private static ArrayList<String> daftarObat6List = new ArrayList<String>();
    private static ArrayList<String> daftarObat7List = new ArrayList<String>();
    private static ArrayList<String> daftarObat8List = new ArrayList<String>();
    private static ArrayList<String> daftarObat9List = new ArrayList<String>();
    private static ArrayList<String> daftarObat10List = new ArrayList<String>();
    private static ArrayList<String> daftarStatusList = new ArrayList<String>();
    private static ArrayList<String> daftarTanggalWaktuList = new ArrayList<String>();
    private static ArrayList<String> daftarIdList = new ArrayList<String>();
*/
    private VAModel va_model;
    //private BannerModel banner_model;
    public static VAstatusBCA_Permata instance;
    private ImageButton btn_back;

 /*
    private static ArrayList<String> daftarBelanjaList = new ArrayList<String>();
    private static ArrayList<String> hargaBelanjaList = new ArrayList<String>();
    private static ArrayList<String> tanggalWaktuList = new ArrayList<String>();
    private static ArrayList<String> namaKonsumenList = new ArrayList<String>();
    private static ArrayList<String> telpKonsumenList = new ArrayList<String>();
    private static ArrayList<String> alamatKonsumenList = new ArrayList<String>();
    private static ArrayList<String> pesananIdList = new ArrayList<String>();
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_va_bca_permata);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        instance = this;
        va_model = getIntent().getParcelableExtra(VAModel.TAG);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //String test =  getIntent().getStringExtra("TEST");
        //va_model = getIntent().getParcelableExtra(BannerModel.TAG);
        //banner_model = getIntent().getParcelableExtra(BannerModel.TAG);

        //Awas... layout ditampilkan di bawah, jadi jangan declare sebangsa button, textview di sini... bikin stop aplikasi
        //Pertama kali daftarbaranglist harus dibersihkan karena kalau tidak dibersihkan akan nambah2 terus tiap activity dipanggil

        if (!daftarPemesanList.isEmpty()){
            daftarPemesanList.clear();
        }
        if (!daftarNoHpList.isEmpty()){
            daftarNoHpList.clear();
        }
        if (!daftarNamaKlinikList.isEmpty()){
            daftarNamaKlinikList.clear();
        }
        if (!daftarObat1List.isEmpty()){
            daftarObat1List.clear();
        }
        if (!daftarObat2List.isEmpty()){
            daftarObat2List.clear();
        }
        /*
        if (!daftarObat3List.isEmpty()){
            daftarObat3List.clear();
        }

        if (!daftarObat4List.isEmpty()){
            daftarObat4List.clear();
        }
        if (!daftarObat5List.isEmpty()){
            daftarObat5List.clear();
        }
        if (!daftarObat6List.isEmpty()){
            daftarObat6List.clear();
        }
        if (!daftarObat7List.isEmpty()){
            daftarObat7List.clear();
        }
        if (!daftarObat8List.isEmpty()){
            daftarObat8List.clear();
        }
        if (!daftarObat9List.isEmpty()){
            daftarObat9List.clear();
        }
        if (!daftarObat10List.isEmpty()){
            daftarObat10List.clear();
        }

        if (!daftarStatusList.isEmpty()){
            daftarStatusList.clear();
        }

        if (!daftarTanggalWaktuList.isEmpty()){
            daftarTanggalWaktuList.clear();
        }
        if (!daftarIdList.isEmpty()){
            daftarIdList.clear();
        }
        */
        //daftarPemesanList.add(va_model.va_number);
        //Toast.makeText(getApplicationContext(), banner_model.list.get(0).transaction_response.toString(), Toast.LENGTH_SHORT).show();
        /*
        daftarPemesanList.add("No.");
        daftarNoHpList.add("Bank");
        daftarNamaKlinikList.add("VA_Number");
        daftarObat1List.add("Amount");
        daftarObat2List.add("Status");
        */
        /*
        daftarObat3List.add("Obat3");
        daftarObat4List.add("Obat4");
        daftarObat5List.add("Obat5");
        daftarObat6List.add("Obat6");
        daftarObat7List.add("Obat7");
        daftarObat8List.add("Obat8");
        daftarObat9List.add("Obat9");
        daftarObat10List.add("Obat10");
        daftarStatusList.add("Status");
        daftarTanggalWaktuList.add("Waktu Pesan");
        daftarIdList.add("Id");
        */
        //Toast.makeText(getApplicationContext(), va_model.list.get(0).gross_amount, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();

        int intNumber=0;
        //for (int i = 0;i< va_model.list.size();i++){
        for (int i = va_model.list.size()-1 ;i>= 0;i--){
            if(va_model.list.get(i).bank.contains("bca")||va_model.list.get(i).bank.contains("permata")) {
                daftarPemesanList.add(String.valueOf(intNumber + 1).toString());
                daftarNoHpList.add(va_model.list.get(i).bank);
                daftarNamaKlinikList.add(va_model.list.get(i).va_number);
                long intResponse = Long.valueOf(va_model.list.get(i).gross_amount);
                String strResponse2 = NumberFormat.getNumberInstance(Locale.US).format(intResponse);
                daftarObat1List.add(strResponse2);
                //daftarObat1List.add(va_model.list.get(i).gross_amount);
                if (va_model.list.get(i).payment_status.contentEquals("settlement")) {
                    daftarObat2List.add("Paid");
                }else if (va_model.list.get(i).payment_status.contentEquals("pending")){
                    daftarObat2List.add("Unpaid");
                }else{
                    daftarObat2List.add(va_model.list.get(i).payment_status);
                }
                intNumber +=1;
            }
        }

        mListView1_1 = (ListView) findViewById(R.id.listView1_1);
        mListView1_2 = (ListView) findViewById(R.id.listView1_2);
        mListView1_3= (ListView) findViewById(R.id.listView1_3);
        mListView1_4= (ListView) findViewById(R.id.listView1_4);
        mListView1_5= (ListView) findViewById(R.id.listView1_5);
/*
        mListView1_6= (ListView) findViewById(R.id.listView1_6);
        mListView1_7= (ListView) findViewById(R.id.listView1_7);
        mListView1_8= (ListView) findViewById(R.id.listView1_8);
        mListView1_9= (ListView) findViewById(R.id.listView1_9);
        mListView1_10= (ListView) findViewById(R.id.listView1_10);
        mListView1_11= (ListView) findViewById(R.id.listView1_11);
        mListView1_12= (ListView) findViewById(R.id.listView1_12);
        mListView1_13= (ListView) findViewById(R.id.listView1_13);
        mListView1_14= (ListView) findViewById(R.id.listView1_14);
        mListView1_15= (ListView) findViewById(R.id.listView1_15);
        mListView1_16= (ListView) findViewById(R.id.listView1_16);
*/
        //terus terang belum paham simple_list_item_1 :-)
        // mListView2_1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, idPedagangList));
        //mListView2_2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, idDataPedagangList));

        //diisi bersamaan dengan data pelanggan=========================================================================


        mListView1_1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarPemesanList));
        mListView1_2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarNoHpList));
        mListView1_3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarNamaKlinikList));
        mListView1_4.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat1List));
        mListView1_5.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat2List));
/*
        mListView1_6.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat3List));
        mListView1_7.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat4List));
        mListView1_8.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat5List));
        mListView1_9.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat6List));
        mListView1_10.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat7List));
        mListView1_11.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat8List));
        mListView1_12.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat9List));
        mListView1_13.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarObat10List));
        mListView1_14.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarStatusList));
        mListView1_15.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarTanggalWaktuList));
        mListView1_16.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarIdList));
*/

 /*
        mListView1_1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarBelanjaList));
        mListView1_2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hargaBelanjaList));
        mListView1_3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tanggalWaktuList));
        mListView1_4.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namaKonsumenList));
        mListView1_5.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telpKonsumenList));
        mListView1_6.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alamatKonsumenList));
   */

        ListUtils.setDynamicHeight(mListView1_1);
        ListUtils.setDynamicHeight(mListView1_2);
        ListUtils.setDynamicHeight(mListView1_3);
        ListUtils.setDynamicHeight(mListView1_4);
        ListUtils.setDynamicHeight(mListView1_5);
/*
        ListUtils.setDynamicHeight(mListView1_6);
        ListUtils.setDynamicHeight(mListView1_7);
        ListUtils.setDynamicHeight(mListView1_8);
        ListUtils.setDynamicHeight(mListView1_9);
        ListUtils.setDynamicHeight(mListView1_10);
        ListUtils.setDynamicHeight(mListView1_11);
        ListUtils.setDynamicHeight(mListView1_12);
        ListUtils.setDynamicHeight(mListView1_13);
        ListUtils.setDynamicHeight(mListView1_14);
        ListUtils.setDynamicHeight(mListView1_15);
        ListUtils.setDynamicHeight(mListView1_16);
*/
        GlobalController.showMessage(instance, "Please transfer to Unpaid Virtual Account");

    }

    //===============================================================================================================

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
    //==============================================================================================

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }


}
