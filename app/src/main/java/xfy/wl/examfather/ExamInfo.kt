package xfy.wl.examfather

data class ExamInfo(var question: String = "", var answer: String = "", var item1: String? ="",
                    var item2: String? ="", var item3: String? = "", var item4: String? ="",
                    var explains: String? ="", var url: String? ="") {

    //单选
    var rightAnswer: Int = 0  //存储正确答案
    var errorAnswer: Int = 0  //存储错误答案
    var isRightChoice: Boolean = false//存储最最终用户选择是否正确
    var hasFinishedResult: Boolean = false//是否完成了答题

    //多选
    val rightList = ArrayList<Int>()//存储正确的答案
    val resultList = ArrayList<Int>()//存储用户答题的答案
}