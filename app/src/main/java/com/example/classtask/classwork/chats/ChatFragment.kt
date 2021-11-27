package com.example.classtask.classwork.chats

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classtask.NodeNames
import com.example.classtask.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ChildEventListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class ChatFragment : Fragment() {

    private lateinit var thisContext: Context
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var emptyListTextView: TextView
    private lateinit var chatModelList: ArrayList<ChatModel>
    private var teacherId = ""
    private var classId = ""
    private var currentPage = 1
    private val RECORD_PER_PAGE = 30
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var childEventListener: ChildEventListener? = null
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = firebaseAuth.currentUser
    private var rootReference = FirebaseDatabase.getInstance().reference
    private val userID: String = firebaseUser?.uid.toString()
    private lateinit var progressBar: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        thisContext = container!!.context
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        teacherId = bundle?.getString("teacherId").toString()
        classId = bundle?.getString("classId").toString()

        val msgEditText: EditText = view.findViewById(R.id.messageEditText)
        val ivSend: ImageView = view.findViewById(R.id.sendImageView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        progressBar = view.findViewById(R.id.chatProgressBar)

        ivSend.setOnClickListener {
            val msg = msgEditText.text.toString().trim()

            if(msg!=""){
                val msgRef = rootReference.child(NodeNames.CHATS).child(teacherId).child(classId).push()

                val hashMap: MutableMap<String, String> = HashMap()

                hashMap[NodeNames.SENDER_ID] = userID
                hashMap[NodeNames.MESSAGE] = msg
                hashMap[NodeNames.MESSAGE_ID] = msgRef.key.toString()
                hashMap[NodeNames.MSG_TIME] = System.currentTimeMillis().toString()

                msgRef.setValue(hashMap).addOnCompleteListener {
                    msgEditText.setText("")
                }
            }
        }

        chatRecyclerView = view.findViewById(R.id.messageRecyclerView)
        emptyListTextView = view.findViewById(R.id.tvNoMessage)

        chatRecyclerView.layoutManager = LinearLayoutManager(activity)

        chatModelList = ArrayList()
        chatAdapter = ChatAdapter(thisContext, chatModelList, teacherId, classId)

        chatRecyclerView.adapter = chatAdapter

        emptyListTextView.visibility = View.VISIBLE

        loadMessages()

        if (chatModelList.size > 0) {
            chatRecyclerView.scrollToPosition(chatModelList.size - 1)
            emptyListTextView.visibility = View.GONE
        }

        swipeRefreshLayout.setOnRefreshListener {
            currentPage++
            loadMessages()
        }
    }

    private fun loadMessages() {
        chatModelList.clear()
        emptyListTextView.visibility = View.VISIBLE
        val msgRef =
            rootReference.child(NodeNames.CHATS).child(teacherId).child(classId)
        val messageQuery: Query = msgRef.limitToLast(currentPage * RECORD_PER_PAGE)
        if (childEventListener != null) {
            messageQuery.removeEventListener(childEventListener!!)
        }
        childEventListener = object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot, previousChildName: String?
            ) {
                progressBar.visibility = View.GONE
                val senderId = snapshot.child(NodeNames.SENDER_ID).value.toString()
                val msg = snapshot.child(NodeNames.MESSAGE).value.toString()
                val msgId = snapshot.child(NodeNames.MESSAGE_ID).value.toString()
                val msgTime = snapshot.child(NodeNames.MSG_TIME).value.toString()
                var senderName = ""

                rootReference.child(NodeNames.USERS).child(senderId).get().addOnCompleteListener {
                    senderName = it.result.child(NodeNames.NAME).value.toString()
                    Log.i("msg", "$senderId $msg $msgId $msgTime $senderName")

                    chatModelList.add(ChatModel(senderId, msgId, msg, msgTime, senderName))
                    chatAdapter?.notifyDataSetChanged()
                    chatRecyclerView.scrollToPosition(chatModelList.size - 1)
                    swipeRefreshLayout.isRefreshing = false

                    if (chatModelList.size > 0) {
                        emptyListTextView.visibility = View.GONE
                    }
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                progressBar.visibility = View.GONE
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                loadMessages()
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {}

            override fun onCancelled(error: DatabaseError) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
        messageQuery.addChildEventListener(childEventListener as ChildEventListener)
    }
}