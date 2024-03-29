package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.FragmentActivity
import cn.edu.sjtu.keyworldteam.keyworld.R
import cn.edu.sjtu.keyworldteam.keyworld.ReadInstruction
import cn.edu.sjtu.keyworldteam.keyworld.WifiConnection

class Instruction : Fragment() {

    private lateinit var button: Button
    private lateinit var returnButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_instruction, container, false)

        button = view.findViewById(R.id.instructionReaderButton)
        button.setOnClickListener {
//            val dialogBuilder = AlertDialog.Builder(requireContext())
//
//            // set message of alert dialog
//            dialogBuilder.setMessage("Please keep the phone close to the NFC tag.")
//                // set title of alert dialog
//                .setTitle("READING...")
//                // if the dialog is cancelable
//                .setCancelable(false)
//                // negative button text and action
//                .setNegativeButton("CANCEL",
//                    DialogInterface.OnClickListener { dialog, _ ->
//                        dialog.cancel()
//                    })
//
//            // create dialog box
//            val alert = dialogBuilder.create()
//            // show alert dialog
//            alert.show()
//
//            val handler = Handler(Looper.getMainLooper())
//            handler.postDelayed({
//                if (alert.isShowing) {
//                    val transaction = activity?.supportFragmentManager?.beginTransaction()
//                    if (transaction != null) {
//                        transaction.replace(R.id.fragment_container, InstructionSuccess())
//                        transaction.disallowAddToBackStack()
//                        transaction.commit()
//                    }
//                    alert.dismiss()
//                }
//            }, 3000)

            startActivity(Intent(requireContext(), ReadInstruction::class.java))
        }

        returnButton = view.findViewById(R.id.returnButton2)
        returnButton.setOnClickListener{
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