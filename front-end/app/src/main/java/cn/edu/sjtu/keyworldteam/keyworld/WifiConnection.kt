package cn.edu.sjtu.keyworldteam.keyworld

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import cn.edu.sjtu.keyworldteam.keyworld.databinding.ActivityMainBinding
import cn.edu.sjtu.keyworldteam.keyworld.databinding.ActivityWifiConnectionBinding
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.experimental.and



class WifiConnection : AppCompatActivity() {

    private lateinit var tvNFCContent: TextView
<<<<<<< Updated upstream
    private lateinit var binding: ActivityWifiConnectionBinding
=======
    private lateinit var tvNFCTag: TextView
    private lateinit var binding: ActivityWifiConnectionBinding
    private lateinit var binding1: ActivityReadInstructionBinding
    lateinit var authentication: String
    lateinit var encryption: String
    lateinit var ssid: String
    lateinit var password: String
>>>>>>> Stashed changes
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    var myTag: Tag? = null

    private lateinit var returnButton: ImageButton

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< Updated upstream
        binding = ActivityWifiConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvNFCContent = binding.wifiInfo
=======
//        binding = ActivityWifiConnectionBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        tvNFCContent = binding.wifiInfo

        //authentication = "open"
        //ssid = "AndroidWifi"
        //password = ""
        //setWifi(authentication, ssid, password)

//
>>>>>>> Stashed changes


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

<<<<<<< Updated upstream
        returnButton = findViewById(R.id.returnButton1)
        returnButton.setOnClickListener {
            finish()
        }
=======


//        returnButton = findViewById(R.id.returnButton1)
//        returnButton.setOnClickListener {
//            finish()
//        }
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream



            //wifi tag
            var lines = text.lines()
            var authentication = lines[0]
            var encryption = lines[1]
            var SSID = lines[2]
            var Password = lines[3]
            tvNFCContent.text = "Authentication: $authentication \n" +
                    "Encryption: $encryption \nSSID: $SSID \nPassword: $Password"
    }
=======
            //wifi tag
            /*var lines = text.lines()
            authentication = lines[1]
            encryption = lines[2]
            ssid = lines[3]
            password = lines[4]

            setWifi(authentication, ssid, password)*/

        val lines = text.lines()
        val flag = lines[0]
>>>>>>> Stashed changes

        if (flag == "instr"){
            //instruction tag

            binding1 = ActivityReadInstructionBinding.inflate(layoutInflater)
            setContentView(binding1.root)

            tvNFCContent = binding1.equipmentInstruction
            tvNFCTag = binding1.equipmentName
            //extract tag name
            val title = lines[1]
            //extract instruction text
            val instruction = text.subSequence(flag.length + title.length + 2,text.length)

            tvNFCTag.text = title
            tvNFCContent.text = "$instruction"


        }else if (flag == "wifi"){
            //wifi tag
            var lines = text.lines()
            authentication = lines[1]
            encryption = lines[2]
            ssid = lines[3]
            password = lines[4]

            setWifi(authentication, ssid, password)
        }else{
            return
        }

    }



<<<<<<< Updated upstream
    companion object {
        const val ERROR_DETECTED = "No NFC tag detected!"
    }*/
}
=======


/******************************************************************************
     * Connect to WIFI (support authentication method: open(without password) / WPA2
     ****************************************************************************/


    fun setWifi(authentication: String, ssid: String, password: String) {
        val suggestions = ArrayList<WifiNetworkSuggestion>()

        //Open configuration
        if(authentication == "open") {
            suggestions.add(
                WifiNetworkSuggestion.Builder()
                    .setSsid(ssid)
                    .build()
            )
        }

        // WPA2 configuration
        if(authentication == "WPA2") {
            suggestions.add(
                WifiNetworkSuggestion.Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password)
                    .build()
            )
        }

        // Create intent
        val bundle = Bundle()
        bundle.putParcelableArrayList(EXTRA_WIFI_NETWORK_LIST, suggestions)
        val intent = Intent(ACTION_WIFI_ADD_NETWORKS)
        intent.putExtras(bundle)

        // Launch intent
        startActivityForResult(intent, 10)
    }


/******************************************************************************
     * user reaction to the connection
     ****************************************************************************/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10) {
            if (resultCode == RESULT_OK) {
                // user agreed to save configurations: still need to check individual results
                if (data != null && data.hasExtra(EXTRA_WIFI_NETWORK_RESULT_LIST)) {
                    for (code in data.getIntegerArrayListExtra(EXTRA_WIFI_NETWORK_RESULT_LIST)!!) {
                        when (code) { //TODO: link to connection result page
                            ADD_WIFI_RESULT_SUCCESS -> {
                                binding = ActivityWifiConnectionBinding.inflate(layoutInflater)
                                setContentView(binding.root)
                                binding.wifiInfo.text = "SSID: $ssid \nPassword: $password"
                                Log.i("wifi", "connect succeed")

                                returnButton = findViewById(R.id.returnButton1)
                                returnButton.setOnClickListener {
                                    finish()
                                }
                            }
                            ADD_WIFI_RESULT_ADD_OR_UPDATE_FAILED ->
                                Log.i("wifi", "invalid configuration")
                            ADD_WIFI_RESULT_ALREADY_EXISTS -> {
                                binding = ActivityWifiConnectionBinding.inflate(layoutInflater)
                                setContentView(binding.root)
                                binding.wifiInfo.text = "SSID: $ssid \nPassword: $password"
                                Log.i("wifi", "already existed")

                                returnButton = findViewById(R.id.returnButton1)
                                returnButton.setOnClickListener {
                                    finish()
                                }
                            }
                            else ->
                                Log.e("wifi", "something wrong")
                        }
                    }
                }
            } else {
                Log.i("wifi", "user declined")
            }
        }
    }



}
>>>>>>> Stashed changes
