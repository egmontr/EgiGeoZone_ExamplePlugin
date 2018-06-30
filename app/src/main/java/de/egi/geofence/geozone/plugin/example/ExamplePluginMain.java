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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ExamplePluginMain extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener{

    // The SharedPreferences object in which settings are stored
    private SharedPreferences mPrefs = null;
    // The name of the resulting SharedPreferences
    public static final String SHARED_PREFERENCE_NAME = ExamplePluginMain.class.getSimpleName();
    SharedPreferences.Editor editor;

    public static final String SHSWITCH = "switch";
    public static final String SHNOTIFICATION = "notification";

    ToggleButton toggle;
    TextView tvNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        mPrefs = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
    	editor = mPrefs.edit();

        toggle = (ToggleButton) this.findViewById(R.id.toggleButton);
        tvNotification = (TextView) this.findViewById(R.id.editNotification);

        tvNotification.addTextChangedListener(this);
        toggle.setOnCheckedChangeListener(this);

        boolean sw = mPrefs.getBoolean(SHSWITCH, false);
        String notif = mPrefs.getString(SHNOTIFICATION, "Test notification");

        toggle.setChecked(sw);
        tvNotification.setText(notif);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plugin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            Intent info = new Intent(this, Info.class);
            startActivity(info);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    	editor.putString(SHNOTIFICATION, tvNotification.getText().toString());
        editor.commit();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            editor.putBoolean(SHSWITCH, true);
        }
        else
        {
            editor.putBoolean(SHSWITCH, false);
        }
        editor.commit();
    }
}
