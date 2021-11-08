package com.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.fragment.databinding.FragmentMainBinding

class MainFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setOnClickListener()
        return binding.root
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

    companion object {
        fun instance() = MainFragment()
    }
}