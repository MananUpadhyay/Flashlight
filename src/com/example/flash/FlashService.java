package com.example.flash;

import com.example.flashlight.R;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
/**
 * 
 * Can be used instead of the BroadcastReceiver but an overkill and hence abandoned.
 * 
*/
public class FlashService extends Service {
	Context mContext;
	boolean mFlashPresentFlag;
	private static Camera mCamera;
	private static boolean flashOn = false;
	private final int INVALID_WIDGET_ID = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mFlashPresentFlag = mContext.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!mFlashPresentFlag) {
			Toast.makeText(mContext, "Flash Not Supported", Toast.LENGTH_SHORT)
					.show();
		} else {

			int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID, INVALID_WIDGET_ID);

			RemoteViews views = new RemoteViews(mContext.getPackageName(),
					R.layout.flash_layout);

			mCamera = Camera.open();
			final Camera.Parameters cameraParams = mCamera.getParameters();

			if (!flashOn) {
				views.setImageViewResource(R.id.img, R.drawable.light91);
				cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
				mCamera.setParameters(cameraParams);
				mCamera.startPreview();
			} else {
				views.setImageViewResource(R.id.img, R.drawable.light91gray);
				cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(cameraParams);
				mCamera.stopPreview();
			}

			AppWidgetManager apm = AppWidgetManager.getInstance(mContext);
			if (INVALID_WIDGET_ID != appWidgetId)
				apm.updateAppWidget(appWidgetId, views);

			this.stopSelf();
		}

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		if (mCamera != null)
			mCamera.release();
		super.onDestroy();
	}
}
