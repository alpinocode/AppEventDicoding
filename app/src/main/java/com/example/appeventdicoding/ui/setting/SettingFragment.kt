package com.example.appeventdicoding.ui.setting

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.appeventdicoding.R
import com.example.appeventdicoding.databinding.FragmentSettingBinding
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var workManager: WorkManager
    private var binding: FragmentSettingBinding? = null
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if(isGranted) {
            Toast.makeText(requireContext(), "Notification permission Accept", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Nofitication permission is Rejected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val switchDarkMode = binding?.switchDarkmode

        val pref = SettingPrefenrece.getInstance(requireContext().dataStore)

        val settingViewModel = ViewModelProvider(this, ViewModelFactorySetting(pref)).get(
            SettingViewModel::class.java
        )



        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkMode ->
            if(isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchDarkMode?.isChecked = true
            }  else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchDarkMode?.isChecked = false
            }
        }
        switchDarkMode?.setOnCheckedChangeListener { _:CompoundButton, isChecked:Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }



        if(Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        workManager = WorkManager.getInstance(requireContext())


        // menangani switch material
        binding?.permissionNotification?.setOnCheckedChangeListener{ _:CompoundButton, isChecked:Boolean ->
            if(isChecked) {
                startOnTimeTask()
            } else {
                cancelPeriodTask()
            }
        }
    }


    private fun startOnTimeTask() {
        val data = Data.Builder()
            .putString(SettingWorker.EXTRA_EVENT, "Event Dicoding")
            .build()

        val constaints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // melakukan pekerjaan  yang berulang dalam rentang 1 hari
        periodicWorkRequest = PeriodicWorkRequest.Builder(SettingWorker::class.java, 1, TimeUnit.DAYS)
            .setInputData(data)
            .setConstraints(constaints)
            .build()

        workManager.enqueue(periodicWorkRequest)
    }

    private fun cancelPeriodTask() {
        workManager.cancelWorkById(periodicWorkRequest.id)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }




}