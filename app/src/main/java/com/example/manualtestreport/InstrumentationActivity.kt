package com.example.manualtestreport

open class InstrumentationActivity : MainActivity() {

    private var onActivityEndListener: (() -> Unit?)? = null

    fun setInstrumentationListener(onActivityEndListener: () -> Unit) {
        this.onActivityEndListener = onActivityEndListener
    }

    override fun onDestroy() {
        super.onDestroy()
        super.finish()
        onActivityEndListener?.invoke()
    }
}