package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sjtu.keyworldteam.keyworld.Message
import cn.edu.sjtu.keyworldteam.keyworld.MessageListAdapter
import cn.edu.sjtu.keyworldteam.keyworld.R

class LiveChat : Fragment() {

    private lateinit var returnButton: ImageButton

    private lateinit var sendButton: Button

    private lateinit var mMessageAdapter: MessageListAdapter
    private lateinit var mMessageRecycler: RecyclerView
    private lateinit var mMessageArrayList: ArrayList<Message>

    lateinit var messages : Array<String>
    lateinit var times : Array<String>
    lateinit var fromUsers : Array<Boolean>

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
        sendButton.setOnClickListener {
            val sendText = view.findViewById<EditText>(R.id.edit_gchat_message)
            // TODO: Send message and refresh message
        }

        return view
    }

    private fun dataInitialize() {
        mMessageArrayList = arrayListOf()

        // TODO: Get chat information from back-end

        messages = arrayOf(
            getString(R.string.my_message),
            getString(R.string.other_message)
        )

        times = arrayOf(
            getString(R.string.my_time),
            getString(R.string.other_time)
        )

        fromUsers = arrayOf(
            true,
            false
        )

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

