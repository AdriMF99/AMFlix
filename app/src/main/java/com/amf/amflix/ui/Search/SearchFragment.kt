package com.amf.amflix.ui.Search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R

class SearchFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchView = view.findViewById(R.id.searchView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        adapter = SearchAdapter()
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModel.searchResults.observe(viewLifecycleOwner, { results ->
            adapter.setItems(results)
        })

        setupSearchView()

        return view
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    viewModel.search(newText)
                } else {
                    adapter.setItems(emptyList())
                }
                return true
            }
        })
    }
}