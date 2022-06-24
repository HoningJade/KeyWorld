package cn.edu.sjtu.keyworldteam.keyworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SelService : AppCompatActivity() {
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
    }
}
