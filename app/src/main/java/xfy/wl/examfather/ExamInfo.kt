package xfy.wl.examfather

data class ExamInfo(var test_type: Int = 0, var point_id: String? = "", var item_count: Int? =0,
                    var content: String? ="", var ans_area: String? = "", var ans_chars: String? ="",
                    var person_type: Int =0, var career: Int = 0, var career_step: String ="",
                    var work_step: Int =0, var author: String ="", var choose_me: Int =0,
                    var id: Int = 0) {

    var realAnswer: Int = 0
    var userAnswer: Int = 0
    var isRightChoice: Boolean = false//存储最最终用户选择是否正确
    var hasFinishedResult: Boolean = false//是否完成了答题
}
