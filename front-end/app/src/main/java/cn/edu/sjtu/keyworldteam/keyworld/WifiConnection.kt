package cn.edu.sjtu.keyworldteam.keyworld

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiNetworkSuggestion
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings.*
import android.text.Editable
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import cn.edu.sjtu.keyworldteam.keyworld.databinding.ActivityMainBinding
import cn.edu.sjtu.keyworldteam.keyworld.databinding.ActivityWifiConnectionBinding
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.experimental.and



class WifiConnection : AppCompatActivity() {

    private lateinit var tvNFCContent: TextView
    private lateinit var binding: ActivityWifiConnectionBinding
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    var myTag: Tag? = null

    private lateinit var returnButton: ImageButton

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvNFCContent = binding.wifiInfo

        setWifi("Open","AndroidWifi","")

//        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
//        if (nfcAdapter == null) {
//            // Stop here, we definitely need NFC
//            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
//            finish()
//        }
//
//        //For when the activity is launched by the intent-filter for android.nfc.action.NDEF_DISCOVERE
//        readFromIntent(intent)
//        pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
//            PendingIntent.FLAG_IMMUTABLE
//        )
//        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
//        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)

        returnButton = findViewById(R.id.returnButton1)
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



            //wifi tag
            var lines = text.lines()
            var authentication = lines[0]
            var encryption = lines[1]
            var SSID = lines[2]
            var Password = lines[3]
            tvNFCContent.text = "Authentication: $authentication \n" +
                    "Encryption: $encryption \nSSID: $SSID \nPassword: $Password"


    }


    /**
     * For reading the NFC when the app is already launched
     */
    /*override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        readFromIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }
    }

    public override fun onPause() {
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
    }


    companion object {
        const val ERROR_DETECTED = "No NFC tag detected!"
    }*/

    /******************************************************************************
     * Connect to WIFI
     ****************************************************************************/

    fun setWifi(authentication: String, ssid: String, password: String) {
        val suggestions = ArrayList<WifiNetworkSuggestion>()

        //Open configuration
        if(authentication == "Open") {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10) {
            if (resultCode == RESULT_OK) {
                // user agreed to save configurations: still need to check individual results
                if (data != null && data.hasExtra(EXTRA_WIFI_NETWORK_RESULT_LIST)) {
                    for (code in data.getIntegerArrayListExtra(EXTRA_WIFI_NETWORK_RESULT_LIST)!!) {
                        when (code) {
                            ADD_WIFI_RESULT_SUCCESS ->
                                Log.i("wifi", "connect succeed")
                            ADD_WIFI_RESULT_ADD_OR_UPDATE_FAILED ->
                                Log.i("wifi", "invalid configuration")
                            ADD_WIFI_RESULT_ALREADY_EXISTS ->
                                Log.i("wifi", "already existed")
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