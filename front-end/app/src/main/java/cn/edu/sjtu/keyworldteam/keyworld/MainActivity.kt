package cn.edu.sjtu.keyworldteam.keyworld

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startRoom(view: View?) {
        var lastNameText = findViewById<EditText>(R.id.lastNameInput)
//        Toast.makeText(this, lastNameText.text, Toast.LENGTH_SHORT).show()
        var codeText = findViewById<EditText>(R.id.codeInput)
//        Toast.makeText(this, codeText.text, Toast.LENGTH_SHORT).show()

        if (lastNameText.text.isNotEmpty() && codeText.text.isNotEmpty()) {
            startActivity(Intent(this, SelService::class.java))
        }
        else {
//            Toast.makeText(this, "Required information is missing", Toast.LENGTH_SHORT).show()
            // build alert dialog
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Required information is missing.")
                // set title of alert dialog
                .setTitle("Caution")
                // if the dialog is cancelable
                .setCancelable(false)
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, _ -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // show alert dialog
            alert.show()
        }
    }
}