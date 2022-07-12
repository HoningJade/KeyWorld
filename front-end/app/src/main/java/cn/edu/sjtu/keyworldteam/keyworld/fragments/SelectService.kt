package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.edu.sjtu.keyworldteam.keyworld.MySingleton
import cn.edu.sjtu.keyworldteam.keyworld.PostStore.postMsg
import cn.edu.sjtu.keyworldteam.keyworld.Postt
import cn.edu.sjtu.keyworldteam.keyworld.R
import java.time.Instant
import java.time.format.DateTimeFormatter


class SelectService : Fragment() {

    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton
    private lateinit var requestButton: Button

    private val _service = MutableLiveData<String>()

    val service: LiveData<String> = _service

    private lateinit var liveChatButton: Button

    fun setService(desiredService: String) {
        _service.value = desiredService
    }

    fun resetService() {
        _service.value = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select_service, container, false)

        resetService()

        // Assigning id of RadioGroup
        radioGroup = view.findViewById(R.id.service_options)

        // Assigning id of Submit button
        requestButton = view.findViewById(R.id.requestService)

        // Actions to be performed
        // when Submit button is clicked
        requestButton.setOnClickListener {

            // Getting the checked radio button id
            // from the radio group
            val selectedOption: Int = radioGroup!!.checkedRadioButtonId

            if (selectedOption != -1) {
                // Assigning id of the checked radio button
                radioButton = view.findViewById(selectedOption)

                // Displaying text of the checked radio button in the form of toast
                setService(radioButton.text as String)
                Toast.makeText(requireContext(), _service.value, Toast.LENGTH_SHORT).show()

                // Send service request to back-end
                val msg = Postt(
                    roomid = MySingleton.roomid,
                    requestdetail = radioButton.text.toString(),
                    timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
                )
                postMsg(requireContext(), msg)

                val fragment = RequestServiceSuccess()
                (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit()
            } else {
//                Toast.makeText(requireContext(), "None is selected", Toast.LENGTH_SHORT).show()
                val dialogBuilder = AlertDialog.Builder(requireContext())

                // set message of alert dialog
                dialogBuilder.setMessage("You haven’t select any service.")
                    // set title of alert dialog
                    .setTitle("CAUTION")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // negative button text and action
                    .setNegativeButton("CANCEL",
                        DialogInterface.OnClickListener { dialog, _ ->
                            dialog.cancel()
                        })

                // create dialog box
                val alert = dialogBuilder.create()
                // show alert dialog
                alert.show()
            }
        }

        liveChatButton = view.findViewById(R.id.liveChat)

        liveChatButton.setOnClickListener {
            val fragment = LiveChat()
            (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment).commit()
        }

        return view
    }

}