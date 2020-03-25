package xfy.wl.examfather

data class ExamInfo(var test_type: Int = 0, var point_id: String? = "", var item_count: Int? =0,
                    var content: String? ="", var ans_area: String? = "", var ans_chars: String? ="",
                    var person_type: Int =0, var career: Int = 0, var career_step: String ="",
                    var work_step: Int =0, var author: String ="", var choose_me: Int =0) {

    //单选
    var rightAnswer: Int = 0  //存储正确答案
    var errorAnswer: Int = 0  //存储错误答案
    var isRightChoice: Boolean = false//存储最最终用户选择是否正确
    var hasFinishedResult: Boolean = false//是否完成了答题

    //多选
    val rightList = ArrayList<Int>()//存储正确的答案
    val resultList = ArrayList<Int>()//存储用户答题的答案
}