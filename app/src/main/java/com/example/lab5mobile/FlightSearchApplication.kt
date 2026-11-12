package com.example.lab5mobile

import android.app.Application
import com.example.lab5mobile.data.AppContainer
import com.example.lab5mobile.data.DefaultAppContainer

class FlightSearchApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
