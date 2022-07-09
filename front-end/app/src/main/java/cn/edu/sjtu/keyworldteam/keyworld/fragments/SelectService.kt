package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.edu.sjtu.keyworldteam.keyworld.R

class SelectService : Fragment() {

    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton
    private lateinit var button: Button

    private val _service = MutableLiveData<String>()

    val service: LiveData<String> = _service

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
        button = view.findViewById(R.id.request_service)

        // Actions to be performed
        // when Submit button is clicked
        button.setOnClickListener {

            // Getting the checked radio button id
            // from the radio group
            val selectedOption: Int = radioGroup!!.checkedRadioButtonId

            // Assigning id of the checked radio button
            radioButton = view.findViewById(selectedOption)

            // Displaying text of the checked radio button in the form of toast
            setService(radioButton.text as String)
            Toast.makeText(requireContext(), _service.value, Toast.LENGTH_SHORT).show()

            // TODO: Send service to back-end
        }

        return view
    }

}