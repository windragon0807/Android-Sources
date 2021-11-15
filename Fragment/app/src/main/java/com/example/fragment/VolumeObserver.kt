package com.example.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler

class VolumeObserver(context: Context, handler: Handler?, private val listener: VolumeChangeListener) : ContentObserver(handler) {

    private var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onChange(selfChange: Boolean) {
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        LogUtil.i(TAG, "volume : $volume")
        listener.onVolumeChanged(volume.toString())
        super.onChange(selfChange)
    }

    companion object {
        private const val TAG = "VolumeObserver"
    }
}