package xfy.wl.examfather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import xfy.wl.examfather.storage.AppPreferences


class MainActivity : AppCompatActivity() {

    private lateinit var examDBHelper: ExamDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        examDBHelper = ExamDBHelper.getInstance(this)
        _tv_top_score = tv_top_score
        val preferences = AppPreferences(this)
        setTopScore(preferences.getTopScore())

        button_new_game.setOnClickListener(this::onClickNewGame)
        button_error_history.setOnClickListener(this::onClickErrorHistory)
        button_law_sets.setOnClickListener(this::onClickLawSets)
        button_clear_score.setOnClickListener(this::onClickClearScore)
        button_exit.setOnClickListener(this::onClickExit)
    }

    private fun onClickNewGame(view: View) {
        initData()
    }

    private fun initData(tableName: String = ExamDBHelper.TABLE_NAME,
                         condition: String = "1=1 order by random() limit 100",
                         intentExtra: Map<String, Boolean> = mapOf(GameActivity.IS_EXAM to true)) {
        Thread(Runnable {
//            this@MainActivity.runOnUiThread(Runnable {
//                progressBar.visibility = View.VISIBLE
//            })

            try {
                ExamDBHelper.TABLE_NAME = tableName
                ExamDBHelper.examInfoList = examDBHelper.query(condition)
                var _intent = Intent(this, GameActivity::class.java)
                intentExtra.forEach{
                    key, value -> _intent.putExtra(key, value)
                }
                startActivity(_intent)
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
        var _intentExtra = mapOf(GameActivity.IS_EXAM to false)
        initData(condition = "1=1", intentExtra = _intentExtra)
    }

    private fun onClickClearScore(view: View) {
        val preferences = AppPreferences(this)
        preferences.clearTopScore()
        tv_top_score.text = "当前最高分：0"
        toast("已经重置最高分！")
    }

    private fun onClickExit(view: View) {
        finishAffinity()
        System.exit(0)
    }

    companion object {
        var _tv_top_score: TextView? = null

        fun setTopScore(topScore: Int) {
            _tv_top_score!!.text = "当前最高分：${topScore}"
        }
    }
}
