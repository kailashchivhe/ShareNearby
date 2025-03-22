package com.example.sharenearby.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharenearby.R
import com.example.sharenearby.adapter.ConnectedDeviceAdapter
import com.example.sharenearby.adapter.DeviceRequestAdapter
import com.example.sharenearby.databinding.FragmentAdvertiserBinding
import com.example.sharenearby.viewmodel.MainViewModel


class AdvertiserFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentAdvertiserBinding
    private lateinit var deviceRequestAdapter: DeviceRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentAdvertiserBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservables()
    }

    private fun initViews() {
        viewBinding.buttonStopAdvertising.setOnClickListener {
            viewBinding.animationView.visibility = View.GONE
            viewModel.stopAdvertising()
        }

        viewBinding.buttonStartAdvertising.setOnClickListener {
            viewBinding.animationView.visibility = View.VISIBLE
            viewBinding.recyclerViewRequestedDevices.visibility = View.GONE
            viewModel.startAdvertising()
        }

        viewBinding.buttonShowConnected.setOnClickListener {
                NavHostFragment.findNavController(this).navigate(R.id.action_advertiserFragment_to_connectedDevicesFragment)
        }

        //Request list
        val linearLayoutManagerRequest =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewRequestedDevices.layoutManager = linearLayoutManagerRequest

        deviceRequestAdapter = DeviceRequestAdapter{ device, isAccepted ->
            if (isAccepted) {
                viewModel.acceptConnectionRequest(device)
            } else {
                viewModel.denyConnectionRequest(device)
            }
        }
        viewBinding.recyclerViewRequestedDevices.adapter = deviceRequestAdapter
    }

    private fun initObservables() {
        viewModel.connectedDeviceList.observe(viewLifecycleOwner) {
            //Update connected devices list
            if (it.isNullOrEmpty()){
                viewBinding.buttonShowConnected.visibility = View.GONE
            }
            else {
                viewBinding.buttonShowConnected.visibility = View.VISIBLE
            }
        }

        viewModel.requestList.observe(viewLifecycleOwner) {
            //Update list
            if (it.isNullOrEmpty()){
                deviceRequestAdapter.updateData(emptyList())
            }
            else {
                viewBinding.animationView.visibility = View.GONE
                viewBinding.recyclerViewRequestedDevices.visibility = View.VISIBLE
                deviceRequestAdapter.updateData(it)
            }
        }

        viewModel.isAdvertising.observe(viewLifecycleOwner) {
            if(it){
                viewBinding.buttonStopAdvertising.visibility = View.VISIBLE
                viewBinding.buttonStartAdvertising.visibility = View.GONE
            }
            else {
                viewBinding.buttonStopAdvertising.visibility = View.GONE
                viewBinding.buttonStartAdvertising.visibility = View.VISIBLE
            }
        }
    }

    override fun onStop() {
        viewModel.stopAdvertising()
        super.onStop()
    }
}