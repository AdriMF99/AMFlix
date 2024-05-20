package com.amf.amflix.ui.series

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.series.TVSeries

class TVSeriesListFragment : Fragment() {

    private lateinit var seriesViewModel: TvSeriesViewModel
    private lateinit var seriesAdapter: TVSeriesRecyclerViewAdapter
    private var popularSeries: List<TVSeries> = ArrayList()
    private var columnCount = 1
    private val seriesviewmodel: TvSeriesViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicialización de componentes
        val view = inflater.inflate(R.layout.fragment_tv_series_item_list, container, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(view as RecyclerView?)
        seriesViewModel = ViewModelProvider(this).get(TvSeriesViewModel::class.java)

        // Creación del adaptador
        seriesAdapter = TVSeriesRecyclerViewAdapter(this.seriesviewmodel.series)
        seriesAdapter.click = { position, serie ->
            run {
                this.seriesviewmodel.selected = serie
                val navController = findNavController()
                navController.navigate(R.id.navigation_tvdetails)
            }
        }

        // Configuración del RecyclerView
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = seriesAdapter
            }
        }

        // Obtención de datos de la capa de datos
        seriesViewModel.getPopularSeries().observe(viewLifecycleOwner, Observer {
            popularSeries = it
            // Actualización del adaptador
            seriesAdapter.setData(popularSeries)
        })

        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance(columnCount: Int) =
            TVSeriesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}