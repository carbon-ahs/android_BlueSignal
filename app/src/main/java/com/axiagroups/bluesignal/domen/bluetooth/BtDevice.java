package com.axiagroups.bluesignal.domen.bluetooth;

/**
 * Created by Ahsan Habib on 7/16/2024.
 */
public class BtDevice {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "BtDevice{" +
                "address='" + address + '\'' +
                '}';
    }
}
