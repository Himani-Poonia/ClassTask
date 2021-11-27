package com.example.classtask.classwork.chats

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.example.classtask.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat

class ChatAdapter(
    context: Context?,
    chatModelList: List<ChatModel>,
    classId: String,
    teacherId: String
):
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var context = context
    private var chatModelList = chatModelList
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    private var rootReference = FirebaseDatabase.getInstance().reference
    private val userID: String = firebaseUser?.uid.toString()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.message_list_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val messageModel: ChatModel = chatModelList[position]

        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")

        val msgTime = messageModel.getMessageTime().toLong()
        val messageTime: String = dateFormat.format(msgTime)
//        Log.i("time", msgTime.toString())
        val senderId = messageModel.getSenderId()
        val senderName = messageModel.getSenderName()


        if(userID == senderId){ //message sent
            holder.sentLinearLayout.visibility = View.VISIBLE
            holder.receivedLinearLayout.visibility = View.GONE

            holder.sentMsgTextView.text = messageModel.getMessage()
            holder.sentMsgTimeTextView.text = messageTime
            holder.senderTextView.text = senderName

        } else{ //message received
            holder.sentLinearLayout.visibility = View.GONE
            holder.receivedLinearLayout.visibility = View.VISIBLE

            holder.receivedMsgTextView.text = messageModel.getMessage()
            holder.receivedMsgTimeTextView.text = messageTime
            holder.receiverTextView.text = senderName
        }
    }


    override fun getItemCount(): Int {
        return chatModelList.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sentLinearLayout: LinearLayout = itemView.findViewById(R.id.sentLinearLayout)
        var receivedLinearLayout: LinearLayout = itemView.findViewById(R.id.receivedLinearLayout)
        var sentMsgTextView: TextView = itemView.findViewById(R.id.sentMsgTextView)
        var sentMsgTimeTextView: TextView = itemView.findViewById(R.id.sentMsgTimeTextView)
        var receivedMsgTextView: TextView = itemView.findViewById(R.id.receivedMsgTextView)
        var receivedMsgTimeTextView: TextView = itemView.findViewById(R.id.receivedMsgTimeTextView)
        var senderTextView: TextView = itemView.findViewById(R.id.senderTv)
        var receiverTextView: TextView = itemView.findViewById(R.id.receiverTv)
    }
}