package com.example.appeventdicoding.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appeventdicoding.data.remote.response.ListEventsItem
import com.example.appeventdicoding.databinding.FragmentFavoriteBinding
import com.example.appeventdicoding.ui.detail.DetailActivity
import com.example.appeventdicoding.ui.viewModelFactory.ViewModelFactory
import com.example.eventdicoding.ui.Adapter.EventDicodingAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentFavoriteBinding? = null

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
       binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Favorite"

        val factory:ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel: FavoriteViewModel by viewModels<FavoriteViewModel> {
            factory
        }

        viewModel.getFavoriteEvent().observe(viewLifecycleOwner, { result ->
            if (result == null) {
                binding?.progresbarFavorite?.visibility = View.GONE
                binding?.titleNotFound?.visibility = View.VISIBLE
                binding?.itemBookmarkNotFound?.visibility = View.VISIBLE
            } else {
                binding?.progresbarFavorite?.visibility = View.GONE
                val itemsEvent = result.map { event ->
                    ListEventsItem(
                        id = event.id,
                        name = event.nameEvent,
                        imageLogo = event.image,
                        beginTime = event.publishedAt
                    )
                }
                sumbmitList(itemsEvent)
            }

        })
    }

    private fun sumbmitList(event:List<ListEventsItem>) {
        val eventAdapter = EventDicodingAdapter(event)
        binding?.rvFavoriteEvent?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvFavoriteEvent?.adapter = eventAdapter
        eventAdapter.submitList(event)

        eventAdapter.setOnItemClickCallback(object : EventDicodingAdapter.OnItemClickCallback {
            override fun onItemCallback(data: ListEventsItem) {
                val context = binding?.root?.context
                val intent = Intent(context, DetailActivity::class.java )
                intent.putExtra(DetailActivity.EVENT_ID, data.id)
                startActivity(intent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val TAG = "Favorite_Fragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}