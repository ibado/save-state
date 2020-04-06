package com.bado.ignacio.statesaver

import android.app.Activity
import android.app.Application
import android.os.Bundle

object StateSaver {

    private const val SAVER_SUFFIX = "_Saver"

    @JvmStatic
    fun saveActivitiesState(application: Application) {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks)
    }

    fun <T : Activity> saveState(target: T, bundle: Bundle) {
        val name = target.javaClass.canonicalName + SAVER_SUFFIX
        try {
            val saver = Class.forName(name).newInstance() as Saver<T>
            saver.save(target, bundle)
        } catch (exception: ClassNotFoundException) {
            // there is no @Safe fields in this activity
        }
    }

    fun <T : Activity> restoreState(target: T, bundle: Bundle) {
        val name = target.javaClass.canonicalName + SAVER_SUFFIX
        try {
            val saver = Class.forName(name).newInstance() as Saver<T>
            saver.restore(target, bundle)
        } catch (exception: ClassNotFoundException) {
            // there is no @Safe fields in this activity
        }
    }
}
