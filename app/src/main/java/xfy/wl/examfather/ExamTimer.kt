package xfy.wl.examfather


import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.os.SystemClock

/**
 * Created by Administrator on 2017/2/13.
 */

abstract class ExamTimer(val mMillisInFuture: Long, val mCountdownInterval: Long) {

    private var mStopTimeInFuture: Long = 0

    /**
     * boolean representing if the timer was cancelled
     */
    private var mCancelled = false


    // handles counting down
    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {

            synchronized(this@ExamTimer) {
                if (mCancelled) {
                    return
                }

                val millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime()

                if (millisLeft < 0) {
                    onFinish()
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft)
                } else {
                    val lastTickStart = SystemClock.elapsedRealtime()
                    onTick(millisLeft)

                    // take into account user's onTick taking time to execute
                    var delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime()

                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval

                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }
    }

    /**
     * Cancel the countdown.
     */
    @Synchronized
    fun cancel() {
        mCancelled = true
        mHandler.removeMessages(MSG)
    }

    /**
     * Start the countdown.
     */
    @Synchronized
    fun start(): ExamTimer {
        mCancelled = false
        if (mMillisInFuture < 0) {
            onFinish()
            return this
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        return this
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    abstract fun onTick(millisUntilFinished: Long)

    /**
     * Callback fired when the time is up.
     */
    abstract fun onFinish()

    companion object {
        private val MSG = 1
    }
}
