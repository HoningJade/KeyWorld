package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import cn.edu.sjtu.keyworldteam.keyworld.R

class OpenKeySuccess : Fragment() {

    private lateinit var returnButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_key_success, container, false)

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