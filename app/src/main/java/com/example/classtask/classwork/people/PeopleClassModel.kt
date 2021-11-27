package com.example.classtask.classwork.people

class PeopleClassModel(
    name: String,
    email: String
) {
    private var studentName = name
    private var studentEmail = email

    fun getStudentName(): String {
        return studentName
    }

    fun getStudentEmail(): String {
        return studentEmail
    }
}