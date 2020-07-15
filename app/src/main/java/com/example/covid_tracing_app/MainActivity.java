package com.example.covid_tracing_app;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{
    int REQUEST_ENABLE_BT = 420;
    TextView textStatus;
    TextView textValue;
    BluetoothAdapter bluetoothAdapter;

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;

    private final BroadcastReceiver broadcastReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        textStatus.setText("블루투스가 켜져있지 않습니다.");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        textStatus.setText("TUNING OFF...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        textStatus.setText("서비스가 활성화 중입니다.");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        textStatus.setText("TUNING ON...");
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        textStatus = (TextView)findViewById(R.id.textViewStatus);
        textValue = (TextView)findViewById(R.id.textViewValue);

        IntentFilter btFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiver, btFilter);
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // ibeacon layout
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(),"Bluetooth Not Support", Toast.LENGTH_SHORT).show();//블루투스가 지원하지 않을 경우
        }
        else{
            if(!bluetoothAdapter.isEnabled()){
                textStatus.setText("블루투스가 켜져있지 않습니다.");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else{
                textStatus.setText("서비스가 활성화 중입니다.");
            }
        }
    }

    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException ignored) {    }
    }
}
