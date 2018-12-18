package id.co.kynga.app.general.controller;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import id.co.kynga.app.R;

public class ImageController {
	public static void initLoader(final Context context) {
		final File cache_dir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.diskCacheExtraOptions(480, 800, null)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				//.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.diskCacheSize(50 * 1024 * 1024)
				.diskCache(new UnlimitedDiskCache(cache_dir))
				.diskCacheFileCount(100)
				.threadPoolSize(3)
				.build();
		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions getOption(final boolean cache_disk) {
		return new DisplayImageOptions.Builder()
				.cacheInMemory(false)
				.cacheOnDisk(cache_disk)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	public static DisplayImageOptions getOptionPlaceholder(final boolean cache_disk) {
		return new DisplayImageOptions.Builder()
				.cacheInMemory(false)
				.cacheOnDisk(cache_disk)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnLoading(R.drawable.ic_logo_thundr_small)
				.showImageOnFail(R.drawable.ic_logo_thundr_small)
				.showImageForEmptyUri(R.drawable.ic_logo_thundr_small)
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();
	}
}