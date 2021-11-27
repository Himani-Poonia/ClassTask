package com.example.classtask.classwork.assignment

class AssignmentClassModel(
    title: String,
    time: String,
    description: String,
    points: String,
    className: String,
    classId: String
) {
    private var myTitle = title
    private var myTime = time
    private var myDescription = description
    private var myPoints = points
    private var myClassName = className
    private var classId = classId

    fun getClassId(): String{
        return classId
    }

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

    fun getPoints(): String {
        return myPoints
    }

    fun setPoints(Points: String) {
        myPoints = Points
    }
}