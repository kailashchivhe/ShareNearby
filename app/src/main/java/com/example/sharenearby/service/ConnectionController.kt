package com.example.sharenearby.service

import android.content.Context
import android.util.Log
import com.example.sharenearby.listener.ConnectionListener
import com.example.sharenearby.model.Constants.SERVICE_ID
import com.example.sharenearby.model.Device
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionOptions
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener


class ConnectionController(
    private val context: Context,
    private val connectionListener: ConnectionListener
) {

    private var userName: String = ""

    fun startAdvertising() {
        val advertisingOptions =
            AdvertisingOptions.Builder().setStrategy(Strategy.P2P_POINT_TO_POINT).build()
        Nearby.getConnectionsClient(context)
            .startAdvertising(
                userName, SERVICE_ID, object: ConnectionLifecycleCallback() {
                    override fun onConnectionInitiated(endPointId: String, connectionInfo: ConnectionInfo) {
                        if (connectionInfo.isIncomingConnection) {
                            connectionListener.onIncomingConnection(Device(endPointId, connectionInfo.endpointName))
                        }
                    }

                    override fun onConnectionResult(endPointId: String, connectionResolution: ConnectionResolution) {
                        connectionListener.onConnectionStatus(endPointId, connectionResolution)
                    }

                    override fun onDisconnected(endPointId: String) {
                        connectionListener.onDisconnected(endPointId)
                    }
                }, advertisingOptions
            )
            .addOnSuccessListener(
                OnSuccessListener<Void> { unused: Void? ->  connectionListener.isAdvertisedStarted(true) })
            .addOnFailureListener(
                OnFailureListener { e: Exception? -> connectionListener.isAdvertisedStarted(false) })
    }


    fun startDiscovery() {
        val discoveryOptions =
            DiscoveryOptions.Builder().setStrategy(Strategy.P2P_POINT_TO_POINT).build()
        Nearby.getConnectionsClient(context)
            .startDiscovery(SERVICE_ID, object : EndpointDiscoveryCallback(){
                override fun onEndpointFound(endPointId: String, discoveredEndpointInfo: DiscoveredEndpointInfo) {
                    connectionListener.onDeviceFound(Device(endPointId, discoveredEndpointInfo.endpointName))
                }

                override fun onEndpointLost(endPointId: String) {
                    connectionListener.onDeviceLost(endPointId)
                }

            }, discoveryOptions)
            .addOnSuccessListener { unused: Void? -> connectionListener.onDiscoveryStarted(true) }
            .addOnFailureListener { e: java.lang.Exception? -> connectionListener.onDiscoveryStarted(false) }
    }

    fun stopDiscovery() {
        Nearby.getConnectionsClient(context).stopDiscovery()
        connectionListener.onDiscoveryStarted(false)
    }

    fun stopAdvertising() {
        Nearby.getConnectionsClient(context).stopAdvertising()
        connectionListener.isAdvertisedStarted(false)
    }

    fun sendConnectionRequest(device: Device) {
        Nearby.getConnectionsClient(context).requestConnection(userName, device.deviceId, object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endPointId: String, connectionInfo: ConnectionInfo) {
                //Not required
                acceptConnection(endPointId, true)
            }

            override fun onConnectionResult(endPointId: String, connectionResolution: ConnectionResolution) {
                if(connectionResolution.status.isSuccess) {
                    connectionListener.onRequestAccepted(endPointId, true)
                } else {
                    connectionListener.isRequested(false)
                }
            }

            override fun onDisconnected(endPointId: String) {
                connectionListener.onDeviceLost(endPointId)
            }
        }).addOnSuccessListener {
            Log.d("Temp ", " ==> addOnSuccessListener: ")
            connectionListener.isRequested(true)
        }.addOnFailureListener {
            Log.d("Temp ", " ==> addOnFailureListener: ")
            connectionListener.isRequested(false)
        }
    }

    fun acceptConnection(deviceId: String, isSender: Boolean) {
        Log.d("Temp ", " ==> acceptConnection: ${deviceId}")
        Nearby.getConnectionsClient(context).acceptConnection(deviceId, object : PayloadCallback() {
            override fun onPayloadReceived(endPointId: String, payload: Payload) {
                connectionListener.onMessageReceived(endPointId, payload.asBytes()?.toString(Charsets.UTF_8) ?: "")
            }

            override fun onPayloadTransferUpdate(endPointId: String, update: PayloadTransferUpdate) {
//                connectionListener.onPayloadTransferUpdate(update.bytesTransferred, update.totalBytes)
            }
        }).addOnSuccessListener {
            connectionListener.onRequestAccepted(deviceId, isSender)
        }.addOnFailureListener {
            //Not required handle later
            Log.d("Temp ", " ==> acceptConnection: onFailure")
        }
    }

    fun denyRequest(device: Device) {
        Nearby.getConnectionsClient(context).rejectConnection(device.deviceId).addOnSuccessListener {
            connectionListener.onRequestDenied(device.deviceId)
        }.addOnFailureListener {
            //Not required handle later
        }
    }

    fun sendPayload(deviceId: String, message: String) {
        Nearby.getConnectionsClient(context).sendPayload(deviceId,
            Payload.fromBytes(message.toByteArray())).addOnSuccessListener {
                connectionListener.onMessageSent(message)
        }.addOnFailureListener {
            //Not required handle later
        }
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }
}