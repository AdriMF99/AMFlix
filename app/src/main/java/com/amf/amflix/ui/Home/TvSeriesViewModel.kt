package com.amf.amflix.ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amf.amflix.repository.series.TVSeriesRepository
import com.amf.amflix.retrofit.models.series.TVSeries
import kotlinx.coroutines.launch

class TvSeriesViewModel : ViewModel() {

    private val tvSeriesRepository = TVSeriesRepository()

    private val _popularTvShows = MutableLiveData<List<TVSeries>>()
    val popularTvShows: LiveData<List<TVSeries>> get() = _popularTvShows

    private val _topRatedTvShows = MutableLiveData<List<TVSeries>>()
    val topRatedTvShows: LiveData<List<TVSeries>> get() = _topRatedTvShows

    private val _trendingTvShows = MutableLiveData<List<TVSeries>>()
    val trendingTvShows: LiveData<List<TVSeries>> get() = _trendingTvShows

    var selectedTvShow: TVSeries? = null

    init {
        fetchPopularTvShows()
        fetchTopRatedTvShows()
        fetchTrendingTvShows()
    }

    private fun fetchPopularTvShows() {
        tvSeriesRepository.getPopularSeries().observeForever { tvShows ->
            _popularTvShows.postValue(tvShows)
        }
    }

    private fun fetchTopRatedTvShows() {
        tvSeriesRepository.getTopRatedTvShows().observeForever { tvShows ->
            _topRatedTvShows.postValue(tvShows)
        }
    }

    private fun fetchTrendingTvShows() {
        tvSeriesRepository.getTrendingTvShows().observeForever { tvShows ->
            _trendingTvShows.postValue(tvShows)
        }
    }
}