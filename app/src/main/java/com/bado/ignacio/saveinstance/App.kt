package com.bado.ignacio.saveinstance

import android.app.Application
import com.bado.ignacio.statesaver.StateSaver

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        StateSaver.saveActivitiesState(this)
    }
}