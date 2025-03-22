package com.example.sharenearby.adapter

import android.annotation.SuppressLint
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


class DeviceRequestAdapter(val onRequestAcceptedDenied: (Device, Boolean) -> Unit) : RecyclerView.Adapter<DeviceRequestAdapter.DeviceRequestViewHolder>() {

    private val deviceList: ArrayList<Device> = arrayListOf()

    fun updateData(connectionsData: List<Device>) {
        deviceList.clear()
        if(connectionsData.isNotEmpty()) {
            deviceList.addAll(connectionsData)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceRequestViewHolder {
        return DeviceRequestViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_requests,
                parent, false))
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DeviceRequestViewHolder, position: Int) {
        val device = deviceList[position]
        holder.name.text = device.deviceName
        holder.acceptButton.setOnClickListener {
            onRequestAcceptedDenied(device, true)
        }
        holder.denyButton.setOnClickListener {
            onRequestAcceptedDenied(device, false)
        }
    }


    inner class DeviceRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: MaterialTextView = itemView.findViewById(R.id.device_name)
        val acceptButton: AppCompatImageButton = itemView.findViewById(R.id.button_accept)
        val denyButton: AppCompatImageButton = itemView.findViewById(R.id.button_deny)
    }
}