package com.example.classtask.classwork.assignment

class AssignmentClassModel(
    title: String,
    time: String,
    assignId: String,
    description: String,
    points: String,
    className: String
) {
    private var myTitle = title
    private var myTime = time
    private var myAssignId = assignId
    private var myDescription = description
    private var myPoints = points
    private var myClassName = className

    fun getClassName(): String {
        return myClassName
    }

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

    fun getDesc(): String {
        return myDescription
    }

    fun setDesc(desc: String) {
        myDescription = desc
    }

    fun getAssignId(): String {
        return myAssignId
    }

    fun setAssignId(assignID: String) {
        myAssignId = assignID
    }

    fun getPoints(): String {
        return myPoints
    }

    fun setPoints(Points: String) {
        myPoints = Points
    }
}