package com.amf.amflix.ui.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amf.amflix.repository.series.TVSeriesRepository
import com.amf.amflix.retrofit.models.series.TVSeries

class TvSeriesViewModel: ViewModel() {
    private var tvSeriesRepository: TVSeriesRepository
    private var topTVSeries: LiveData<List<TVSeries>>

    init {
        tvSeriesRepository = TVSeriesRepository()
        topTVSeries = tvSeriesRepository?.topTVSeries()!!
    }

    fun getTopTVSeries(): LiveData<List<TVSeries>>{
        return topTVSeries
    }

}