package com.example.covid_tracing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    int REQUEST_ENABLE_BT = 420;
    TextView textStatus;
    BluetoothAdapter bluetoothAdapter;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

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

        IntentFilter btFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiver, btFilter);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
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

        textStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BeaconService.class);
                //startActivity(intent);
                startService(intent);
            }
        });
    }
}
