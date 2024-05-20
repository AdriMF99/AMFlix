package com.amf.amflix.retrofit.models.series

import com.amf.amflix.common.Constants
import okhttp3.Interceptor
import okhttp3.Response

class TVSeriesInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.url.toString().contains("tv/popular")) {
            // Si la solicitud es para obtener detalles de una película popular, solicita los detalles en español
            val urlWithParams = request.url.newBuilder()
                .addQueryParameter(Constants.URL_PARAM_API_KEY, Constants.API_KEY)
                .addQueryParameter(Constants.URL_PARAM_LANG, "es-ES")
                .build()

            request = request.newBuilder()
                .url(urlWithParams)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
        } else {
            // En caso contrario, solicita el tagline en inglés
            val urlWithParams = request.url.newBuilder()
                .addQueryParameter(Constants.URL_PARAM_API_KEY, Constants.API_KEY)
                .addQueryParameter(Constants.URL_PARAM_LANG, "en-EN")
                .build()

            request = request.newBuilder()
                .url(urlWithParams)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
        }

        return chain.proceed(request)
    }
}