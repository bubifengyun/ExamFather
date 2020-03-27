package xfy.wl.examfather

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import xfy.wl.examfather.storage.AppPreferences
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var examDBHelper: ExamDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        examDBHelper = ExamDBHelper.getInstance(this)

        button_new_game.setOnClickListener(this::onClickNewGame)
        button_error_history.setOnClickListener(this::onClickErrorHistory)
        button_law_sets.setOnClickListener(this::onClickLawSets)
        button_clear_score.setOnClickListener(this::onClickClearScore)
        button_exit.setOnClickListener(this::onClickExit)
    }

    private fun onClickNewGame(view: View) {
        initData()
    }

    private fun initData(tableName: String = ExamDBHelper.TABLE_NAME, condition: String = "1=1 order by random() limit 100") {
        Thread(Runnable {
//            this@MainActivity.runOnUiThread(Runnable {
//                progressBar.visibility = View.VISIBLE
//            })

            try {
                ExamDBHelper.TABLE_NAME = tableName
                ExamDBHelper.examInfoList = examDBHelper.query(condition)
                startActivity(Intent(this, GameActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }

//            this@MainActivity.runOnUiThread(Runnable {
//                progressBar.visibility = View.GONE
//            })
        }).start()
    }

    private fun onClickErrorHistory(view: View) {
        TODO("尚未完成")
    }

    private fun onClickLawSets(view: View) {
        TODO("尚未完成")
    }

    private fun onClickClearScore(view: View) {
        val preferences = AppPreferences(this)
        preferences.clearHighScore()
        tv_current_score.text = "当前最高分：0"
        toast("已经重置最高分！")
    }

    private fun onClickExit(view: View) {
        finishAffinity()
        System.exit(0)
    }
}
