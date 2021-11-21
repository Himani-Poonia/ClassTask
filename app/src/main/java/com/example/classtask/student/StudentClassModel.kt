package com.example.classtask.student

class StudentClassModel(
    title: String,
    section: String,
    teacherName: String,
    userId: String,
    codeToJoin: String,
    teacherId: String
){
    private var myTitle = title
    private var mySection = section
    private var myTeacher = teacherName
    private var myCodeToJoin = codeToJoin
    private var myUserId = userId
    private var myTeacherId = teacherId

    fun getTitle(): String {
        return myTitle
    }

    fun setTitle(title: String) {
        myTitle = title
    }

    fun getSection(): String {
        return mySection
    }

    fun setSection(section: String) {
        mySection = section
    }

    fun getTeacherName(): String {
        return myTeacher
    }

    fun setTeacherName(teacherName: String) {
        myTeacher = teacherName
    }

    fun getUserId(): String {
        return myUserId
    }

    fun setUserId(newUserId: String) {
        myUserId = newUserId
    }

    fun getCodeToJoin(): String {
        return myCodeToJoin
    }

    fun setCodeToJoin(codeToJoin: String) {
        myCodeToJoin = codeToJoin
    }

    fun getTeacherId(): String {
        return myTeacherId
    }

    fun setTeacherId(teacherID: String) {
        myTeacherId = teacherID
    }
}