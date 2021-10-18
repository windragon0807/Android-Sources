package com.ryong.logutil_logwindow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.ryong.logutil_logwindow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        listenerInit()
    }

    private fun listenerInit() {
        var moveX = 0f
        var moveY = 0f
        binding.layoutMainLogWindow.setOnTouchListener { view: View, event: MotionEvent ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveX = view.x - event.rawX
                    moveY = view.y - event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    view.animate()
                        .x(event.rawX + moveX)
                        .y(event.rawY + moveY)
                        .setDuration(0)
                        .start()
                }
            }
            true
        }
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.btn_main_log -> {
                binding.layoutMainLogWindow.visibility = View.VISIBLE
                LogUtil.i(TAG, "button clicked")
            }

            R.id.iv_main_minimize_popup -> {
                binding.layoutMainLogWindowBody.visibility = View.GONE
                binding.ivMainMinimizePopup.visibility = View.GONE
                binding.ivMainMaximizePopup.visibility = View.VISIBLE
            }

            R.id.iv_main_maximize_popup -> {
                binding.layoutMainLogWindowBody.visibility = View.VISIBLE
                binding.ivMainMinimizePopup.visibility = View.VISIBLE
                binding.ivMainMaximizePopup.visibility = View.GONE
            }

            R.id.iv_main_close_popup -> {
                binding.layoutMainLogWindow.visibility = View.GONE
            }
        }
    }

    fun writeWindow(msg: String) {
        var totalMsg = "$msg\n${binding.tvMainLogWindow.text}"
        with (binding.tvMainLogWindow) {
            text = totalMsg
        }
    }

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null
        fun getInstance(): MainActivity? { return instance }
    }
}