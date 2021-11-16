package com.example.customlistener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import com.example.customlistener.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), VolumeObserver.VolumeChangeListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val volumeObserver by lazy { VolumeObserver(this, Handler()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            volumeObserver
        )
        volumeObserver.setVolumeChangeListener(this)
    }

    override fun onVolumeChanged(volume: String) {
        binding.tvMain.text = volume
        privateMethod()
        publicMethod()
    }

    init {
        instance = this
    }

    private fun privateMethod() {
        LogUtil.d(TAG, "Private Method")
    }

    fun publicMethod() {
        LogUtil.d(TAG, "Public Method")
    }

    companion object {
        private const val TAG = "MainActivity"
        private var instance: MainActivity? = null
        fun instance(): MainActivity? { return instance }
    }
}