package cn.edu.sjtu.keyworldteam.keyworld

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.edu.sjtu.keyworldteam.keyworld.databinding.ActivityMainBinding
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.*
import android.os.Parcelable
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import cn.edu.sjtu.keyworldteam.keyworld.databinding.ActivityReadInstructionBinding
import cn.edu.sjtu.keyworldteam.keyworld.databinding.ActivityWifiConnectionBinding
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.nio.file.Files.lines
import kotlin.experimental.and

class NFCReader : AppCompatActivity()  {

    private lateinit var tvNFCContent: TextView
    private lateinit var tvNFCTag: TextView
    private lateinit var binding: ActivityReadInstructionBinding
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    var myTag: Tag? = null

    private lateinit var returnButton: ImageButton

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadInstructionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvNFCContent = binding.equipmentInstruction
        tvNFCTag = binding.equipmentName

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            finish()
        }

        //For when the activity is launched by the intent-filter for android.nfc.action.NDEF_DISCOVERE
        readFromIntent(intent)
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)

        returnButton = findViewById(R.id.returnButton3)
        returnButton.setOnClickListener {
            finish()
        }
    }

    /******************************************************************************
     * Read From NFC Tag
     ****************************************************************************/
    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            myTag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            var msgs = mutableListOf<NdefMessage>()
            if (rawMsgs != null) {
                for (i in rawMsgs.indices) {
                    msgs.add(i, rawMsgs[i] as NdefMessage)
                }
                buildTagViews(msgs.toTypedArray())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun buildTagViews(msgs: Array<NdefMessage>) {
        if (msgs == null || msgs.isEmpty()) return
        var text = ""
        val payload = msgs[0].records[0].payload
        val textEncoding: Charset = if ((payload[0] and 128.toByte()).toInt() == 0) Charsets.UTF_8 else Charsets.UTF_16 // Get the Text Encoding
        val languageCodeLength: Int = (payload[0] and 51).toInt() // Get the Language Code, e.g. "en"
        try {
            // Get the Text
            text = String(
                payload,
                languageCodeLength + 1,
                payload.size - languageCodeLength - 1,
                textEncoding
            )
        } catch (e: UnsupportedEncodingException) {
            Log.e("UnsupportedEncoding", e.toString())
        }

        val lines = text.lines()
        val flag = lines[0]

        if (flag == "instr"){
            //instruction tag

            //extract tag name
            val title = lines[1]
            //extract instruction text
            val instruction = text.subSequence(flag.length + title.length + 2,text.length)

            tvNFCTag.text = title
            tvNFCContent.text = "$instruction"

        }else if (flag == "wifi"){
            //wifi tag
            val authentication = lines[1]
            val encryption = lines[2]
            val SSID = lines[3]
            val Password = lines[4]

            tvNFCTag.text = "wifi"
            tvNFCContent.text = "Authentication: $authentication \n" +
                    "Encryption: $encryption \nSSID: $SSID \nPassword: $Password"
        }else{
            return
        }

    }

}