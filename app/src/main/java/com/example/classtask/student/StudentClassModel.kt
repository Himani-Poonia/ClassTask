package com.example.classtask.student

class StudentClassModel(
    title: String,
    section: String,
    teacherName: String,
){
    private var myTitle = title
    private var mySection = section
    private var myTeacher = teacherName

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
}