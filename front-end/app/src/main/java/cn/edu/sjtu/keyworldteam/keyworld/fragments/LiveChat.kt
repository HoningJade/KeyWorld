package cn.edu.sjtu.keyworldteam.keyworld.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sjtu.keyworldteam.keyworld.*
import cn.edu.sjtu.keyworldteam.keyworld.PostStore.sendChat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.Typography.times

class LiveChat : Fragment() {

    private lateinit var returnButton: ImageButton

    private lateinit var sendButton: Button
    private lateinit var sendText: EditText

    private lateinit var mMessageAdapter: MessageListAdapter
    private lateinit var mMessageRecycler: RecyclerView
    private lateinit var mMessageArrayList: ArrayList<Message>
    var msgCount = 0

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_live_chat, container, false)

        returnButton = view.findViewById(R.id.returnButton5)
        returnButton.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragment_container, SelectService())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        mMessageRecycler = view.findViewById(R.id.chatRecycler)
        mMessageRecycler.layoutManager = layoutManager
        mMessageRecycler.setHasFixedSize(true)
        mMessageAdapter = MessageListAdapter(mMessageArrayList)
        mMessageRecycler.adapter = mMessageAdapter

        sendButton = view.findViewById(R.id.button_gchat_send)
        sendText = view.findViewById(R.id.edit_gchat_message)
        sendButton.isEnabled = false
        sendButton.alpha = 0.5F

        sendText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendButton.isEnabled = s.toString().isNotEmpty()
                if (s.toString().isNotEmpty()) {
                    sendButton.alpha = 1.0F
                }
                else {
                    sendButton.alpha = 0.5F
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        sendButton.setOnClickListener {
            val messageText = sendText.text.toString() // Message

            val timestamp = Date()
            val dateFormatter = SimpleDateFormat("KK:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(timestamp) // Time
//            Toast.makeText(requireContext(), "Time is: $dateString", Toast.LENGTH_SHORT).show()

            // Send message to back-end
            val msg = Chat(
                roomid = MySingleton.roomid,
                chatts = messageText
            )
            sendChat(requireContext(), msg)

            // addMessage(messageText, dateString, true)

            sendText.text.clear()
//            Toast.makeText(requireContext(), "Message is: $messageText", Toast.LENGTH_SHORT).show()
        }

        // Use addMessage(messageText, timeText, false) to add message bubble
        // Receive message from back-end
        lifecycleScope.launch {
            while(true) {
                updateMsg()
                mMessageAdapter = MessageListAdapter(mMessageArrayList)
                mMessageRecycler.adapter = mMessageAdapter
                delay(1000)
            }
        }

        return view
    }

    private fun addMessage(messageText: String, timeText: String, fromUser: Boolean) {
        if (fromUser) {
            mMessageArrayList.add(Message.User(messageText, timeText))
        }
        else {
            mMessageArrayList.add(Message.Hotel(messageText, timeText))
        }
        mMessageAdapter = MessageListAdapter(mMessageArrayList)
        mMessageRecycler.adapter = mMessageAdapter
    }

    private fun dataInitialize() {
        mMessageArrayList = arrayListOf()

        val nFields = 2
        // Get chat information from back-end
        val builder  = (PostStore.serverUrl +"sendChat/").toHttpUrlOrNull()?.newBuilder()
        if (builder != null) {
            val url: String = builder.build().toString()
            val request = okhttp3.Request.Builder()
                .url(url)
                .build()

            PostStore.client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("sendChat", "Failed GET request")
                }
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        // val msgReceived = try { JSONObject(response.body?.string() ?: "").getJSONArray("msg") } catch (e: JSONException) { JSONArray() }
                        // val msgReceived = response.body?.string().toString()
                        //if (msgReceived.length() == 4) {
                        val msgReceived = JSONTokener(response.body?.string()).nextValue() as JSONObject
                        // msgReceived = msgReceived.getJSONObject("msg")
                        val msgList = msgReceived.getJSONArray("chatts")
                        msgCount = msgList.length()
                        for (i in 0 until msgList.length()) {
                            val chattEntry = msgList[i] as JSONArray
                            if (chattEntry.length() == nFields) {
                                if (chattEntry[0] == "customer") {
                                    val message = Message.User(chattEntry[1] as String, getString(R.string.my_time))
                                    mMessageArrayList.add(message)
                                } else {
                                    val message = Message.Hotel(chattEntry[1] as String, getString(R.string.my_time))
                                    mMessageArrayList.add(message)
                                }
                            } else {
                                Log.e("getChatts", "Received unexpected number of fields " + chattEntry.length().toString() + " instead of " + nFields.toString())
                            }
                        }
                    }
                }
            })

        }

/*        // Test messages //
        val messages = arrayOf(
            getString(R.string.my_message),
            getString(R.string.other_message)
        )

        val times = arrayOf(
            getString(R.string.my_time),
            getString(R.string.other_time)
        )

        val fromUsers = arrayOf(
            true,
            false
        )
        // Test messages //

        for (i in messages.indices) {
            if (fromUsers[i]) {
                val message = Message.User(messages[i], times[i])
                mMessageArrayList.add(message)
            }
            else {
                val message = Message.Hotel(messages[i], times[i])
                mMessageArrayList.add(message)
            }
        }*/
    }

    suspend fun updateMsg() {
        val nFields = 2
        // Get chat information from back-end
        val builder  = (PostStore.serverUrl +"sendChat/").toHttpUrlOrNull()?.newBuilder()
        if (builder != null) {
            val url: String = builder.build().toString()
            val request = okhttp3.Request.Builder()
                .url(url)
                .build()

            PostStore.client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("sendChat", "Failed GET request")
                }
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        // val msgReceived = try { JSONObject(response.body?.string() ?: "").getJSONArray("msg") } catch (e: JSONException) { JSONArray() }
                        // val msgReceived = response.body?.string().toString()
                        //if (msgReceived.length() == 4) {
                        val msgReceived = JSONTokener(response.body?.string()).nextValue() as JSONObject
                        // msgReceived = msgReceived.getJSONObject("msg")
                        val msgList = msgReceived.getJSONArray("chatts")
                        Log.i("msg number: ", msgList.length().toString())
                        if (msgCount < msgList.length()){
                            for (i in msgCount until msgList.length()) {
                                val chattEntry = msgList[i] as JSONArray
                                if (chattEntry.length() == nFields) {
                                    if (chattEntry[0] == "customer") {
                                        Log.i("customer msg: ", chattEntry[1] as String)
                                        val message = Message.User(chattEntry[1] as String, getString(R.string.my_time))
                                        mMessageArrayList.add(message)
                                    } else {
                                        Log.i("hotel msg: ", chattEntry[1] as String)
                                        val message = Message.Hotel(chattEntry[1] as String, getString(R.string.my_time))
                                        mMessageArrayList.add(message)
                                    }
                                } else {
                                    Log.e("getChatts", "Received unexpected number of fields " + chattEntry.length().toString() + " instead of " + nFields.toString())
                                }
                            }
                        }
                        msgCount = msgList.length()
                    }
                }
            })
        }
    }
}