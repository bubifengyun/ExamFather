package xfy.wl.examfather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        result_back.setOnClickListener{ this.finish()}

        var rightAnswer = 0
        for (exam in ExamDBHelper.examInfoList) {
            if (exam.isRightChoice)
                rightAnswer++
        }

        if (rightAnswer < 80) {
            result_result.text = "马路杀手"
        } else if (rightAnswer >= 80 && rightAnswer < 90) {
            result_result.text = "碾压一切"
        } else {
            result_result.text = "秋名山上行人稀，\n常有车神较高低,\n如今车牌依旧在,\n不见当年老司机。\n    ----秋名山车神"
        }
    }
}
