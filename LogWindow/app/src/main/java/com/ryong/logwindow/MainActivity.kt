package com.ryong.logwindow

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.*
import android.view.animation.DecelerateInterpolator
import com.ryong.logwindow.databinding.ActivityMainBinding
import android.view.ViewTreeObserver.OnGlobalLayoutListener as OnGlobalLayoutListener1

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val container by lazy { binding.container }
    private val button by lazy { binding.btnMainLog }
    private val logWindow by lazy { binding.layoutMainLogWindow }

    private var shortAnimationDuration: Int = 0
    private var currentAnimator: Animator? = null
    private var zoomState: Boolean = false
    private var logWindowX: Float = 0f
    private var logWindowY: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        // Init log window start x, y points
        with (container) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener1 {
                override fun onGlobalLayout() {
                    val containerWidth = width
                    with (logWindow) {
                        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener1 {
                            override fun onGlobalLayout() {
                                logWindowX = ((containerWidth - width) / 2).toFloat()
                                logWindowY = (height / 2).toFloat()
                                viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        })
                    }
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }

        // LogWindow touch and drag move listener
        var moveX = 0f
        var moveY = 0f
        logWindow.setOnTouchListener { view: View, event: MotionEvent ->
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
                    logWindowX = view.x
                    logWindowY = view.y
                }
            }
            true
        }

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    /**
     * View Expansion and Shrink animation.
     * Start from button x, y to current window's x, y
     * Up to window's original scale
     * @param button start animation view
     */
    private fun zoomAnimation(button: View) {
        currentAnimator?.cancel()

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        button.getGlobalVisibleRect(startBoundsInt)
        container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)
        val startScale = startBounds.height() / finalBounds.height()

        logWindow.pivotX = 0f
        logWindow.pivotY = 0f

        when (zoomState) {
            false -> {
                LogUtil.i(TAG, "Animation Open")
                with (logWindow) {
                    visibility = View.VISIBLE
                    bringToFront()
                }
                currentAnimator = AnimatorSet().apply {
                    play(ObjectAnimator.ofFloat(logWindow, View.X, button.x, logWindowX)).apply {  // X 시작 위치, 마지막 위치
                        with(ObjectAnimator.ofFloat(logWindow, View.Y, button.y, logWindowY))  // Y 시작 위치, 마지막 위치
                        with(ObjectAnimator.ofFloat(logWindow, View.SCALE_X, startScale, 1f))  // X 크기
                        with(ObjectAnimator.ofFloat(logWindow, View.SCALE_Y, startScale, 1f))  // Y 크기
                    }
                    duration = shortAnimationDuration.toLong()
                    interpolator = DecelerateInterpolator()
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            currentAnimator = null
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            currentAnimator = null
                        }
                    })
                    start()
                }
                zoomState = true
            }
            true -> {
                LogUtil.i(TAG, "Animation Close")
                currentAnimator = AnimatorSet().apply {
                    play(ObjectAnimator.ofFloat(logWindow, View.X, button.x)).apply {
                        with(ObjectAnimator.ofFloat(logWindow, View.Y, button.y))
                        with(ObjectAnimator.ofFloat(logWindow, View.SCALE_X, startScale))
                        with(ObjectAnimator.ofFloat(logWindow, View.SCALE_Y, startScale))
                    }
                    duration = shortAnimationDuration.toLong()
                    interpolator = DecelerateInterpolator()
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            logWindow.visibility = View.GONE
                            currentAnimator = null
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            logWindow.visibility = View.GONE
                            currentAnimator = null
                        }
                    })
                    start()
                }
                zoomState = false
            }
        }
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.btn_main_log -> {
                zoomAnimation(view)
            }

            R.id.iv_main_minimize_popup -> {
                with (binding) {
                    layoutMainLogWindowBody.visibility = View.GONE
                    ivMainMinimizePopup.visibility = View.GONE
                    ivMainMaximizePopup.visibility = View.VISIBLE
                }
            }

            R.id.iv_main_maximize_popup -> {
                with (binding) {
                    layoutMainLogWindowBody.visibility = View.VISIBLE
                    ivMainMinimizePopup.visibility = View.VISIBLE
                    ivMainMaximizePopup.visibility = View.GONE
                }
            }

            R.id.iv_main_close_popup -> {
                zoomAnimation(button)
            }
        }
    }

    /**
     * Float log messages at log window
     * @param message log message
     */
    fun writeLog(message: String) {
        val totalMsg = "$message\n${binding.tvMainLogWindow.text}"
        with (binding.tvMainLogWindow) {
            text = totalMsg
        }
    }

    init {
        instance = this
    }

    companion object {
        private const val TAG = "MainActivity"
        private lateinit var instance: MainActivity
        fun instance(): MainActivity { return instance }
    }
}