package com.bado.ignacio.statesaver

import android.app.Activity
import android.app.Application
import android.os.Bundle

object ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) {
        // do nothing
    }

    override fun onActivityStarted(activity: Activity) {
        // do nothing
    }

    override fun onActivityDestroyed(activity: Activity) {
        // do nothing
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        StateSaver.saveState(activity, outState)
    }

    override fun onActivityStopped(activity: Activity) {
        // do nothing
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            StateSaver.restoreState(activity, savedInstanceState)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        // do nothing
    }
}