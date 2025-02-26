package com.example.appeventdicoding.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appeventdicoding.data.remote.response.ListEventsItem
import com.example.appeventdicoding.databinding.FragmentHomeBinding
import com.example.appeventdicoding.ui.detail.DetailActivity
import com.example.appeventdicoding.ui.setting.SettingPrefenrece
import com.example.appeventdicoding.ui.setting.SettingViewModel
import com.example.appeventdicoding.ui.setting.ViewModelFactorySetting
import com.example.appeventdicoding.ui.setting.dataStore
import com.example.eventdicoding.ui.Adapter.EventDicodingAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val prefHome = SettingPrefenrece.getInstance(requireContext().dataStore)

        val settingViewModel = ViewModelProvider(this, ViewModelFactorySetting(prefHome)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner, { isDarkMode ->
            if (isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })

        viewModel.listEventsFinished.observe(viewLifecycleOwner) {
            setListEventData(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun setListEventData(event: List<ListEventsItem>) {
        val eventAdapter =EventDicodingAdapter(event)
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvent.adapter = eventAdapter
        eventAdapter.submitList(event)

        eventAdapter.setOnItemClickCallback(object : EventDicodingAdapter.OnItemClickCallback {
            override fun onItemCallback(data: ListEventsItem) {
                val context = binding.root.context
                val intent = Intent(context,DetailActivity::class.java )
                intent.putExtra(DetailActivity.EVENT_ID, data.id)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading:Boolean) {
        if(isLoading) {
            binding.progresbarFinished.visibility = View.VISIBLE
        } else {
            binding.progresbarFinished.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}