package com.example.customlistener

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import com.example.customlistener.MainActivity.Companion.instance

class VolumeObserver(context: Context, handler: Handler?) : ContentObserver(handler) {

    private var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private lateinit var listener: VolumeChangeListener

    override fun onChange(selfChange: Boolean) {
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        LogUtil.i(TAG, "volume : $volume")
        listener.onVolumeChanged(volume.toString())
        instance()?.apply {
            publicMethod()
//            privateMethod()
        }
        super.onChange(selfChange)
    }

    fun interface VolumeChangeListener {
        fun onVolumeChanged(volume: String)
    }

    fun setVolumeChangeListener(listener: VolumeChangeListener) {
        this.listener = listener
    }

    companion object {
        private const val TAG = "VolumeObserver"
    }
}