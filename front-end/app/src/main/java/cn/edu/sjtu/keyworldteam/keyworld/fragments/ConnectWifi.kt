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
import androidx.fragment.app.FragmentActivity
import cn.edu.sjtu.keyworldteam.keyworld.R
import cn.edu.sjtu.keyworldteam.keyworld.SendReview
import cn.edu.sjtu.keyworldteam.keyworld.WifiConnection

class ConnectWifi : Fragment() {

    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_connect_wifi, container, false)

        button = view.findViewById(R.id.wifiButton)
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
//                        transaction.replace(R.id.fragment_container, ConnectWifiSuccess())
//                        transaction.disallowAddToBackStack()
//                        transaction.commit()
//                    }
//                    alert.dismiss()
//                }
//            }, 3000)

            // TODO: Verify WiFi NFC tag
            startActivity(Intent(requireContext(), WifiConnection::class.java))
        }

        return view
    }

}