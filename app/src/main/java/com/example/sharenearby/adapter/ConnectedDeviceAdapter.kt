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
import com.google.android.material.textview.MaterialTextView


class ConnectedDeviceAdapter(val onChatButtonClicked: (Device) -> Unit) : RecyclerView.Adapter<ConnectedDeviceAdapter.ConnectedDeviceViewHolder>() {
    private val deviceList: ArrayList<Device> = arrayListOf()

    fun updateData(connectionsData: List<Device>) {
        deviceList.clear()
        if(connectionsData.isNotEmpty()) {
            deviceList.addAll(connectionsData)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedDeviceViewHolder {
        return ConnectedDeviceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_connected_device,
                parent, false))
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ConnectedDeviceViewHolder, position: Int) {
        val device = deviceList[position]
        holder.name.text = device.deviceName
        holder.chatButton.setOnClickListener {
            onChatButtonClicked(device)
        }
    }


    inner class ConnectedDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: MaterialTextView = itemView.findViewById(R.id.device_name)
        val chatButton: AppCompatImageButton = itemView.findViewById(R.id.button_chat)
    }
}