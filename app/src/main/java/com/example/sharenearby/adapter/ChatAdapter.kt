package com.example.sharenearby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharenearby.R
import com.example.sharenearby.model.Message
import com.google.android.material.textview.MaterialTextView

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messageList: ArrayList<Message> = arrayListOf()

    companion object {
        private const val VIEW_TYPE_LEFT = 0
        private const val VIEW_TYPE_RIGHT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].device.deviceName.contentEquals("Me", true)) {
            VIEW_TYPE_RIGHT
        } else {
            VIEW_TYPE_LEFT
        }
    }

    fun updateData(messages: List<Message>) {
        messageList.clear()
        if(messages.isNotEmpty()) {
            messageList.addAll(messages)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LEFT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
            ChatViewHolderLeft(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_right, parent, false)
            ChatViewHolderRight(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is ChatViewHolderLeft) {
            holder.textViewDeviceName.text = message.device.deviceName
            holder.textViewMessage.text = message.message
        } else if (holder is ChatViewHolderRight) {
            holder.textViewDeviceName.text = message.device.deviceName
            holder.textViewMessage.text = message.message
        }
    }

    class ChatViewHolderLeft(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDeviceName: MaterialTextView = itemView.findViewById(R.id.textViewDeviceName)
        val textViewMessage: MaterialTextView = itemView.findViewById(R.id.textViewMessage)
    }

    class ChatViewHolderRight(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDeviceName: MaterialTextView = itemView.findViewById(R.id.textViewDeviceName)
        val textViewMessage: MaterialTextView = itemView.findViewById(R.id.textViewMessage)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}