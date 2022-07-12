package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentActivity
import cn.edu.sjtu.keyworldteam.keyworld.MySingleton
import cn.edu.sjtu.keyworldteam.keyworld.R

class OpenKey : Fragment() {

    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_open_key, container, false)
        val tv1: TextView? = view?.findViewById(R.id.roomNum1)
        if (tv1 != null) {
            tv1.text = "Room: " + MySingleton.roomid.toString()
        }
        button = view.findViewById(R.id.openKeyButton)
        button.setOnClickListener{
//            Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_SHORT).show()
            val dialogBuilder = AlertDialog.Builder(requireContext())

            // set message of alert dialog
            dialogBuilder.setMessage("Please keep the phone close to the door.")
                // set title of alert dialog
                .setTitle("OPENING...")
                // if the dialog is cancelable
                .setCancelable(false)
                // negative button text and action
                .setNegativeButton("CANCEL",
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        if (transaction != null) {
                            transaction.replace(R.id.fragment_container, OpenKeyFail())
                            transaction.disallowAddToBackStack()
                            transaction.commit()
                        }
                    })

            // create dialog box
            val alert = dialogBuilder.create()
            // show alert dialog
            alert.show()

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (alert.isShowing) {
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    if (transaction != null) {
                        transaction.replace(R.id.fragment_container, OpenKeySuccess())
                        transaction.disallowAddToBackStack()
                        transaction.commit()
                    }
                    alert.dismiss()
                }
            }, 3000)

            // TODO: Verify the NFC code
        }

        return view
    }

}