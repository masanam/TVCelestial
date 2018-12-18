package id.co.kynga.app.ui.adapter;

import id.co.kynga.app.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class GridMenuAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	int [] buttons;

	public GridMenuAdapter(Context ctx ,int[] btnList){
		context = ctx;
		buttons = btnList;
	}
	@Override
	public int getCount() {
		return buttons.length;
	}

	@Override
	public Object getItem(int pos) {
		return buttons[pos];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View vi = convertView;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			vi = inflater.inflate(R.layout.list_menu_item,parent, false);
		}

		ImageView image = (ImageView)vi.findViewById(R.id.image_menu);
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int imgWidth = (metrics.widthPixels - 82) / 6;
		int imgHeight = (int) (imgWidth * 0.74);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(imgWidth,imgHeight);
		image.setLayoutParams(params);
		//image.setImageResource(buttons[pos]);

		image.setImageBitmap(
				decodeSampledBitmapFromResource(context.getResources(), buttons[pos], 200, 200));
	//	image.setImageBitmap(src);

		//Picasso.with(context).load(buttons[pos]).into(image);//worst
		//image.setBackgroundResource(buttons[pos]); //not work
		//image.setScaleType(ImageView.ScaleType.CENTER_CROP); //to make efficient need to be tested in Xiaomi
		//image.setLayoutParams(new GridView.LayoutParams(imgWidth, imgHeight)); //to make efficient need to be tested in Xiaomi
		return vi;
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
														 int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}


}