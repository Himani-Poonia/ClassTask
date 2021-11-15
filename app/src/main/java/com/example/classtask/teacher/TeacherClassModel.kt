package com.example.classtask.teacher

class TeacherClassModel(
    title: String,
    section: String,
    students: Int,
){
    private var myTitle = title
    private var mySection = section
    private var myStudents = students

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

    fun getStudentCount(): String {
        return if(myStudents>0)
            "$myStudents Students"
        else
            ""
    }

    fun setStudentCount(studentCount: Int) {
        myStudents = studentCount
    }
}