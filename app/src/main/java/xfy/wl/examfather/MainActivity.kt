package xfy.wl.examfather

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
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

        button_test_game.setOnClickListener(this::onClickTestGame)
        button_new_game.setOnClickListener(this::onClickNewGame)
        button_continue_game.setOnClickListener(this::onClickContinueGame)
        button_error_history.setOnClickListener(this::onClickErrorHistory)
        button_law_sets.setOnClickListener(this::onClickLawSets)
        button_exit.setOnClickListener(this::onClickExit)
    }

    private fun onClickTestGame(view: View) {
        startActivity(Intent(this, GameActivity::class.java))
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

    private fun onClickContinueGame(view: View) {
        val preferences = AppPreferences(this)
        preferences.clearHighScore()
        Snackbar.make(view, "最高分重置成功", Snackbar.LENGTH_SHORT).show()
    }

    private fun onClickErrorHistory(view: View) {
        val preferences = AppPreferences(this)
        preferences.clearHighScore()
        Snackbar.make(view, "最高分重置成功", Snackbar.LENGTH_SHORT).show()
    }

    private fun onClickLawSets(view: View) {
        val datetime: String = SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        tv_current_score.text = datetime
    }

    private fun onClickExit(view: View) {
        finishAffinity()
        System.exit(0)
    }
}
