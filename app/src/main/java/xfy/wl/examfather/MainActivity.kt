package xfy.wl.examfather

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.alert
import xfy.wl.examfather.storage.AppPreferences
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var tvHighScore: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_new_game.setOnClickListener(this::onClickNewGame)
        button_continue_game.setOnClickListener(this::onClickContinueGame)
        button_error_history.setOnClickListener(this::onClickErrorHistory)
        button_law_sets.setOnClickListener(this::onClickLawSets)
        button_exit.setOnClickListener(this::onClickExit)


    }

    private fun onClickNewGame(view: View) {
        //var dialog = ProgressBar(this)
        //dialog.tooltipText = "加载中..."
        //var intent = Intent(this, GameActivity::class.java)
        //intent.putExtra(GameActivity.IS_EXAM, true)
        startActivity(Intent(this, GameActivity::class.java))
    }

    private fun onClickContinueGame(view: View) {
        val preferences = AppPreferences(this)
        preferences.clearHighScore()
        Snackbar.make(view, "最高分重置成功", Snackbar.LENGTH_SHORT).show()
        tvHighScore?.text = "最高分：${preferences.getHighScore()}"
    }

    private fun onClickErrorHistory(view: View) {
        val preferences = AppPreferences(this)
        preferences.clearHighScore()
        Snackbar.make(view, "最高分重置成功", Snackbar.LENGTH_SHORT).show()
        tvHighScore?.text = "最高分：${preferences.getHighScore()}"
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
