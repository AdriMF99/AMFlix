package com.amf.amflix.ui.Search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.movies.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    fun search(query: String) {
        // Aquí harías la llamada a tu API para buscar las películas y series
        // Ejemplo de llamada usando Retrofit:
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        val call = api.searchMoviesAndSeries(Constants.API_KEY, query)

        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    _searchResults.postValue(response.body()?.results)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                // Manejar el error
            }
        })
    }
}
