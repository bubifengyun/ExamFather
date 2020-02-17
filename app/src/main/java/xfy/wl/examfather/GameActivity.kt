package xfy.wl.examfather

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.alert


class GameActivity : AppCompatActivity() {

    var is_odd: Boolean = true
    private var countDownTimer: ExamTimer? = null
    var IS_EXAM = "is_exam"
    private val viewPager: ViewPager? = null
    var isExam: Boolean = false
    private val timerLayout: LinearLayout? = null
    private var currentTimeMS = 45 * 60_000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        main_bar.setOnClickListener(this::onClickOpenBottomSheetDialog)
        question_back.setOnClickListener(this::onClickBackHandler)
        //viewPager.setAdapter(MyPagerAdapter(supportFragmentManager))
        //viewPager.setOnPageChangeListener(MyPagerChangeListner())

        isExam = intent.getBooleanExtra(IS_EXAM, true)

        if (!isExam) {//如果不是考试,就隐藏
            question_timer.visibility = View.GONE
            question_submit.visibility = View.GONE
        }

        question_submit.setOnClickListener(this::onClickBackHandler)
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : ExamTimer(currentTimeMS, 1000) {
            //2700 45分钟
            override fun onTick(l: Long) {
                currentTimeMS = l
                val allSecond = l.toInt() / 1000//秒
                val minute = allSecond / 60
                val second = allSecond - minute * 60
                question_countdown.text = "倒计时 $minute:$second"
            }

            override fun onFinish() {
                submitAnswer()
            }
        }
        countDownTimer?.start()
    }

    var bottomSheetDialog: BottomSheetDialog? = null

    private fun onClickOpenBottomSheetDialog(view: View) {
        bottomSheetDialog = BottomSheetDialog(this)
        val gridView = GridView(this)
        gridView.numColumns = 6
        gridView.verticalSpacing = 10
        gridView.horizontalSpacing = 10
        gridView.setBackgroundColor(-0x1)
        //gridView.adapter = AnswerAdapter()
        gridView.scrollBarStyle = GridView.SCROLLBARS_OUTSIDE_INSET
        gridView.setPadding(20, 20, 20, 20)
        bottomSheetDialog?.setContentView(gridView)
        bottomSheetDialog?.show()
        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                viewPager?.setCurrentItem(position, false)
                bottomSheetDialog?.dismiss()
            }
    }

    //提交答案
    fun submitAnswer() {
        //startActivity(Intent(this, ResultActivity::class.java))
        finish()
    }

    private fun onClickBackHandler(view: View) {
        var finishAnswer = 0
        var rightAnswer = 0
//        for (r in Question.result) {
//            if (r.isChooseResult())
//                rightAnswer++
//            if (r.isFinishAnswer())
//                finishAnswer++
//        }

        val message = "您已回答了" + finishAnswer + "题(共100题),考试得分" + rightAnswer + ",确定交卷？"
        alert(message, "温馨提示") {
            positiveButton("确定交卷") {
                submitAnswer()
            }
            negativeButton("直接返回") {
                this@GameActivity.finish()
            }
        }.show()
    }
}
