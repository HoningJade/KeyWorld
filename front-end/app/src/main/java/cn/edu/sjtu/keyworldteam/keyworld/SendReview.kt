package cn.edu.sjtu.keyworldteam.keyworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext

class SendReview : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_review)

        button = findViewById(R.id.sendReviewButton)
        button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}