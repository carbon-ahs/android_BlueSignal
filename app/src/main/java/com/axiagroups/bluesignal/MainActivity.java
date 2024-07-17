package com.axiagroups.bluesignal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnOn;
    Button btnOff;
    Button btnScan;

    DeviceRecyclerViewAdapter adapter;
    ArrayList<Device> devices;
    RecyclerView recyclerView;
     int scanCount = 3;
    BluetoothAdapter mBluetoothAdapter;
    Intent btIntent;
    int reqCode = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnOn = findViewById(R.id.btn_bt_on);
        btnOff = findViewById(R.id.btn_bt_off);
        btnScan = findViewById(R.id.btn_scan);
        recyclerView = findViewById(R.id.rv_devices);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);


        // Dummy devices
        devices = new ArrayList<Device>();
        adapter = new DeviceRecyclerViewAdapter(getApplicationContext(), devices);
        recyclerView.setAdapter(adapter);
//        devices.add(new Device("D1", "-59dB"));
//        devices.add(new Device("D2", "-59dB"));
//        devices.add(new Device("D3", "-59dB"));
//        devices.add(new Device("D4", "-59dB"));
//        devices.add(new Device("D5", "-59dB"));

        //permissions granted at runtime
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
        }

        turnOnBluetooth();
        turnOffBluetooth();
        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnScan.setVisibility(View.GONE);
                btnOn.setVisibility(View.GONE);
                try {
                    if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
                    }
                    if (mBluetoothAdapter.isDiscovering()) {
                        mBluetoothAdapter.cancelDiscovery();
                    }
                    scanCount = 1; //for ex change the scan count number to 3 then aftre pressing the scan button (once) it will scan three time 12*3=36 seconds
                    mBluetoothAdapter.startDiscovery();

                    recyclerView.setVisibility(View.VISIBLE);
                } catch (Exception npe) {
                    Toast.makeText(getApplicationContext(), "Exception, Scan Again", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void turnOffBluetooth() {
        btnOff.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();

                }
            }
        });
    }

    private void turnOnBluetooth() {
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter == null) {
                    Toast.makeText(MainActivity.this, "Bluetooth Not Supported.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        startActivityForResult(btIntent, reqCode);
                    } else
                        Toast.makeText(MainActivity.this, "BT already enabled.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == reqCode) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth is Enabled.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
            }

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MAX_VALUE);
                Toast.makeText(getApplicationContext(), "Device Found " + device.getName() + " RSSI: " + rssi + "dBm ", Toast.LENGTH_SHORT).show();

                //input data to display
                if (device.getName() != null) {
                    devices.add(new Device(device.getName().toString(), device.getAddress() , String.valueOf(rssi)));
                } else {
                    devices.add(new Device("Null" ,device.getAddress() , String.valueOf(rssi)));
                }

                adapter.notifyDataSetChanged();
//                scannedListView.setAdapter(arrayAdapter);
            }
            else if (mBluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(getApplicationContext(), "Discovery Finished", Toast.LENGTH_SHORT).show();
                scanCount=scanCount-1;
                if(scanCount>0){
                    mBluetoothAdapter.startDiscovery();
                }
                Log.d("TAG", "onReceive: "+ devices);
            }
            else if (mBluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(getApplicationContext(), "Discovery Started", Toast.LENGTH_SHORT).show();
            }
        }
    };


}