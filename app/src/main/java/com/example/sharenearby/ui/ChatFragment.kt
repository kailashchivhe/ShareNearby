package com.example.sharenearby.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharenearby.adapter.ChatAdapter
import com.example.sharenearby.databinding.FragmentChatBinding
import com.example.sharenearby.model.Device
import com.example.sharenearby.viewmodel.MainViewModel


class ChatFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter

    //get device from arguments
    private val device by lazy {
        arguments?.getParcelable<Device>("device")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentChatBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.buttonSend.setOnClickListener {
            val message = viewBinding.editTextMessage.text.toString()
            viewBinding.editTextMessage.text?.clear()
            if(device != null) {
                viewModel.sendMessage(device!!, message)
            }
        }

        chatAdapter = ChatAdapter()
        viewBinding.recyclerViewChatLog.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }

        viewModel.messageList.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                chatAdapter.updateData(it)
                viewBinding.recyclerViewChatLog.smoothScrollToPosition(it.size - 1)
            }
        }
    }
}