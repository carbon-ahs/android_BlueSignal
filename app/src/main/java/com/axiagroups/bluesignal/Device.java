package com.axiagroups.bluesignal;

/**
 * Created by Ahsan Habib on 7/17/2024.
 */
public class Device {
    String name, strength, macAddress;

    public String getName() {
        return name;
    }

    public Device(
                  String name,
                  String macAddress,
                  String strength) {
        this.macAddress = macAddress;
        this.name = name;
        this.strength = strength;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Device{" +
                "macAddress='" + macAddress + '\'' +
                ", name='" + name + '\'' +
                ", strength='" + strength + '\'' +
                '}';
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
