package com.example.sharenearby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sharenearby.R
import com.example.sharenearby.model.Device
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class DiscoveredDeviceAdapter(val onRequestConnect: (Device) -> Unit) : RecyclerView.Adapter<DiscoveredDeviceAdapter.DiscoveredDeviceViewHolder>() {

    private val deviceList: ArrayList<Device> = arrayListOf()

    fun updateData(connectionsData: List<Device>) {
        deviceList.clear()
        if(connectionsData.isNotEmpty()) {
            deviceList.addAll(connectionsData)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoveredDeviceViewHolder {
        return DiscoveredDeviceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_discovered_device,
            parent, false))
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: DiscoveredDeviceViewHolder, position: Int) {
        val device = deviceList[position]
        holder.name.text = device.deviceName
        holder.requestButton.setOnClickListener {
            onRequestConnect(device)
        }
    }


    inner class DiscoveredDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: MaterialTextView = itemView.findViewById(R.id.device_name)
        val requestButton: AppCompatImageButton = itemView.findViewById(R.id.button_request)
    }
}