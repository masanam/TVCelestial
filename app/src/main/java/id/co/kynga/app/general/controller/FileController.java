package id.co.kynga.app.general.controller;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import id.co.kynga.app.KyngaApp;

public class FileController {
	public static void checkStoragePermission(final Activity activity) {
		if (ContextCompat.checkSelfPermission(KyngaApp.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
		} else if (ContextCompat.checkSelfPermission(KyngaApp.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
		}
	}

	public static boolean isStoragePermitted() {
		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(KyngaApp.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(KyngaApp.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			return false;
		}
		return true;
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	public static void copy(final String source_path, final String destination_path) {
		try {
			final InputStream input = new FileInputStream(source_path);
			final OutputStream output = new FileOutputStream(destination_path);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
			output.close();
			input.close();
		} catch (IOException ex) {
		}
	}

	public static Uri getOutputMediaFileUri(
			final String directory,
			final String filename) {
		final File file = FileController.getOutputMediaFile(directory, filename);
		if (file == null) {
			return null;
		}
		return Uri.fromFile(file);
	}

	public static File getOutputMediaFile(
			final String directory,
			final String filename) {
		File media_storage_dir;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			media_storage_dir = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES), directory);
		} else {
			media_storage_dir = KyngaApp.getContext().getCacheDir();
		}
		if (!media_storage_dir.exists()) {
			if (!media_storage_dir.mkdirs()) {
				return null;
			}
		}
		final String file_path =
				media_storage_dir.getPath() + File.separator +
						filename;
		return new File(file_path);
	}

	public static String getOutputMediaDirectory() {
		File media_storage_dir;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			media_storage_dir = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES), "Kynga");
		}else {
			media_storage_dir = KyngaApp.getContext().getCacheDir();
		}
		if (!media_storage_dir.exists()) {
			return "";
		}
		return media_storage_dir.getPath() + File.separator;
	}

	public static String getOutputMediaDecoded(final String filename) {
		final String file_path = FileController.getOutputMediaDirectory() + filename;
		final String uri = Uri.fromFile(new File(file_path)).toString();
		return Uri.decode(uri);
	}
}
