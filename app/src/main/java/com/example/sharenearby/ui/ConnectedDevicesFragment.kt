package com.example.sharenearby.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharenearby.R
import com.example.sharenearby.adapter.ConnectedDeviceAdapter
import com.example.sharenearby.databinding.FragmentConnectedDevicesBinding
import com.example.sharenearby.databinding.FragmentDiscovererBinding
import com.example.sharenearby.viewmodel.MainViewModel


class ConnectedDevicesFragment : Fragment() {

    private lateinit var viewBinding: FragmentConnectedDevicesBinding
    private lateinit var connectedDeviceAdapter: ConnectedDeviceAdapter
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding =
            FragmentConnectedDevicesBinding.inflate(layoutInflater, container, false).apply {
                lifecycleOwner = viewLifecycleOwner
            }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservables()
    }

    private fun initObservables() {
        viewModel.connectedDeviceList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                connectedDeviceAdapter.updateData(emptyList())
            } else {
                connectedDeviceAdapter.updateData(it)
            }
        }
    }

    private fun initViews() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewConnectedDevices.layoutManager = linearLayoutManager
        connectedDeviceAdapter = ConnectedDeviceAdapter() {
            //Navigate to chat fragment
            val bundle = Bundle().apply {
                putParcelable("device", it)
            }
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_connectedDevicesFragment_to_chatFragment, bundle)
        }
        viewBinding.recyclerViewConnectedDevices.adapter = connectedDeviceAdapter
    }
}