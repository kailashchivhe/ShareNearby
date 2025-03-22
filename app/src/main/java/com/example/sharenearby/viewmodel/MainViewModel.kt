package com.example.sharenearby.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharenearby.listener.ConnectionListener
import com.example.sharenearby.model.Device
import com.example.sharenearby.model.Message
import com.example.sharenearby.service.ConnectionController
import com.google.android.gms.nearby.connection.ConnectionResolution
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application), ConnectionListener {

    private var connectionController: ConnectionController? = null

    private val _requestList = MutableLiveData<List<Device>>(emptyList())
    val requestList: LiveData<List<Device>> get() = _requestList

    private val _connectedDeviceList = MutableLiveData<List<Device>>(emptyList())
    val connectedDeviceList: LiveData<List<Device>> get() = _connectedDeviceList

    private val _isAdvertising = MutableLiveData(false)
    val isAdvertising: LiveData<Boolean> get() = _isAdvertising

    private val _isDiscovering = MutableLiveData(false)
    val isDiscovering: LiveData<Boolean> get() = _isDiscovering

    private val _discoveredDeviceList = MutableLiveData<List<Device>>(emptyList())
    val discoveredDeviceList: LiveData<List<Device>> get() = _discoveredDeviceList

    private val _isRequested = MutableLiveData(false)
    val isRequested: LiveData<Boolean> get() = _isRequested

    private val _messageList = MutableLiveData<List<Message>>(emptyList())
    val messageList: LiveData<List<Message>> get() = _messageList

    init {
        connectionController = ConnectionController(application.applicationContext, this)
    }

    fun startAdvertising() {
        connectionController?.startAdvertising()
    }

    fun startDiscovery() {
        connectionController?.startDiscovery()
    }

    fun stopAdvertising() {
        connectionController?.stopAdvertising()
    }

    fun stopDiscovery() {
        connectionController?.stopDiscovery()
    }

    fun sendConnectionRequest(device: Device) {
        connectionController?.sendConnectionRequest(device)
    }

    fun acceptConnectionRequest(device: Device) {
        connectionController?.acceptConnection(device.deviceId, false)
    }

    fun denyConnectionRequest(device: Device) {
        connectionController?.denyRequest(device)
    }

    fun sendMessage(device: Device, message: String) {
        connectionController?.sendPayload(device.deviceId, message)
    }

    override fun onCleared() {
        connectionController?.stopAdvertising()
        connectionController?.stopDiscovery()
        super.onCleared()
    }

    override fun onIncomingConnection(device: Device) {
        if (_requestList.value?.none { it.deviceId == device.deviceId } == true) {
            _requestList.value = _requestList.value?.plus(device)
            }
    }

    override fun onConnectionStatus(deviceId: String, connectionResolution: ConnectionResolution) {
        if (connectionResolution.status.isSuccess) {
            _connectedDeviceList.value = _connectedDeviceList.value?.plus(_requestList.value?.find { it.deviceId == deviceId }!!)
                _requestList.value = _requestList.value?.filter { it.deviceId != deviceId }
        } else {
            _requestList.value = _requestList.value?.filter { it.deviceId != deviceId }

        }
    }

    override fun onDisconnected(deviceId: String) {
        _requestList.value = _requestList.value?.filter { it.deviceId != deviceId }

    }

    override fun isAdvertisedStarted(isStarted: Boolean) {
        _isAdvertising.value = isStarted
    }

    override fun onDeviceFound(device: Device) {
        if (_discoveredDeviceList.value?.none { it.deviceId == device.deviceId } == true) {
            _discoveredDeviceList.value = _discoveredDeviceList.value?.plus(device)
            }
    }

    override fun onDeviceLost(deviceId: String) {
        _discoveredDeviceList.value = _discoveredDeviceList.value?.filter { it.deviceId != deviceId }
    }

    override fun onDiscoveryStarted(isStarted: Boolean) {
       _isDiscovering.value = isStarted
    }

    override fun onRequestAccepted(deviceId: String, isSender: Boolean) {
        if (isSender) {
            val device = _discoveredDeviceList.value?.find { it.deviceId == deviceId }
            if (device != null) {
                _connectedDeviceList.value = _connectedDeviceList.value?.plus(device)
                _discoveredDeviceList.value = _discoveredDeviceList.value?.filter { it.deviceId != deviceId }
            }
        } else {
            val device = _requestList.value?.find { it.deviceId == deviceId }
            if (device != null) {
                _connectedDeviceList.value = _connectedDeviceList.value?.plus(device)
                _requestList.value = _requestList.value?.filter { it.deviceId != deviceId }
            }
        }
    }

    override fun onRequestDenied(deviceId: String) {
        _requestList.value = _requestList.value?.filter { it.deviceId != deviceId }
    }

    override fun isRequested(isRequested: Boolean) {
        _isRequested.value = isRequested
    }

    override fun onMessageReceived(endPointId: String, message: String) {
        _messageList.value = _messageList.value?.plus(Message(message, _connectedDeviceList.value?.find { it.deviceId == endPointId }!!))
    }

    override fun onMessageSent(message: String) {
        _messageList.value = _messageList.value?.plus(Message(message, Device("", "Me")))
    }

    fun clearData() {
        _requestList.value = emptyList()
        _connectedDeviceList.value = emptyList()
        _discoveredDeviceList.value = emptyList()
        _messageList.value = emptyList()
    }

    fun setUserName(userName: String) {
        connectionController?.setUserName(userName)
    }
}