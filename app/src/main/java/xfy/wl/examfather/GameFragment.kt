package xfy.wl.examfather

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatRadioButton
import android.widget.LinearLayout
import android.widget.RadioGroup
//import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.getDrawable
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "position"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var position: Int = 0
    private val TIAN_KONG: Int = 1
    private val XUAN_ZE: Int = 2
    private val PAN_DUAN: Int = 3
    private val JIAN_DA: Int = 4
    private val ZONG_SHU: Int = 5
    private val DUO_XUAN: Int = 6

    var v: View? = null

    lateinit var listRadio: MutableList<AppCompatRadioButton>
    lateinit var listCheck: MutableList<AppCompatCheckBox>
    var realAnswer: Int = 0
    var userAnswer: Int = 0
    lateinit var examInfo: ExamInfo
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments!!.getInt("position")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listRadio = ArrayList<AppCompatRadioButton>()
        listCheck = ArrayList<AppCompatCheckBox>()

        v = inflater.inflate(R.layout.fragment_game, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Kotlin之Fragment中直接引用视图控件id
        // https://blog.csdn.net/wxx_csdn/article/details/78261903
        initData()
    }

    private fun initData() {
        examInfo = ExamDBHelper.examInfoList[position]
        initAnswer()
        when (examInfo.test_type) {
            TIAN_KONG -> renderTianKong()
            XUAN_ZE -> renderXuanZe()
            PAN_DUAN -> renderPanDuan()
            JIAN_DA, ZONG_SHU -> renderLongEditText()
            DUO_XUAN -> renderDuoXuan()
            else -> {}
        }
    }

    private fun renderTianKong() {
        initEditText()
    }

    private fun renderXuanZe() {
        var lines = examInfo.content!!.split("\n").toMutableList()
        que_title.text = "第" + (position+1).toString() + "题（选择题）." + lines.first()
        lines.removeAt(0)
        while (lines.remove(""));
        initRadioButton(lines as ArrayList<String>)
    }

    private fun renderPanDuan() {
        que_title.text = "第" + (position+1).toString() + "题（判断题）." + examInfo.content
        initRadioButton(arrayListOf("正确","错误"))
    }

    private fun renderDuoXuan() {
        var lines = examInfo.content!!.split("\n").toMutableList()
        que_title.text = "第" + (position+1).toString() + "题（多选题）." + lines.first()
        lines.removeAt(0)
        while (lines.remove(""));
        initCheckBox(lines as ArrayList<String>)
    }

    fun initEditText() {

    }

    private fun renderLongEditText(){

    }

    private fun initRadioButton(items: ArrayList<String>) {
        for (item in items) {
            addRadioButtonView(item)
        }

        if (examInfo.hasFinishedResult) {//判断当前题目是否已经答题答过了，如果是的话，就恢复答题数据，并且设置不可点击
            radioButtonClickDisable()
            if (!examInfo!!.isRightChoice) {//如果没有选择到正确答案的话，就要显示错误答案，否则不显示
                val radio = listRadio!![examInfo!!.userAnswer]
                radio.setTextColor(context!!.getColor(R.color.error))
                setErrorDrable(radio)
            }
            val radio = listRadio!![examInfo!!.realAnswer]
            radio.setTextColor(context!!.getColor(R.color.right))
            setRightDrable(radio)
        } else {
            que_group.setOnCheckedChangeListener(this::onRadioButtonGroupListener)
        }
    }

    private fun initCheckBox(items: ArrayList<String>) {
        for (item in items) {
            addCheckBoxView(item)
        }

        if (examInfo.hasFinishedResult) { // 回看的时候，还需要再次判断是否做过
            checkBoxClickDisable()

            for (i in 0..3) {
                if (examInfo.realAnswer.shr(i) % 2 == 1) {
                    listCheck[i].setTextColor(context!!.getColor(R.color.right))
                    if (examInfo.userAnswer.shr(i) % 2 == 1)
                        listCheck[i].isChecked = true
                } else if (examInfo.userAnswer.shr(i) % 2 == 1) {
                    listCheck[i].setTextColor(context!!.getColor(R.color.error))
                    listCheck[i].isChecked = true
                }
            }

        } else {
            //添加一个确定按钮
            val appCompatButton = AppCompatButton(context!!)
            appCompatButton.text = "确定"
            appCompatButton.setBackgroundColor(context!!.getColor(R.color.colorPrimary))
            appCompatButton.setTextColor(Color.parseColor("#ffffff"))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(20, 30, 20, 20)
            appCompatButton.layoutParams = params
            que_check_layout!!.addView(appCompatButton)
            appCompatButton.setOnClickListener { onClickDuoXuanSubmitListener() }
        }
    }


    private fun radioButtonClickDisable() {
        for (radioButton in listRadio!!) {
            radioButton.setClickable(false)
        }
    }

    private fun checkBoxClickDisable() {
        for (checkbox in listCheck!!) {
            checkbox.setClickable(false)
        }
    }

    private fun initAnswer(){
        if (examInfo.hasFinishedResult) {
            realAnswer = examInfo.realAnswer
        } else {
            realAnswer = when (examInfo.test_type) {
                PAN_DUAN -> when (examInfo.ans_chars) {
                    "V", "v", "Ｖ", "ｖ" -> 0
                    else -> 1
                }
                XUAN_ZE -> when (examInfo.ans_chars) {
                    "A", "a", "Ａ", "ａ" -> 0
                    "B", "b", "Ｂ", "ｂ" -> 1
                    "C", "c", "Ｃ", "ｃ" -> 2
                    else -> 3
                }
                DUO_XUAN -> {
                    realAnswer = 0
                    if (examInfo.ans_chars!!.contains("A"))
                        realAnswer += 1
                    if (examInfo.ans_chars!!.contains("B"))
                        realAnswer += 2
                    if (examInfo.ans_chars!!.contains("C"))
                        realAnswer += 4
                    if (examInfo.ans_chars!!.contains("D"))
                        realAnswer += 8
                    realAnswer
                }
                else -> 0
            }

            examInfo.realAnswer = realAnswer
        }
    }

    private fun onRadioButtonGroupListener(rd: RadioGroup, id: Int) {
        if (listRadio.isNullOrEmpty()) return

        for (i in listRadio.indices) {
            var radioButton = listRadio[i]

            //遍历查找找到当前点击的item
            if (radioButton.isChecked) {
                userAnswer = i
                examInfo.userAnswer = userAnswer
                examInfo.isRightChoice = (userAnswer == realAnswer)
                examInfo.hasFinishedResult = true
                radioButtonClickDisable()
                GameActivity.updateRightAndError()

                if (examInfo.isRightChoice) {
                    radioButton.setTextColor(context!!.getColor(R.color.right))
                    setRightDrable(radioButton)
                    GameActivity.nextQuestion()
                } else {
                    radioButton.setTextColor(context!!.getColor(R.color.error))
                    setErrorDrable(radioButton)
                    listRadio[realAnswer].setTextColor(context!!.getColor(R.color.right))
                    setRightDrable(listRadio[realAnswer])
                }
                break
            }
        }
    }


    fun addCheckBoxView(question: String) {
        val checkBox = AppCompatCheckBox(context!!)
        checkBox.text = question
        checkBox.textSize = 20f
        checkBox.setTextColor(context!!.getColor(R.color.black333))
        val param = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(10, 10, 10, 10)
        checkBox.layoutParams = param
        que_check_layout!!.addView(checkBox)
        listCheck!!.add(checkBox)
    }


    private fun addRadioButtonView(question: String) {
        val appCompatRadioButton = AppCompatRadioButton(context!!)
        appCompatRadioButton.text = question
        val bitmapDrawable = getDrawable(context!!, R.drawable.ic_default)
        appCompatRadioButton.buttonDrawable = bitmapDrawable
        appCompatRadioButton.textSize = 20f
        appCompatRadioButton.setTextColor(context!!.getColor(R.color.black333))
        val param = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(10, 10, 10, 10)
        appCompatRadioButton.layoutParams = param
        que_group.addView(appCompatRadioButton)
        listRadio.add(appCompatRadioButton)
    }

    private fun setErrorDrable(appCompatRadioButton: AppCompatRadioButton) {
        val bitmapDrawable = getDrawable(context!!, R.drawable.ic_error)
        appCompatRadioButton.buttonDrawable = bitmapDrawable
    }


    private fun setRightDrable(appCompatRadioButton: AppCompatRadioButton) {
        val bitmapDrawable = getDrawable(context!!, R.drawable.ic_right)
        appCompatRadioButton.buttonDrawable = bitmapDrawable
    }

    private fun onClickDuoXuanSubmitListener() {
        for (i in 0..3) {
            if (realAnswer.shr(i) % 2 == 1) {
                listCheck[i].setTextColor(context!!.getColor(R.color.right))
            }
        }

        userAnswer = 0
        for (i in listCheck.indices) {
            if (listCheck[i].isChecked) {  //遍历查询当前是否选中
                userAnswer += 1.shl(i)
                if ((1.shl(i) and realAnswer) == 0 ) {
                    listCheck[i].setTextColor(context!!.getColor(R.color.error))
                }
            }
        }
        
        examInfo.userAnswer = userAnswer
        examInfo.hasFinishedResult = true
        checkBoxClickDisable()
        examInfo.isRightChoice = (realAnswer == userAnswer)
        GameActivity.updateRightAndError()

        if (examInfo.isRightChoice)
            GameActivity.nextQuestion()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
