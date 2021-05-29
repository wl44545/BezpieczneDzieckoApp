package com.example.bezpiecznedziecko.child.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bezpiecznedziecko.BuildConfig;
import com.example.bezpiecznedziecko.R;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.example.bezpiecznedziecko.authorization.childLogout;
import com.example.bezpiecznedziecko.child.maps.childMap;
import com.example.bezpiecznedziecko.child.schedules.childSchedulesList;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class childMain extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    Button btn_maps, btn_schedules, btn_logout, btn_panic;
    TextView txt_name, txt_login;
    String login,first_name,last_name,name;

    private static final String TAG = childMain.class.getSimpleName();

    // sprawdzanie uprawnien w trakcie uzywania
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private childLocationService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            childLocationService.LocalBinder binder = (childLocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        setContentView(R.layout.child_main);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        login = sharedPref.getString(getString(R.string.shared_preferences_login), getString(R.string.shared_preferences_login));
        first_name = sharedPref.getString(getString(R.string.shared_preferences_first_name),getString(R.string.shared_preferences_first_name));
        last_name = sharedPref.getString(getString(R.string.shared_preferences_last_name), getString(R.string.shared_preferences_last_name));

        name = first_name + " " + last_name;

        txt_name = (TextView) findViewById(R.id.view_txt2);
        txt_login = (TextView) findViewById(R.id.view_txt1);
        txt_name.setText(name);
        txt_login.setText(login);

        btn_maps = (Button)findViewById(R.id.btn_maps);
        btn_maps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(childMain.this, childMap.class);
                startActivity(intent);
            }
        });
        btn_schedules = (Button)findViewById(R.id.btn_schedules);
        btn_schedules.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(childMain.this, childSchedulesList.class);
                startActivity(intent);
            }
        });
        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                mService.removeLocationUpdates();
                Intent intent = new Intent(childMain.this, childLogout.class);
                startActivity(intent);
            }
        });
        btn_panic = (Button)findViewById(R.id.btn_panic);
        btn_panic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

            }
        });

        // Check that the user hasn't revoked permissions by going to Settings.
        if (childLocationUtils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

//        bindService(new Intent(this, childLocationService.class), mServiceConnection,
//                Context.BIND_AUTO_CREATE);
//        mService.requestLocationUpdates();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        boolean isOkay = bindService(new Intent(this, childLocationService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
        Log.d("bindService",String.valueOf(isOkay));
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(childLocationService.ACTION_BROADCAST));
//        mRequestLocationUpdatesButton.callOnClick();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
            findViewById(R.id.child_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(childMain.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(childMain.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                Snackbar.make(
                        findViewById(R.id.child_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    /**
     * Receiver for broadcasts sent by {@link childLocationService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*Location location = intent.getParcelableExtra(childLocationService.EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(childMain.this, childLocationUtils.getLocationText(location),
                        Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, childLogout.class);
        startActivity(intent);
    }

}