package xfy.wl.examfather

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager


class GameActivity : AppCompatActivity() {

    var is_odd: Boolean = true
    private var countDownTimer: ExamTimer? = null
    var IS_EXAM = "is_exam"
    private val viewPager: ViewPager? = null
    private val mainBar: LinearLayout? = null
    private val rightTxt: TextView? = null
    val errorTxt: TextView? = null
    val totalTxt: TextView? = null
    private val timer: TextView? = null
    var isExam: Boolean = false
    private val timerLayout: LinearLayout? = null
    private val back: ImageView? = null
    private val submit: TextView? = null
    private var currentTime = 120000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }

    fun startTimer() {
        countDownTimer = object : ExamTimer(currentTime, 1000) {
            //2700 45分钟
            override fun onTick(l: Long) {
                currentTime = l
                val allSecond = l.toInt() / 1000//秒
                val minute = allSecond / 60
                val second = allSecond - minute * 60
                timer?.text = "倒计时 $minute:$second"
            }

            override fun onFinish() {
                //submitAnswer()
            }
        }
        countDownTimer?.start()
    }

    fun getFourBigVararg(general: String, first: String = "印刷术", second: String = "造纸术", third: String = "火药",
                         fourth: String = "指南针", vararg arrayString: String?): String {
        var ans: String = "$general，$first, $second, $third, $fourth"
        for (item in arrayString) {
            ans = "$ans, $item"
        }
        return ans
    }
}
