package xfy.wl.examfather

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatRadioButton
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.text.TextUtils
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.Nullable
//import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.getDrawable
import com.bumptech.glide.Glide


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

    var v: View? = null
    private var titleImg: ImageView? = null
    private var title: TextView? = null
    private var radioGroup: RadioGroup? = null
    private var explainLayout: LinearLayout? = null
    private var explainTxt: TextView? = null
    private var checkLayout: LinearLayout? = null

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

        initView()
        initData()
        initListner()

        // Inflate the layout for this fragment
        return v
    }

    private fun initView() {
        titleImg = v!!.findViewById(R.id.que_img)
        title = v!!.findViewById(R.id.que_title)
        radioGroup = v!!.findViewById(R.id.que_group)
        explainLayout = v!!.findViewById(R.id.que_explain_layout)
        explainTxt = v!!.findViewById(R.id.que_explain_txt)
        checkLayout = v!!.findViewById(R.id.que_check_layout)
    }

    private fun initData() {
        examInfo = ExamDBHelper.examInfoList[position]
        title!!.text = examInfo!!.question
        explainTxt!!.text = examInfo!!.explains

        answer = Integer.parseInt(examInfo!!.answer)
        if (answer <= 4) {
            initRadioButton()
        } else {
            initCheckBox()
        }

        if (!TextUtils.isEmpty(examInfo!!.url))
            Glide.with(context)
                .load(examInfo!!.url)
//                .apply(RequestOptions().placeholder(R.drawable.loading)
//                ).error(R.drawable.load_fail)
                .into(
                titleImg
            )
        else titleImg!!.visibility = View.GONE
        titleImg!!.visibility = View.GONE

    }


    fun initRadioButton() {
        if (!TextUtils.isEmpty(examInfo!!.item1)) {
            addRadioButtonView(examInfo!!.item1!!)
        }
        if (!TextUtils.isEmpty(examInfo!!.item2)) {
            addRadioButtonView(examInfo!!.item2!!)
        }
        if (!TextUtils.isEmpty(examInfo!!.item3)) {
            addRadioButtonView(examInfo!!.item3!!)
        }
        if (!TextUtils.isEmpty(examInfo!!.item4)) {
            addRadioButtonView(examInfo!!.item4!!)
        }

        if (examInfo!!.hasFinishedResult) {//判断当前题目是否已经答题答过了，如果是的话，就恢复答题数据，并且设置不可点击
            radioButtonClickEnable()
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

    fun initCheckBox() {
        if (!TextUtils.isEmpty(examInfo!!.item1)) {
            addCheckBoxView(examInfo!!.item1!!)
        }
        if (!TextUtils.isEmpty(examInfo!!.item2)) {
            addCheckBoxView(examInfo!!.item2!!)
        }
        if (!TextUtils.isEmpty(examInfo!!.item3)) {
            addCheckBoxView(examInfo!!.item3!!)
        }
        if (!TextUtils.isEmpty(examInfo!!.item4)) {
            addCheckBoxView(examInfo!!.item4!!)
        }

        if (examInfo!!.hasFinishedResult) {
            checkBoxClickEnable()
            //遍历用户的选择
            for (i in 0 until examInfo!!.resultList.size) {
                val a = examInfo!!.resultList[i]//拿到答题的标号
                listCheck!![a-1].setTextColor(context!!.getColor(R.color.error))
                listCheck!![a-1].isChecked = true
            }

            for (i in 0 until examInfo!!.rightList.size) {
                val a = examInfo!!.resultList[i]//拿到答题的标号
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
            checkLayout!!.addView(appCompatButton)
            appCompatButton.setOnClickListener { doHandle() }
        }
    }


    fun radioButtonClickEnable() {
        for (radioButton in listRadio!!) {
            radioButton.setClickable(false)
        }
    }

    fun checkBoxClickEnable() {
        for (checkbos in listCheck!!) {
            checkbos.setClickable(false)
        }
    }

    private fun initListner() {
        radioGroup?.setOnCheckedChangeListener { rd, id ->
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
                    radioButtonClickEnable()//设置不可点击

                    GameActivity.upDataRightAndError()//更新MainActivity
                    break
                }
            }
        }

        explainLayout!!.setOnClickListener{ explainTxt!!.visibility = View.VISIBLE}

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
        checkLayout!!.addView(checkBox)
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
        radioGroup?.addView(appCompatRadioButton)
        listRadio?.add(appCompatRadioButton)
    }

    fun setErrorDrable(appCompatRadioButton: AppCompatRadioButton) {
        val bitmapDrawable = getDrawable(context!!, R.drawable.ic_error)
        appCompatRadioButton.buttonDrawable = bitmapDrawable
    }


    fun setRightDrable(appCompatRadioButton: AppCompatRadioButton) {
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
        checkBoxClickEnable()
        GameActivity.upDataRightAndError()//更新MainActivity

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
