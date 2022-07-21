package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import cn.edu.sjtu.keyworldteam.keyworld.R

class LiveChat : Fragment() {

    private lateinit var returnButton: ImageButton
    private lateinit var listView: ListView

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

        listView = view.findViewById<ListView>(R.id.chatListView)

        // TODO: Get chat information from back-end
        val chatList = {
            "chat"
        }

        return view
    }

}