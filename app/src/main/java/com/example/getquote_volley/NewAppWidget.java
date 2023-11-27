package com.example.getquote_volley;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.getquote_volley.MainActivity;
import com.example.getquote_volley.R;

public class NewAppWidget extends AppWidgetProvider {

    public static final String PREFS_NAME = "WidgetPrefs";
    public static final String KEY_WIDGET_TEXT = "widgetText";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            // Set the text on the widget from SharedPreferences
            String widgetText = getWidgetTextFromPrefs(context);
            views.setTextViewText(R.id.appwidget_text, widgetText);

            // Set up click event to open MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private String getWidgetTextFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_WIDGET_TEXT, "Default Widget Text");
    }



}
