package com.example.classtask.teacher

class TeacherClassModel(
    title: String,
    section: String,
    students: Int,
    databaseId: String,
    userId: String
){
    private var myTitle = title
    private var mySection = section
    private var myStudents = students
    private var uniqueDBId = databaseId
    private var teacherUserId = userId

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
        return if(myStudents>1)
            "$myStudents Students"
        else if(myStudents==1)
            "$myStudents Student"
        else
            ""
    }

    fun setStudentCount(studentCount: Int) {
        myStudents = studentCount
    }

    fun getUniqueId(): String{
        return uniqueDBId
    }

    fun setUniqueId(uniqueId: String){
        uniqueDBId = uniqueId
    }

    fun getUserId(): String{
        return teacherUserId
    }

    fun setUserId(thisUserId: String){
        teacherUserId = thisUserId
    }
}