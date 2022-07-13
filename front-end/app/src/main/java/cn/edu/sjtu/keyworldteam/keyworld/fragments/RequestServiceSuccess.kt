package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import cn.edu.sjtu.keyworldteam.keyworld.R

class RequestServiceSuccess : Fragment() {

    private lateinit var requestDone: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_request_service_success, container, false)

        requestDone = view.findViewById(R.id.requestDoneButton)
        requestDone.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragment_container, HotelService())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        return view
    }

}