package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import cn.edu.sjtu.keyworldteam.keyworld.R

class HotelService : Fragment() {

    private lateinit var instructionButton: Button
    private lateinit var serviceButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hotel_service, container, false)

        instructionButton = view.findViewById(R.id.instructionButton)
        instructionButton.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragment_container, Instruction())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        serviceButton = view.findViewById(R.id.selectServiceButton)
        serviceButton.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragment_container, SelectService())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        return view
    }

}