package id.co.kynga.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import id.co.kynga.app.R;

public class GridPaymentAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	ArrayList<HashMap<String, String>> listPrice;
	private OnItemClickedListener onItemClickedListener;

	public GridPaymentAdapter(Context ctx , ArrayList<HashMap<String, String>> listPrice){
		context = ctx;
		this.listPrice = listPrice;
	}
	@Override
	public int getCount() {
		return listPrice.size();
	}

	@Override
	public Object getItem(int pos) {
		return listPrice.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		View vi = convertView;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			vi = inflater.inflate(R.layout.grid_price,parent, false);
		}

		HashMap<String, String> priceData = listPrice.get(pos);
		TextView monthView = (TextView) vi.findViewById(R.id.price_month);
		TextView monthPriceView = (TextView) vi.findViewById(R.id.price_per_month);
		TextView monthTotal = (TextView) vi.findViewById(R.id.price_total);
//		ImageButton buyNow = (ImageButton) vi.findViewById(R.id.price_buy);


		DecimalFormat df = new DecimalFormat("Rp ###,###,###.##");
		String price = df.format(Integer.parseInt(priceData.get("Price")));
		monthView.setText(priceData.get("Remark"));
		monthPriceView.setText(" ");
		monthTotal.setText(price);


		vi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onItemClickedListener!=null)
					onItemClickedListener.onItemClicked(pos);
			}
		});

		return vi;
	}

	public static abstract interface OnItemClickedListener{
		void onItemClicked(int pos);
	}

	public void setOnItemClickedListener(OnItemClickedListener listener){
		onItemClickedListener = listener;
	}
}