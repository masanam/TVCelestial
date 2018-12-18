package id.co.kynga.app.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.kynga.app.R;

/**
 * Created by Toshiba on 1/11/2017.
 */

public class CategoryImgAdapter extends BaseAdapter {

    private final Activity context;
    private final ArrayList<HashMap<String, String>> category;
    private int positionId = -1;

    public CategoryImgAdapter(Activity context,
                              ArrayList<HashMap<String, String>> category) {
        this.context = context;
        this.category = category;
    }

    @Override
    public int getCount() {
        return category.size();
    }

    @Override
    public Object getItem(int position) {
        return category.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(category.get(position).get("id"));
    }

    public void setIndex(int id) {
        positionId = id;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_category_img, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.category_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.category_img);

        HashMap<String, String> cat = category.get(position);

        if (positionId == position) {
            txtTitle.setBackgroundResource(R.color.semi_transparan);
            imageView.setBackgroundResource(R.color.semi_transparan);
        } else {
            txtTitle.setBackgroundResource(R.color.full_transparant);
            imageView.setBackgroundResource(R.color.full_transparant);
        }


        switch (cat.get("id")){
            case "10017": //food n mood
                txtTitle.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ic_happen_foodnmood);
                break;
            case "10036": //happen tv live
                txtTitle.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ic_happen_tv_live);
                break;
            case "10035": //walking fitly
                txtTitle.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ic_happen_walkingfitly);
                break;
            case "10030": //musicuality
                txtTitle.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ic_happen_musicuality);
                break;
            case "10032": //travelogy
                txtTitle.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ic_happen_travelogy);
                break;
            case "10034": //kardashian
                txtTitle.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ic_happen_kardashian);
                break;
            default:
                txtTitle.setText(cat.get("name"));
                imageView.setVisibility(View.GONE);
                break;
        }
        return rowView;
    }
}