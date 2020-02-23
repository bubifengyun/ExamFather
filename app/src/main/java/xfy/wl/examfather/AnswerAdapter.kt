package xfy.wl.examfather

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/20.
 */
class AnswerAdapter: BaseAdapter() {


    override fun getCount(): Int = ExamDBHelper.examInfoList.size

    override fun getItem(position: Int) = ExamDBHelper.examInfoList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var examInfo = ExamDBHelper.examInfoList[position]
        var viewHolder: ViewHolder?
        var _convertView = convertView

        if (_convertView == null) {
            viewHolder = ViewHolder()
            val displayMetrics = DisplayMetrics()
            var windowManager = parent!!.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            var width = displayMetrics.widthPixels
            _convertView = LayoutInflater.from(parent.context).inflate(R.layout.circle_img, parent, false);
            var params = AbsListView.LayoutParams(width / 9, width / 9);
            viewHolder.tv = _convertView!!.findViewById(R.id.circle_img);
            viewHolder.tv.layoutParams = params
            _convertView!!.tag = viewHolder
        } else {
            viewHolder = _convertView!!.tag as ViewHolder
        }

        viewHolder.tv.text = (position + 1).toString()

        if (!examInfo.hasFinishedResult) {//判断该题是否已经作答
            viewHolder.tv.setBackgroundResource(R.drawable.circle_default);
        } else {
            if (examInfo.isRightChoice) {//判断选择的结果是否正确
                viewHolder.tv.setBackgroundResource(R.drawable.circle_right);
            } else {
                viewHolder.tv.setBackgroundResource(R.drawable.circle_error);
            }
        }


        return _convertView!!
    }


    inner class ViewHolder {
        lateinit var tv: TextView
    }
}
