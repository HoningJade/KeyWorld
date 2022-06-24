package cn.edu.sjtu.keyworldteam.keyworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SelService : AppCompatActivity() {
    private val _service = MutableLiveData<String>()
    val service: LiveData<String> = _service

    fun setService(desiredService: String) {
        _service.value = desiredService
    }

    fun resetService() {
        _service.value = ""
    }

    init {
        // Set initial values for the service
        resetService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selservice)
        resetService()
    }
}