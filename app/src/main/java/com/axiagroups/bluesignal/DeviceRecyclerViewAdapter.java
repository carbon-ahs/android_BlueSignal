package com.axiagroups.bluesignal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Ahsan Habib on 7/17/2024.
 */
public class DeviceRecyclerViewAdapter extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Device> devices;
    private Context context;

    public DeviceRecyclerViewAdapter(Context context,
                                     ArrayList<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @NonNull
    @Override
    public DeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_device, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceRecyclerViewAdapter.ViewHolder holder,
                                 int position) {

        Device device = devices.get(position);
        holder.name.setText("Name: "+device.getName());
        holder.strength.setText("Strength: "+device.getStrength()+"dB");
        holder.mac.setText("Mac Address: "+device.getMacAddress());

    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, strength, mac;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            strength = itemView.findViewById(R.id.tv_strength);
            mac = itemView.findViewById(R.id.tv_mac);
        }
    }
}
