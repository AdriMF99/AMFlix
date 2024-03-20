package com.amf.amflix.common

import android.app.Application

class App: Application() {

    /**
     * Esta clase extiende Application y se utiliza para realizar tareas de inicialización de nivel de aplicación,
     * como configurar componentes de aplicación, definir instancias globales y más.
     */

    companion object{
        lateinit var instance: App
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}