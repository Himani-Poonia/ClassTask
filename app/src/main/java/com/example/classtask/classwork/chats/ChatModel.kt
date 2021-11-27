package com.example.classtask.classwork.chats

class ChatModel(
    senderId: String,
    messageId: String,
    message: String,
    messageTime: String,
    senderName: String
) {
    private var senderId = senderId
    private var messageId = messageId
    private var message = message
    private var messageTime = messageTime
    private var senderName = senderName

    fun getSenderId() : String{
        return senderId
    }

    fun getMessageId() : String{
        return messageId
    }

    fun getMessage() : String{
        return message
    }

    fun getMessageTime() : String{
        return messageTime
    }

    fun getSenderName() : String{
        return senderName
    }
}