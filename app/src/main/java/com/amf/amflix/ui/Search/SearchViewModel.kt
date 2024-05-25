package com.amf.amflix.ui.Search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.series.TVSeries
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<MutableList<Movie>>()
    val searchResults: LiveData<MutableList<Movie>> = _searchResults

    private val _searchResultstv = MutableLiveData<MutableList<TVSeries>>()
    val searchResultstv: LiveData<MutableList<TVSeries>> = _searchResultstv

    init {
        _searchResults.value = mutableListOf()
        _searchResultstv.value = mutableListOf()
    }

    fun search(query: String, page: Int = 1) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        val call = api.searchMoviesAndSeries(Constants.API_KEY, query, page)

        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val mediaItems = response.body()?.results ?: emptyList()
                    _searchResults.value?.addAll(mediaItems)
                    _searchResults.postValue(_searchResults.value)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                // Manejar el error
            }
        })
    }

    fun searchtv(query: String, page: Int = 1) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        val call = api.searchSeries(Constants.API_KEY, query, page)

        call.enqueue(object : Callback<SearchResponsetv> {
            override fun onResponse(call: Call<SearchResponsetv>, response: Response<SearchResponsetv>) {
                if (response.isSuccessful) {
                    val mediaItems = response.body()?.results ?: emptyList()
                    _searchResultstv.value?.addAll(mediaItems)
                    _searchResultstv.postValue(_searchResultstv.value)
                }
            }

            override fun onFailure(call: Call<SearchResponsetv>, t: Throwable) {
                // Manejar el error
            }
        })
    }

    fun clearSearchResults() {
        _searchResults.value?.clear()
        _searchResultstv.value?.clear()
    }
}