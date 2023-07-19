package com.example.manualtestreport.utils

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.example.manualtestreport.broadcastreceiver.EndInstrumentationBroadcastReceiver
import com.example.manualtestreport.InstrumentationActivity
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class JacocoInstrumentation : Instrumentation() {
    private var mIntent: Intent? = null

    companion object {
        const val TAG = "JacocoInstrumentation:"
        const val COVERAGE_FILE_NAME = "coverage.ec"
        const val INTENT_FILTER = "com.example.manualtestreport.END_INSTRUMENTATION"
    }

    private var coverageFilePath: String = ""


    override fun onCreate(arguments: Bundle?) {
        coverageFilePath = context.filesDir.path.toString() + "/$COVERAGE_FILE_NAME"
        mIntent = Intent(targetContext, InstrumentationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        createFile()
        start()
    }

    private fun createFile() {
        File(coverageFilePath).apply {
            if (!exists()) {
                try {
                    createNewFile()
                } catch (e: Exception) {
                    Log.d(TAG, "File Exception ：${e.stackTraceToString()}")
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Looper.prepare()
        // Register broadcast receiver and start InstrumentActivity
        val activity = startActivitySync(mIntent) as InstrumentationActivity
        activity.apply {
            setInstrumentationListener(onActivityEndListener)
            registerReceiver(EndInstrumentationBroadcastReceiver().apply {
                setInstrumentationListener(
                    onActivityEndListener
                )
            }, IntentFilter(INTENT_FILTER))
        }
        Log.d(TAG, "EndEmmaBroadcast registered:::")
    }

    private fun onActivityEnd() {
        Log.d(TAG, "onActivityFinished()")
        generateCoverageReport()
        finish(Activity.RESULT_OK, Bundle())
    }

    private fun generateCoverageReport() {
        Log.d(TAG, "generateCoverageReport(): $coverageFilePath")
        var out: OutputStream? = null
        try {
            out = FileOutputStream(coverageFilePath, true)
            val agent = Class.forName("org.jacoco.agent.rt.RT").getMethod("getAgent").invoke(null)
            out.write(
                agent.javaClass.getMethod("getExecutionData", Boolean::class.javaPrimitiveType)
                    .invoke(agent, false) as ByteArray
            )
        } catch (e: Exception) {
            Log.d(TAG, e.toString(), e)
        } finally {
            out?.close()
        }
    }

    private val onActivityEndListener: () -> Unit = {
        onActivityEnd()
    }
}