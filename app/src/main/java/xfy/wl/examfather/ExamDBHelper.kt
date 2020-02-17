package xfy.wl.examfather

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.util.Log

import xfy.wl.examfather.ExamInfo
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper

class ExamDBHelper(var context: Context, private var DB_VERSION: Int=CURRENT_VERSION) : ManagedSQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private val TAG = "ExamDBHelper"
        var DB_NAME = Environment.getExternalStoragePublicDirectory("car.db")
            .toString()
        var TABLE_NAME = "c1_1" //表名称
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
        Log.d(TAG, "onCreate")
        val drop_sql = "DROP TABLE IF EXISTS $TABLE_NAME;"
        Log.d(TAG, "drop_sql:" + drop_sql)
        db.execSQL(drop_sql)
        val create_sql = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL," +
                "question VARCHAR NOT NULL," + "answer VARCHAR NOT NULL," +
                "item1 VARCHAR NOT NULL," + "item2 VARCHAR NOT NULL," +
                "item3 VARCHAR NOT NULL," + "item4 VARCHAR NOT NULL," +
                //演示数据库升级时要先把下面这行注释
                "explains VARCHAR" + ",url VARCHAR" + ");"
        Log.d(TAG, "create_sql:" + create_sql)
        db.execSQL(create_sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun delete(condition: String): Int {
        var count = 0
        use {
            count = delete(TABLE_NAME, condition, null)
        }
        return count
    }

//    fun insert(info: ExamInfo): Long {
//        val infoArray = mutableListOf(info)
//        return insert(infoArray)
//    }

//    fun insert(infoArray: MutableList<ExamInfo>): Long {
//        var result: Long = -1
//        for (i in infoArray.indices) {
//            val info = infoArray[i]
//            var tempArray: List<ExamInfo>
//            // 如果存在同名记录，则更新记录
//            // 注意条件语句的等号后面要用单引号括起来
//            if (info.name.isNotEmpty()) {
//                val condition = "name='${info.name}'"
//                tempArray = query(condition)
//                if (tempArray.size > 0) {
//                    update(info, condition)
//                    result = tempArray[0].rowid
//                    continue
//                }
//            }
//            // 如果存在同样的手机号码，则更新记录
//            if (info.phone.isNotEmpty()) {
//                val condition = "phone='${info.phone}'"
//                tempArray = query(condition)
//                if (tempArray.size > 0) {
//                    update(info, condition)
//                    result = tempArray[0].rowid
//                    continue
//                }
//            }
//            // 不存在唯一性重复的记录，则插入新记录
//            val cv = ContentValues()
//            cv.put("name", info.name)
//            cv.put("age", info.age)
//            cv.put("height", info.height)
//            cv.put("weight", info.weight)
//            cv.put("married", info.married)
//            cv.put("update_time", info.update_time)
//            cv.put("phone", info.phone)
//            cv.put("password", info.password)
//            use {
//                result = insert(TABLE_NAME, "", cv)
//            }
//            // 添加成功后返回行号，失败后返回-1
//            if (result == -1L) {
//                return result
//            }
//        }
//        return result
//    }

    @JvmOverloads
//    fun update(info: ExamInfo, condition: String = "rowid=${info.rowid}"): Int {
//        val cv = ContentValues()
//        cv.put("name", info.name)
//        cv.put("age", info.age)
//        cv.put("height", info.height)
//        cv.put("weight", info.weight)
//        cv.put("married", info.married)
//        cv.put("update_time", info.update_time)
//        cv.put("phone", info.phone)
//        cv.put("password", info.password)
//        var count = 0
//        use {
//            count = update(TABLE_NAME, cv, condition, null)
//        }
//        return count
//    }

    fun query(condition: String): List<ExamInfo> {
        val sql = "select * from $TABLE_NAME where $condition;"
        Log.d(TAG, "query sql: " + sql)
        var infoArray = mutableListOf<ExamInfo>()
        use {
            val cursor = rawQuery(sql, null)
            if (cursor.moveToFirst()) {
                while (true) {
                    val info = ExamInfo()
                    info.question = cursor.getString(1)
                    info.answer = cursor.getString(8)
//                    info.item1 = cursor.getString(2)
//                    info.item2 = cursor.getString(3)
//                    info.item3 = cursor.getString(4)
//                    info.item4 = cursor.getString(5)
//                    info.explains = cursor.getString(6)
//                    info.url = cursor.getString(7)
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

//    fun queryByPhone(phone: String): ExamInfo {
//        val infoArray = query("phone='$phone'")
//        val info: ExamInfo = if (infoArray.size>0) infoArray[0] else ExamInfo()
//        return info
//    }

//    fun deleteAll(): Int = delete("1=1")

    fun queryAll(): List<ExamInfo> = query("1=1")

}