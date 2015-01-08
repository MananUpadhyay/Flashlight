package com.example.flash;

import com.example.flashlight.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FlashWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		final int N = appWidgetIds.length;
		// set onClick Event listeners for all instances of the widget.
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			Intent intent = new Intent(context, FlashReceiver.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			intent.setAction("com.example.flash.FLASH_ACTION");

			PendingIntent pending = PendingIntent.getBroadcast(context,
					appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.flash_layout);

			views.setOnClickPendingIntent(R.id.img, pending);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
