package com.example.sharenearby.listener

import com.example.sharenearby.model.Device
import com.google.android.gms.nearby.connection.ConnectionResolution

interface ConnectionListener {
    fun onIncomingConnection(device: Device)
    fun onConnectionStatus(deviceId: String, connectionResolution: ConnectionResolution)
    fun onDisconnected(deviceId: String)
    fun isAdvertisedStarted(isStarted: Boolean)

    fun onDeviceFound(device: Device)
    fun onDeviceLost(deviceId: String)
    fun onDiscoveryStarted(isStarted: Boolean)

    fun onRequestAccepted(deviceId: String, isSender: Boolean)
    fun onRequestDenied(deviceId: String)
    fun isRequested(isRequested: Boolean)

    fun onMessageReceived(endPointId: String, message: String)
    fun onMessageSent(message: String)
}