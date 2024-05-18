package com.amf.amflix.common

class Constants {

    /**
     * Esta clase define constantes que se utilizan en toda la aplicación,
     * como la URL base de la API, la clave de la API,
     * URL para imágenes y otros valores relacionados con la API.
     */

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "e82bf5ed9a851273c254eaad2cfa549b"
        const val IMAGE_ORIGINAL_BASE_URL = "https://image.tmdb.org/t/p/original/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
        const val URL_PARAM_API_KEY = "api_key"
        const val URL_PARAM_LANG = "language"
    }
}