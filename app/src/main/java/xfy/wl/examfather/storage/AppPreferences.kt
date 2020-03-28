package xfy.wl.examfather.storage

import android.content.Context
import android.content.SharedPreferences


class AppPreferences(ctx: Context) {

    var data: SharedPreferences = ctx.getSharedPreferences(
        "APP_PREFERENCES", Context.MODE_PRIVATE)

    fun saveTopScore(topScore: Int) {
        if (topScore > data.getInt("TOP_SCORE",0))
            data.edit().putInt("TOP_SCORE", topScore).apply()
    }

    fun getTopScore(): Int {
        return data.getInt("TOP_SCORE", 0)
    }

    fun clearTopScore() {
        data.edit().putInt("TOP_SCORE", 0).apply()
    }

    fun saveCurrentExamPage(currentExamPage: Int) {
        data.edit().putInt("CURRENT_EXAM_PAGE", currentExamPage).apply()
    }

    fun getCurrentExamPage(): Int {
        return data.getInt("CURRENT_EXAM_PAGE", 0)
    }
}
