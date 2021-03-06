/*
* Copyright 2014 - 2015 Egmont R. (egmontr@gmail.com)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/		

package de.egi.geofence.geozone.plugin.example;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code ExampleBroadcastReceiverPlugin} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
@SuppressWarnings("unused")
public class ExamplePluginIntentService extends IntentService {

	private static final String ACTION_EGIGEOZONE_EVENT = "de.egi.geofence.geozone.plugin.EVENT";
    // The SharedPreferences object in which settings are stored
    private SharedPreferences mPrefs = null;

    private String transText;
    private String zoneName;
	private String latitude;
    private String longitude;
    private String deviceId;
    private String date;

    
    private String notificationText = null;

    public ExamplePluginIntentService() {
        super("ExamplePluginIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

		String action = intent.getAction();
		if (ACTION_EGIGEOZONE_EVENT.equals(action)) {
			// Call Method to perform EgiGeoZone events
			doEvent(intent);
		}
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        ExampleBroadcastReceiverPlugin.completeWakefulIntent(intent);
    }

	private void doEvent(Intent intent){
        mPrefs = getSharedPreferences(ExamplePluginMain.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        // Transmit event only if activated
        boolean sw = mPrefs.getBoolean(ExamplePluginMain.SHSWITCH, false);
        if (!sw) return;

        notificationText = mPrefs.getString(ExamplePluginMain.SHNOTIFICATION, null);

        String transition = intent.getStringExtra("transition");
        transText = "";
        if (transition.equals("1")){
            transText = "Entering";
        }else{
            transText = "Leaving";
        }
		zoneName = intent.getStringExtra("zone_name");
		latitude = intent.getStringExtra("latitude");
		longitude = intent.getStringExtra("longitude");
		deviceId = intent.getStringExtra("device_id");
		date = intent.getStringExtra("date");

		// Post notification as an example, what we can do
		sendNotification(this);

	}
    /**
     * Posts a notificationText in the notificationText bar.
     * If the user clicks the notificationText, control goes to the main Activity.
     * @param transitionType The type of transition that occurred.
     *
     */
    public void sendNotification(Context context) {
        // Create an explicit content Intent that starts the main Activity
        Intent notificationIntent = new Intent(context, ExamplePluginMain.class);

        // Construct a task stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the main Activity to the task stack as the parent
        stackBuilder.addParentStack(ExamplePluginMain.class);

        // Push the content Intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notificationText builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Set the notificationText contents
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay).setContentTitle(transText + " " + zoneName)
               .setContentText(notificationText).setContentIntent(notificationPendingIntent).setDefaults(Notification.DEFAULT_ALL);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notificationText
        mNotificationManager.notify(0, builder.build());
    }
}















