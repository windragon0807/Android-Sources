package com.ryong.logwindow

import android.util.Log
import com.ryong.logwindow.MainActivity.Companion.instance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogUtil {

    companion object {
        private const val TAG = "LogWindow"

        fun v(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.v(TAG, message)
            CoroutineScope(Dispatchers.Main).launch {
                instance().writeLog(message)
            }
        }

        fun i(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.i(TAG, message)
            CoroutineScope(Dispatchers.Main).launch {
                instance().writeLog(message)
            }
        }

        fun d(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.d(TAG, message)
            CoroutineScope(Dispatchers.Main).launch {
                instance().writeLog(message)
            }
        }

        fun w(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.w(TAG, message)
            CoroutineScope(Dispatchers.Main).launch {
                instance().writeLog(message)
            }
        }

        fun e(tag: String, msg: String) {
            val message = buildLogMsg(tag, msg)
            Log.e(TAG, message)
            CoroutineScope(Dispatchers.Main).launch {
                instance().writeLog(message)
            }
        }

        private val mStringBuffer = StringBuffer()
        private fun buildLogMsg(tag: String, message: String): String {
            val stackTrace = Thread.currentThread().stackTrace[4]
            mStringBuffer.setLength(0)
            mStringBuffer.append("[ $tag ] ")
            mStringBuffer.append("[ ${stackTrace.methodName}() ] ")
            mStringBuffer.append("[ line ${stackTrace.lineNumber} ] ")
            mStringBuffer.append(": $message")
            return mStringBuffer.toString()
        }
    }
}