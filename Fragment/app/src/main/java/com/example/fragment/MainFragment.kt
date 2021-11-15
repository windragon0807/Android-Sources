package com.example.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.fragment.databinding.FragmentMainBinding

class MainFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentMainBinding
    private val volumeObserver by lazy { VolumeObserver(requireContext(), Handler(), listener) }

    private val listener =
        VolumeChangeListener { volume -> binding.tvFragmentMain.text = volume }
//    private val listener = object : VolumeChangeListener {
//        override fun onVolumeChanged(volume: String) {
//            binding.tvFragmentMain.text = volume
//        }
//    }

    private fun testCallback(callback: ((String) -> Int)) {
        callback("String")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        setOnClickListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            volumeObserver
        )
    }

    private fun setOnClickListener() {
        val btnSequence = binding.containerMain.children
        btnSequence.forEach { btn ->
            btn.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_fragment_main -> {
                Toast.makeText(activity, "hello", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    override fun onVolumeChanged(volume: String) {
//        binding.tvFragmentMain.text = volume
//    }

    companion object {
        fun instance() = MainFragment()
    }
}