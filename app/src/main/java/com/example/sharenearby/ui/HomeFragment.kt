package com.example.sharenearby.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.sharenearby.R
import com.example.sharenearby.databinding.FragmentHomeBinding
import com.example.sharenearby.model.Constants
import com.example.sharenearby.viewmodel.MainViewModel
import com.google.android.material.textfield.TextInputEditText


class HomeFragment : Fragment() {
    private var viewBinding: FragmentHomeBinding? = null

    private lateinit var alertDialog: AlertDialog

    private val viewModel: MainViewModel by activityViewModels()

    private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                Toast.makeText(
                    requireContext(),
                    "Permission denied: ${it.key}",
                    Toast.LENGTH_LONG
                ).show()
                return@registerForActivityResult
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermissions()

        viewBinding?.buttonCreateGroup?.setOnClickListener {
            showUserNameDialog(true)
        }

        viewBinding?.buttonJoinGroup?.setOnClickListener {
            showUserNameDialog(false)
        }
    }

    private fun checkPermissions() {
        if (!hasPermissions(Constants.REQUIRED_PERMISSIONS)) {
            requestPermissionLauncher.launch(Constants.REQUIRED_PERMISSIONS)
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun showUserNameDialog(isAdvertiser: Boolean) {
        val title = "Enter User Name"
        val okButtonLabel =  "Ok"
        val cancelButtonLabel =   "Cancel"
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.alert_input_view, null)
        val editText = view.findViewById<TextInputEditText?>(R.id.editText)
        editText.doAfterTextChanged {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                it.toString().isNotEmpty()
        }
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.apply {
            setTitle(title)
            setView(view)
            setPositiveButton(okButtonLabel) { _, _ ->
                editText.text?.toString()?.let { newTitle ->
                    if (newTitle.isNotEmpty()) {
                        viewModel.setUserName(newTitle)
                        if (isAdvertiser) {
                            NavHostFragment.findNavController(this@HomeFragment)
                                .navigate(R.id.action_homeFragment_to_advertiserFragment)
                        } else {
                            NavHostFragment.findNavController(this@HomeFragment)
                                .navigate(R.id.action_homeFragment_to_discovererFragment)
                        }
                    }
                }
            }
            setNegativeButton(cancelButtonLabel) { dialog, _ ->
                dialog.dismiss()
            }
        }
        alertDialog = builder.create()
        alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        alertDialog.setOnShowListener {
            editText?.requestFocus()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = true
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.isEnabled = true
        }
        alertDialog.show()
    }
}