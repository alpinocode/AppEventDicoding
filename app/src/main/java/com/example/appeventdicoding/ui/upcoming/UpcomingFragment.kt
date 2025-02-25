package com.example.appeventdicoding.ui.upcoming

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appeventdicoding.R
import com.example.appeventdicoding.data.remote.response.ListEventsItem
import com.example.appeventdicoding.databinding.FragmentUpcomingBinding
import com.example.appeventdicoding.ui.detail.DetailActivity
import com.example.eventdicoding.ui.Adapter.EventDicodingAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpcomingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpcomingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentUpcomingBinding? = null

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
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        viewModel.listEventUpcomingDicoding.observe(viewLifecycleOwner) {
            showDataUpcomingEvent(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showDataUpcomingEvent(event: List<ListEventsItem>) {
        val eventAdapter = EventDicodingAdapter(event)
        binding?.rvUpcomingEvent?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvUpcomingEvent?.adapter = eventAdapter
        eventAdapter.submitList(event)

        eventAdapter.setOnItemClickCallback(object : EventDicodingAdapter.OnItemClickCallback {
            override fun onItemCallback(data: ListEventsItem) {
                val contextUpcoming = binding?.root?.context
                val intent = Intent(contextUpcoming, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EVENT_ID, data.id)
                contextUpcoming?.startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding?.progresbarUpcoming?.visibility = View.VISIBLE
        } else {
            binding?.progresbarUpcoming?.visibility = View.GONE
        }
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
         * @return A new instance of fragment UpcomingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpcomingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}