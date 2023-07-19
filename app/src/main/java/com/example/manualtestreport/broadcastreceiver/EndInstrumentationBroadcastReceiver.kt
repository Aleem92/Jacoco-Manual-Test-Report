package com.example.manualtestreport.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Process

// adb shell am broadcast -a com.example.manualtestreport.END_INSTRUMENTATION

class EndInstrumentationBroadcastReceiver : BroadcastReceiver() {

    private var onActivityEndListener: (() -> Unit?)? = null

    fun setInstrumentationListener(onActivityEndListener: () -> Unit) {
        this.onActivityEndListener = onActivityEndListener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        onActivityEndListener?.invoke()
        Process.killProcess(Process.myPid())
    }

}