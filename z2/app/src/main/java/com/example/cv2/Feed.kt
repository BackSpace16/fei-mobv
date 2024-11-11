package com.example.cv2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.databinding.FragmentFeedBinding
import com.google.android.material.snackbar.Snackbar

class Feed : Fragment(R.layout.fragment_feed) {
    private lateinit var viewModel: FeedViewModel
    private var binding: FragmentFeedBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.feed_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val feedAdapter = FeedAdapter()
        recyclerView.adapter = feedAdapter

        feedAdapter.run {
            recyclerView.adapter = this

            updateItems(listOf(
                MyItem("1 osoba"),
                MyItem("2 osoba"),
                MyItem("3 osoba"),
                MyItem("4 osoba"),
                MyItem("5 osoba"),
                MyItem("7 osoba"),
                MyItem("6 osoba"),
            ))
        }

        val snackbar = Snackbar.make(requireActivity().findViewById(R.id.feed), "VYHRAJTE IPHONE 17!!!", Snackbar.LENGTH_LONG)
        snackbar.setAction("IHNEĎ!!!") {
            performAction()
        }
        snackbar.show()
    }

    private fun performAction() {
        Toast.makeText(context, "smola, skúste štastie nabudúce", Toast.LENGTH_SHORT).show()
    }*/
}