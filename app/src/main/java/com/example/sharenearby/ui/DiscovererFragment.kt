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
import com.example.sharenearby.adapter.DiscoveredDeviceAdapter
import com.example.sharenearby.databinding.FragmentDiscovererBinding
import com.example.sharenearby.viewmodel.MainViewModel


class DiscovererFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentDiscovererBinding
    private lateinit var deviceRequestAdapter: DiscoveredDeviceAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding =
            FragmentDiscovererBinding.inflate(layoutInflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservables()
    }

    private fun initViews() {
        viewBinding.buttonStopDiscovering.setOnClickListener {
            viewBinding.animationView.visibility = View.GONE
            viewModel.stopDiscovery()
        }

        viewBinding.buttonStartDiscovering.setOnClickListener {
            viewBinding.animationView.visibility = View.VISIBLE
            viewBinding.recyclerViewDiscoveredDevices.visibility = View.GONE
            viewModel.startDiscovery()
        }

        viewBinding.buttonShowConnected.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_discovererFragment_to_connectedDevicesFragment)
        }

        //discovered list
        val linearLayoutManagerRequest =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewDiscoveredDevices.layoutManager = linearLayoutManagerRequest
        deviceRequestAdapter = DiscoveredDeviceAdapter{ device ->
            viewModel.sendConnectionRequest(device)
        }
        viewBinding.recyclerViewDiscoveredDevices.adapter = deviceRequestAdapter
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

        viewModel.discoveredDeviceList.observe(viewLifecycleOwner) {
            //Update list
            if (it.isNullOrEmpty()){
                deviceRequestAdapter.updateData(emptyList())
            }
            else {
                viewBinding.animationView.visibility = View.GONE
                viewBinding.recyclerViewDiscoveredDevices.visibility = View.VISIBLE
                deviceRequestAdapter.updateData(it)
            }
        }

        viewModel.isDiscovering.observe(viewLifecycleOwner) {
            if(it){
                viewBinding.buttonStopDiscovering.visibility = View.VISIBLE
                viewBinding.buttonStartDiscovering.visibility = View.GONE
            }
            else {
                viewBinding.buttonStopDiscovering.visibility = View.GONE
                viewBinding.buttonStartDiscovering.visibility = View.VISIBLE
            }
        }
    }

    override fun onStop() {
        viewModel.stopDiscovery()
        super.onStop()
    }
}