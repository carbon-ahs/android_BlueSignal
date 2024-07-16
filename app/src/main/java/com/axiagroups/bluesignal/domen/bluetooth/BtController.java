package com.axiagroups.bluesignal.domen.bluetooth;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

/**
 * Created by Ahsan Habib on 7/16/2024.
 */
interface BtController {
    Flow<List<BtDevice>> scannedDevices = null;
    Flow<List<BtDevice>> pairedDevices = null;

    void startDiscovery();
    void stopDiscovery();
    void release();
}
