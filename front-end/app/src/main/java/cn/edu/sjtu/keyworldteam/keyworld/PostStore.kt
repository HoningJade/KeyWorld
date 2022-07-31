package cn.edu.sjtu.keyworldteam.keyworld

import android.content.Context
import android.telecom.Call
import android.text.Editable
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.reflect.full.declaredMemberProperties
import okhttp3.*
import org.json.JSONTokener

object PostStore {
    private val nFields = Postt::class.declaredMemberProperties.size
    val client = OkHttpClient()
    private lateinit var queue: RequestQueue
    const val serverUrl = "https://18.116.30.203/"

    fun postMsg(context: Context, postt: Postt) {
        val jsonObj = mapOf(
            "roomid" to postt.roomid,
            "requestdetail" to postt.requestdetail,
            "timestamp" to  postt.timestamp //"17:18:30.698589"
        )
        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            serverUrl+"roomServiceRequest/", JSONObject(jsonObj),
            { Log.d("postRequest", "request posted!") },
            { error -> Log.e("postRequest", error.localizedMessage ?: "JsonObjectRequest error")}
        )
        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(postRequest)
    }

    fun getMsg(text1: Editable, text2: Editable) {
        val builder  = (serverUrl+"keyFetch/").toHttpUrlOrNull()?.newBuilder()
        if (builder != null) {
            builder.addQueryParameter("lastname", text1.toString())
            builder.addQueryParameter("code", text2.toString())
            val url: String = builder.build().toString()
            val request = okhttp3.Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("getMsg", "Failed GET request")
                }
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        // val msgReceived = try { JSONObject(response.body?.string() ?: "").getJSONArray("msg") } catch (e: JSONException) { JSONArray() }
                        // val msgReceived = response.body?.string().toString()
                        //if (msgReceived.length() == 4) {
                        val msgReceived = JSONTokener(response.body?.string()).nextValue() as JSONObject
                        // msgReceived = msgReceived.getJSONObject("msg")
                        MySingleton.roomid = msgReceived.getInt("room_number")
                        Log.i("roomid: ", MySingleton.roomid.toString())
                        MySingleton.key = msgReceived.getString("key")
                        Log.i("key: ", MySingleton.key)
                        MySingleton.starttime = msgReceived.getString("start_date")
                        Log.i("starttime: ", MySingleton.starttime)
                        MySingleton.endtime = msgReceived.getString("end_date")
                        Log.i("endtime: ", MySingleton.endtime)
                        //} else {
                        //    Log.e("getMsg", "Received unexpected number of fields " + msgReceived.length().toString() + " instead of " + nFields.toString())
                        //}
                    }
                }
            })

        }
    }

    fun postReview(context: Context, postt: Review) {
        val jsonObj = mapOf(
            "room_number" to postt.roomid,
            "rating" to postt.rating,
            "review" to postt.review
        )
        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            serverUrl+"receiveReview/", JSONObject(jsonObj),
            { Log.d("postReview", "review posted!") },
            { error -> Log.e("postReview", error.localizedMessage ?: "JsonObjectRequest error")}
        )
        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(postRequest)
    }

    fun sendChat(context: Context, postt: Chat) {
        val jsonObj = mapOf(
            "room_number" to postt.roomid,
            "chatts" to postt.chatts,
        )
        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            serverUrl+"receiveChat/", JSONObject(jsonObj),
            { Log.d("sendChat", "chat posted!") },
            { error -> Log.e("sendChat", error.localizedMessage ?: "JsonObjectRequest error")}
        )
        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(postRequest)
    }

    /*fun getMsg(context: Context, url: String, completion: () -> Unit) {
        val getRequest = JsonObjectRequest(url,
            { response ->
                val msgReceived = try { response.getJSONArray("msg") } catch (e: JSONException) { JSONArray() }
                if (msgReceived.length() == 4) {
                    MySingleton.roomid = msgReceived[0].toString().toInt()
                    MySingleton.key = msgReceived[1].toString()
                    MySingleton.starttime = msgReceived[3].toString()
                    MySingleton.endtime = msgReceived[4].toString()
                } else {
                    Log.e("getChatts", "Received unexpected number of fields: " + msgReceived.length().toString() + " instead of " + nFields.toString())
                }
                completion()
            }, { completion() }
        )
        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(getRequest)
    }*/

}