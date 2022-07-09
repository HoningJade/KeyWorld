package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import cn.edu.sjtu.keyworldteam.keyworld.MainActivity
import cn.edu.sjtu.keyworldteam.keyworld.R
import cn.edu.sjtu.keyworldteam.keyworld.SendReview

class Checkout : Fragment() {

    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_checkout, container, false)

        button = view.findViewById(R.id.checkConfirmButton)
        button.setOnClickListener {
            startActivity(Intent(requireContext(), SendReview::class.java))
        }

        return view
    }

}