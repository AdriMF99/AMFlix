package com.amf.amflix.ui.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amf.amflix.repository.series.TVSeriesRepository
import com.amf.amflix.retrofit.models.series.TVSeries

class TvSeriesViewModel: ViewModel() {

    private var _series: MutableList<TVSeries> = mutableListOf()
    private var _selected:TVSeries?=null

    val series:List<TVSeries>
        get() = _series.toList()
    var selected:TVSeries?
        get()=_selected
        set(item){ _selected=item}

    private var seriesRepository: TVSeriesRepository
    private var popularSeries: LiveData<List<TVSeries>>
    private var typeSeries: LiveData<List<TVSeries>>

    init {
        seriesRepository = TVSeriesRepository()
        popularSeries = seriesRepository?.popularSeries()!!
        typeSeries = seriesRepository?.getTypeSeries()!!
    }

    fun getPopularSeries(): LiveData<List<TVSeries>>{
        return popularSeries
    }

    fun getTypeSeries(): LiveData<List<TVSeries>>{
        return typeSeries
    }
}