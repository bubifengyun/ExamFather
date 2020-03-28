package xfy.wl.examfather

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.util.Log

import xfy.wl.examfather.ExamInfo
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper

class ExamDBHelper(var context: Context, private var DB_VERSION: Int=CURRENT_VERSION)
    : ManagedSQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private val TAG = "ExamDBHelper"
        lateinit var examInfoList: List<ExamInfo>
        var DB_NAME = Environment.getExternalStoragePublicDirectory("exams.db")
            .toString()
        var TABLE_NAME = "tb_exams" //表名称
        var CURRENT_VERSION = 10 //当前的最新版本，如有表结构变更，该版本号要加一
        private var instance: ExamDBHelper? = null
        @Synchronized
        fun getInstance(ctx: Context, version: Int=0): ExamDBHelper {
            if (instance == null) {
                //如果调用时没传版本号，就使用默认的最新版本号
                instance = if (version>0) ExamDBHelper(ctx.applicationContext, version)
                else ExamDBHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    @JvmOverloads
    fun query(condition: String): List<ExamInfo> {
        val sql = "select * from $TABLE_NAME where $condition;"
        Log.d(TAG, "query sql: " + sql)
        var infoArray = mutableListOf<ExamInfo>()
        use {
            val cursor = rawQuery(sql, null)
            if (cursor.moveToFirst()) {
                while (true) {
                    val info = ExamInfo()
                    info.id = cursor.getInt(cursor.getColumnIndex("id"))
                    info.test_type = cursor.getInt(cursor.getColumnIndex("test_type"))
                    info.point_id = cursor.getString(cursor.getColumnIndex("point_id"))
                    info.item_count = cursor.getInt(cursor.getColumnIndex("item_count"))
                    info.content = cursor.getString(cursor.getColumnIndex("content"))
                    info.ans_area = cursor.getString(cursor.getColumnIndex("ans_area"))
                    info.ans_chars = cursor.getString(cursor.getColumnIndex("ans_chars"))
                    info.person_type = cursor.getInt(cursor.getColumnIndex("person_type"))
                    info.career = cursor.getInt(cursor.getColumnIndex("career"))
                    info.career_step = cursor.getString(cursor.getColumnIndex("career_step"))
                    info.work_step = cursor.getInt(cursor.getColumnIndex("work_step"))
                    info.author = cursor.getString(cursor.getColumnIndex("author"))
                    info.choose_me = cursor.getInt(cursor.getColumnIndex("choose_me"))
                    infoArray.add(info)
                    if (cursor.isLast) {
                        break
                    }
                    cursor.moveToNext()
                }
            }
            cursor.close()
        }
        return infoArray
    }
}
