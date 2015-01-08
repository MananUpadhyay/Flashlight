package com.example.flash;

import java.io.IOException;

import com.example.flashlight.R;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/*
 * * Receives custom onClick Intent from the Widget.
 * 	 Toggles Camera Flash.
 * 
 */
public class FlashReceiver extends BroadcastReceiver {

	boolean mFlashPresentFlag;
	private static Camera mCamera = null;
	private static boolean flashOn = false;
	private final int INVALID_WIDGET_ID = 0;

	@Override
	public void onReceive(Context ctxt, Intent intent) {
		Log.d("FlashReceiver", "Widget Intent received");

		// checking if the phone has a flash.
		mFlashPresentFlag = ctxt.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH);

		if (!mFlashPresentFlag) {
			Toast.makeText(ctxt, "Flash Not Supported", Toast.LENGTH_SHORT)
					.show();
		} else {
			// Flash present

			int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID, INVALID_WIDGET_ID);

			RemoteViews views = new RemoteViews(ctxt.getPackageName(),
					R.layout.flash_layout);
			try {
				if (mCamera == null)
					mCamera = Camera.open();
				else
					mCamera.reconnect();
			} catch (IOException e) {
				Log.e("FlashService", "In IOException");
				// releasing Camera resources in case of an Exception.
				mCamera.release();
			}

			final Camera.Parameters cameraParams = mCamera.getParameters();
			if (!flashOn) {
				views.setImageViewResource(R.id.img, R.drawable.light91);
				cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(cameraParams);
				mCamera.startPreview();
			} else {
				views.setImageViewResource(R.id.img, R.drawable.light91gray);
				cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(cameraParams);
				mCamera.stopPreview();
				if (mCamera != null) {
					mCamera.release();
					mCamera = null;
				}
			}
			flashOn = !flashOn;

			// Update Widget to reflect appropriate image resource based on
			// flash state.
			AppWidgetManager apm = AppWidgetManager.getInstance(ctxt);
			if (INVALID_WIDGET_ID != appWidgetId)
				apm.updateAppWidget(appWidgetId, views);

		}

	}

}
