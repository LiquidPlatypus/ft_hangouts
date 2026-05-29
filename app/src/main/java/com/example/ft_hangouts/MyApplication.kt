package com.example.ft_hangouts

import android.app.Activity
import android.app.Application
import android.os.Bundle

class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

	private var startedActivities = 0
	private var wasInBackground = false
	private var pendingBackgroundReturnEvent = false

	override fun onCreate() {
		super.onCreate()
		registerActivityLifecycleCallbacks(this)
	}

	fun consumeBackgroundReturnEvent(): Boolean {
		val event = pendingBackgroundReturnEvent
		pendingBackgroundReturnEvent = false
		if (event) {
			wasInBackground = false
		}
		return event
	}

	override fun onActivityStarted(activity: Activity) {
		if (startedActivities == 0 && wasInBackground) {
			pendingBackgroundReturnEvent = true
		}
		startedActivities++
	}

	override fun onActivityStopped(activity: Activity) {
		startedActivities--
		if (startedActivities <= 0) {
			startedActivities = 0
			if (!activity.isChangingConfigurations) {
				wasInBackground = true
			}
		}
	}

	override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
	override fun onActivityResumed(activity: Activity) = Unit
	override fun onActivityPaused(activity: Activity) = Unit
	override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
	override fun onActivityDestroyed(activity: Activity) = Unit
}