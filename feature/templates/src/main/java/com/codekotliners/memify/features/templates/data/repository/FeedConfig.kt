package com.codekotliners.memify.features.templates.data.repository

// Раньше nextStart был Firestore DocumentSnapshot (курсор пагинации). У REST-бэка курсоров
// нет — используем Int (сколько элементов уже загружено), см. TemplatesRestDatasource.kt.
class FeedConfig(
    val loop: Boolean = false,
) {
    var scrollState: ScrollState = ScrollState.NONE
    var nextStart: Int? = null
        private set

    fun setNextStart(newNextStart: Int?) {
        nextStart = newNextStart
        scrollState =
            if (newNextStart == null && !loop) {
                ScrollState.REACHED_END
            } else {
                ScrollState.STARTED
            }
    }

    fun reset() {
        scrollState = ScrollState.NONE
        nextStart = null
    }
}

enum class ScrollState {
    NONE,
    STARTED,
    REACHED_END,
}
