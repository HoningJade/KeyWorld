package cn.edu.sjtu.keyworldteam.keyworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SelService : AppCompatActivity() {
    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton
    private lateinit var button: Button

    private val _service = MutableLiveData<String>()

    // android:checked="@{service.equals(@string/service1)}"
    val service: LiveData<String> = _service

    fun setService(desiredService: String) {
        _service.value = desiredService
    }

    fun resetService() {
        _service.value = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selservice)

        resetService()

        // Assigning id of RadioGroup
        radioGroup = findViewById(R.id.service_options)

        // Assigning id of Submit button
        button = findViewById(R.id.request_service)

        // Actions to be performed
        // when Submit button is clicked
        button.setOnClickListener {

            // Getting the checked radio button id
            // from the radio group
            val selectedOption: Int = radioGroup!!.checkedRadioButtonId

            // Assigning id of the checked radio button
            radioButton = findViewById(selectedOption)

            // Displaying text of the checked radio button in the form of toast
            setService(radioButton.text as String)
            Toast.makeText(baseContext, _service.value, Toast.LENGTH_SHORT).show()
        }
    }
}
