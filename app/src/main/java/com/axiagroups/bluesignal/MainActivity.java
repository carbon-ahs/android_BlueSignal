package com.axiagroups.bluesignal;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnOn;
    Button btnOff;

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
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        turnOnBluetooth();
        turnOffBluetooth();

    }

    private void turnOffBluetooth() {
        btnOff.setOnClickListener(new View.OnClickListener() {
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
                    }
                }
            }
        });
    }

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
}