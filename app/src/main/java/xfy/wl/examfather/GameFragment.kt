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
    var answer: Int = 0
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
        initListener()
    }

    private fun initData() {
        examInfo = ExamDBHelper.examInfoList[position]
        when (examInfo.test_type) {
            TIAN_KONG -> renderTianKong()
            XUAN_ZE -> renderXuanZe()
            PAN_DUAN -> renderPanDuan()
            JIAN_DA, ZONG_SHU -> renderLongEditText()
            DUO_XUAN -> renderDuoXuan()
            else -> {}
        }
        answer = Integer.parseInt(examInfo.ans_area);
        if (answer > 4) {
            renderDuoXuan()
        } else {
            renderXuanZe()
        }
    }

    fun renderTianKong() {
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

        if (examInfo!!.hasFinishedResult) {//判断当前题目是否已经答题答过了，如果是的话，就恢复答题数据，并且设置不可点击
            radioButtonClickDisable()
            if (!examInfo!!.isRightChoice) {//如果没有选择到正确答案的话，就要显示错误答案，否则不显示
                val radio = listRadio!![examInfo!!.errorAnswer - 1]
                radio.setTextColor(context!!.getColor(R.color.error))
                setErrorDrable(radio)
            }
            val radio = listRadio!![examInfo!!.rightAnswer - 1]
            radio.setTextColor(context!!.getColor(R.color.right))
            setRightDrable(radio)
        }
    }

    private fun initCheckBox(items: ArrayList<String>) {
        for (item in items) {
            addCheckBoxView(item)
        }

        if (examInfo!!.hasFinishedResult) {
            checkBoxClickDisable()
            //遍历用户的选择
            for (i in 0 until examInfo!!.resultList.size) {
                val a = examInfo!!.resultList[i]//拿到答题的标号
                listCheck!![a-1].setTextColor(context!!.getColor(R.color.error))
                listCheck!![a-1].isChecked = true
            }

            for (i in 0 until examInfo!!.rightList.size) {
                val a = examInfo!!.rightList[i]//拿到答题的标号
                listCheck!![a-1].setTextColor(context!!.getColor(R.color.right))
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
            appCompatButton.setOnClickListener { doHandle() }
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

    private fun initListener() {
        que_group?.setOnCheckedChangeListener { rd, id ->
            if (listRadio.isNullOrEmpty()) return@setOnCheckedChangeListener
            for (i in listRadio!!.indices) {
                var radioButton = listRadio!![i]
                //遍历查找找到当前点击的item
                if (radioButton.id == id) {
                    if (i + 1 == answer) {//判断选择是否是正确答案
                        radioButton.setTextColor(context!!.getColor(R.color.right))
                        examInfo!!.isRightChoice = true//存储用户选择的答案为正确的
                        setRightDrable(radioButton)//设置样式
                        GameActivity.nextQuestion()
                    } else {//选择的是错误答案
                        radioButton.setTextColor(context!!.getColor(R.color.error))
                        setErrorDrable(radioButton)//设置样式

                        listRadio!![answer - 1]
                            .setTextColor(context!!.getColor(R.color.right))
                        setRightDrable(listRadio!![answer - 1])

                        examInfo!!.errorAnswer = i + 1//设置选错题目的标识
                        examInfo!!.isRightChoice = false//存储用户选择的答案为错误的
                        setErrorDrable(radioButton)
                    }
                    examInfo!!.rightAnswer = answer//设置选对题目的标识
                    examInfo!!.hasFinishedResult = true//设置完成了答题
                    radioButtonClickDisable()//设置不可点击

                    GameActivity.updateRightAndError()//更新MainActivity
                    break
                }
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


    fun addRadioButtonView(question: String) {
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


    //处理点击事件
    private fun doHandle() {
        when (answer) {
            7//AB 也就是1、2选项为正确
            -> {
                //存储正确答案
                examInfo!!.rightList.add(1)
                listCheck!![0].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(2)
                listCheck!![1].setTextColor(context!!.getColor(R.color.right))
            }
            8//AC 也就是1、3
            -> {
                //存储正确答案
                examInfo!!.rightList.add(1)
                listCheck!![0].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(3)
                listCheck!![2].setTextColor(context!!.getColor(R.color.right))
            }
            9//AD 也就是1、4
            -> {
                //存储正确答案
                examInfo!!.rightList.add(1)
                listCheck!![0].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(4)
                listCheck!![3].setTextColor(context!!.getColor(R.color.right))
            }
            10//BC 也就是2、3
            -> {
                //存储正确答案
                examInfo!!.rightList.add(2)
                listCheck!![1].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(3)
                listCheck!![2].setTextColor(context!!.getColor(R.color.right))
            }
            11//BD 也就是2、4
            -> {
                //存储正确答案
                examInfo!!.rightList.add(2)
                listCheck!![1].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(4)
                listCheck!![3].setTextColor(context!!.getColor(R.color.right))
            }
            12//CD 也就是3、4
            -> {
                //存储正确答案
                examInfo!!.rightList.add(3)
                listCheck!![2].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(4)
                listCheck!![3].setTextColor(context!!.getColor(R.color.right))
            }
            13//ABC 也就是1、2、3
            -> {

                //存储正确答案
                examInfo!!.rightList.add(1)
                listCheck!![0].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(2)
                listCheck!![1].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(3)
                listCheck!![2].setTextColor(context!!.getColor(R.color.right))
            }
            14//ABD 也就是1、2、4
            -> {
                examInfo!!.rightList.add(1)
                listCheck!![0].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(2)
                listCheck!![1].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(4)
                listCheck!![3].setTextColor(context!!.getColor(R.color.right))
            }
            15//ACD 也就是1、3、4
            -> {
                examInfo!!.rightList.add(1)
                listCheck!![0].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(3)
                listCheck!![2].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(4)
                listCheck!![3].setTextColor(context!!.getColor(R.color.right))
            }
            16//BCD 也就是2、3、4
            -> {
                examInfo!!.rightList.add(4)
                listCheck!![3].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(2)
                listCheck!![1].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(3)
                listCheck!![2].setTextColor(context!!.getColor(R.color.right))
            }
            17//ABCD 也就是1、2、3、4
            -> {
                examInfo!!.rightList.add(1)
                listCheck!![0].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(2)
                listCheck!![1].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(3)
                listCheck!![2].setTextColor(context!!.getColor(R.color.right))
                examInfo!!.rightList.add(4)
                listCheck!![3].setTextColor(context!!.getColor(R.color.right))
            }
        }
        //然后进行筛选
        commonJudge()

    }

    fun commonJudge() {
        //存储用户选择的答案
        for (i in listCheck!!.indices) {
            if (listCheck!![i].isChecked) {  //遍历查询当前是否选中
                examInfo!!.rightList.add(i + 1)
            }
        }

        //先判断用户输入的和答案的选择个数是否相同
        if (examInfo!!.resultList.size === examInfo!!.rightList.size) {
            for (i in 0 until examInfo!!.resultList.size) {
                if (examInfo!!.resultList[i] !== examInfo!!.rightList[i]) {
                    examInfo!!.isRightChoice = false //遍历如果有答案不相同时，则选择答题失败，并跳出
                    //如果有错误的话就把错误答案显示出来
                    for (j in listCheck!!.indices) {
                        if (listCheck!![j].isChecked) {  //遍历查询当前是否选中
                            listCheck!![j]
                                .setTextColor(context!!.getColor(R.color.error))
                        }
                    }
                    break
                } else {
                    examInfo!!.hasFinishedResult = true//这一步骤可以设置为true，因为只要有不同就直接跳出了，并且设置了false，如果不走的话，就是为true
                }
            }
            if (examInfo!!.hasFinishedResult)
                GameActivity.nextQuestion()
        } else {
            //如果有错误的话就把错误答案显示出来
            for (j in listCheck!!.indices) {
                if (listCheck!![j].isChecked) {  //遍历查询当前是否选中
                    listCheck!![j].setTextColor(context!!.getColor(R.color.error))
                }
            }
            examInfo!!.isRightChoice = false
        }
        examInfo!!.hasFinishedResult = true
        checkBoxClickDisable()
        GameActivity.updateRightAndError()//更新MainActivity

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
