package com.example.classtask.classwork.assignment.submissions

class SubmissionModel(
    studentId: String,
    studentName: String,
    fileName: String
) {
    private var studentId = studentId
    private var studentName = studentName
    private var fileName = fileName

    fun getStudentId() : String{
        return studentId
    }

    fun getStudentName() : String{
        return studentName
    }

    fun getFileName() : String{
        return fileName
    }
}