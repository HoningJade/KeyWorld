package cn.edu.sjtu.keyworldteam.keyworld

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

class SendReview : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_review)

        button = findViewById(R.id.sendReviewButton)
        val rBar = findViewById<RatingBar>(R.id.ratingBar)
        button.setOnClickListener {
            val msg = rBar.rating.toString() // Rating number
//            Toast.makeText(this, "Rating is: $msg", Toast.LENGTH_SHORT).show()

            val reviewText = findViewById<EditText>(R.id.reviewInput) // Review text
            if (reviewText.text.isNotEmpty()) {
                // TODO: Send rating and reviews
                finish()
            }
            else {
                val dialogBuilder = AlertDialog.Builder(this)

                // set message of alert dialog
                dialogBuilder.setMessage("You have not filled out a review.")
                    // set title of alert dialog
                    .setTitle("CAUTION")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // negative button text and action
                    .setNegativeButton("CANCEL", DialogInterface.OnClickListener {
                            dialog, _ -> dialog.cancel()
                    })

                // create dialog box
                val alert = dialogBuilder.create()
                // show alert dialog
                alert.show()
            }

        }
    }
}