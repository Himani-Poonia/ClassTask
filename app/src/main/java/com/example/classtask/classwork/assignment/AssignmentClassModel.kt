package com.example.classtask.classwork.assignment

class AssignmentClassModel(
    title: String,
    time: String
) {
    private var myTitle = title
    private var myTime = time

    fun getTitle(): String {
        return myTitle
    }

    fun setTitle(title: String) {
        myTitle = title
    }

    fun getTime(): String {
        return myTime
    }

    fun setTime(time: String) {
        myTime = time
    }
}