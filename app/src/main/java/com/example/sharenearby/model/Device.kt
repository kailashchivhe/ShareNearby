package com.example.sharenearby.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(var deviceId: String,
                  var deviceName: String): Parcelable

data class Message(var message: String,
                   var device: Device)
