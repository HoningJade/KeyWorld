package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import cn.edu.sjtu.keyworldteam.keyworld.MySingleton
import cn.edu.sjtu.keyworldteam.keyworld.R

class OpenKeySuccess : Fragment() {

    private lateinit var returnButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_key_success, container, false)
        val tv1: TextView? = view?.findViewById(R.id.roomNum2)
        if (tv1 != null) {
            tv1.text = "Room: " + MySingleton.roomid.toString()
        }
        returnButton = view.findViewById(R.id.renewButton1)
        returnButton.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragment_container, OpenKey())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        return view
    }
}