package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sjtu.keyworldteam.keyworld.*
import cn.edu.sjtu.keyworldteam.keyworld.PostStore.sendChat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LiveChat : Fragment() {

    private lateinit var returnButton: ImageButton

    private lateinit var sendButton: Button
    private lateinit var sendText: EditText

    private lateinit var mMessageAdapter: MessageListAdapter
    private lateinit var mMessageRecycler: RecyclerView
    private lateinit var mMessageArrayList: ArrayList<Message>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_live_chat, container, false)

        returnButton = view.findViewById(R.id.returnButton5)
        returnButton.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragment_container, SelectService())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        mMessageRecycler = view.findViewById(R.id.chatRecycler)
        mMessageRecycler.layoutManager = layoutManager
        mMessageRecycler.setHasFixedSize(true)
        mMessageAdapter = MessageListAdapter(mMessageArrayList)
        mMessageRecycler.adapter = mMessageAdapter

        sendButton = view.findViewById(R.id.button_gchat_send)
        sendText = view.findViewById(R.id.edit_gchat_message)
        sendButton.isEnabled = false
        sendButton.alpha = 0.5F

        sendText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendButton.isEnabled = s.toString().isNotEmpty()
                if (s.toString().isNotEmpty()) {
                    sendButton.alpha = 1.0F
                }
                else {
                    sendButton.alpha = 0.5F
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        sendButton.setOnClickListener {
            val messageText = sendText.text.toString() // Message

            val timestamp = Date()
            val dateFormatter = SimpleDateFormat("KK:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(timestamp) // Time
//            Toast.makeText(requireContext(), "Time is: $dateString", Toast.LENGTH_SHORT).show()

            // Send message to back-end
            val msg = Chat(
                roomid = MySingleton.roomid,
                chatts = messageText
            )
            sendChat(requireContext(), msg)

            addMessage(messageText, dateString, true)

            sendText.text.clear()
//            Toast.makeText(requireContext(), "Message is: $messageText", Toast.LENGTH_SHORT).show()
        }

        // TODO: Receive message from back-end
        // TODO: Use addMessage(messageText, timeText, false) to add message bubble

        return view
    }

    private fun addMessage(messageText: String, timeText: String, fromUser: Boolean) {
        if (fromUser) {
            mMessageArrayList.add(Message.User(messageText, timeText))
        }
        else {
            mMessageArrayList.add(Message.Hotel(messageText, timeText))
        }
        mMessageAdapter = MessageListAdapter(mMessageArrayList)
        mMessageRecycler.adapter = mMessageAdapter
    }

    private fun dataInitialize() {
        mMessageArrayList = arrayListOf()

        // TODO: Get chat information from back-end

        // Test messages //
        val messages = arrayOf(
            getString(R.string.my_message),
            getString(R.string.other_message)
        )

        val times = arrayOf(
            getString(R.string.my_time),
            getString(R.string.other_time)
        )

        val fromUsers = arrayOf(
            true,
            false
        )
        // Test messages //

        for (i in messages.indices) {
            if (fromUsers[i]) {
                val message = Message.User(messages[i], times[i])
                mMessageArrayList.add(message)
            }
            else {
                val message = Message.Hotel(messages[i], times[i])
                mMessageArrayList.add(message)
            }
        }
    }
}

