package com.example.test

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LogUtil {
    companion object {
        private val TAG = this.javaClass.simpleName

        var job: Job? = null

        fun v(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.v(TAG, message)
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                MainActivity.getInstance()?.writeWindow(message)
            }
        }

        fun i(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.v(TAG, message)
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                MainActivity.getInstance()?.writeWindow(message)
            }

        }

        fun d(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.v(TAG, message)
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                MainActivity.getInstance()?.writeWindow(message)
            }
        }

        fun w(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.v(TAG, message)
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                MainActivity.getInstance()?.writeWindow(message)
            }
        }

        fun e(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.v(TAG, message)
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                MainActivity.getInstance()?.writeWindow(message)
            }
        }

        private val mStringBuffer = StringBuffer()
        private fun buildLogMsg(tag: String, message: String): String {
            val stackTrace = Thread.currentThread().stackTrace[4]
            mStringBuffer.setLength(0)
            mStringBuffer.append("[ $tag ]")
            mStringBuffer.append(" [ line ${stackTrace.lineNumber} ]")
            mStringBuffer.append(" : $message")
            return mStringBuffer.toString()
        }
    }
}