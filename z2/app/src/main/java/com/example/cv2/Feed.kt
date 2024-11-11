package com.example.cv2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.databinding.FragmentFeedBinding

class Feed : Fragment(R.layout.fragment_feed) {
    private lateinit var viewModel: FeedViewModel
    private lateinit var mapViewModel: MapViewModel
    private var binding: FragmentFeedBinding? = null

    val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MapViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[MapViewModel::class.java]

        viewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeedViewModel(DataRepository.getInstance(requireContext())) as T
            }
        })[FeedViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFeedBinding.bind(view).apply {
            lifecycleOwner = viewLifecycleOwner
        }.also { bnd ->

            mapViewModel.lat.observe(viewLifecycleOwner) { lat ->
                mapViewModel.lon.value?.let { lon ->
                        mapViewModel.updateGeofence()
                        viewModel.updateItems()
                }
            }
            mapViewModel.lon.observe(viewLifecycleOwner) { lon ->
                mapViewModel.lat.value?.let { lat ->
                        mapViewModel.updateGeofence()
                        viewModel.updateItems()
                }
            }

            if (hasPermissions(requireContext())) {
                // ziskaj polohu a jebni ju do lat lon
            }


            bnd.feedRecyclerview.layoutManager = LinearLayoutManager(context)
            val feedAdapter = FeedAdapter()
            bnd.feedRecyclerview.adapter = feedAdapter

            viewModel.feedItems.observe(viewLifecycleOwner) { items ->
                Log.d("FeedFragment", "nove hodnoty $items")
                feedAdapter.updateItems(items.filterNotNull() ?: emptyList())
            }

            bnd.pullRefresh.setOnRefreshListener {
                viewModel.updateItems()
            }

            viewModel.loading.observe(viewLifecycleOwner) {
                bnd.pullRefresh.isRefreshing = it
            }

        }
    }

}