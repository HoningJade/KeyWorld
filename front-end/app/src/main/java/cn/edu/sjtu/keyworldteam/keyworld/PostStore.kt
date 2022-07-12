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

object PostStore {
    private val nFields = Postt::class.declaredMemberProperties.size
    private val client = OkHttpClient()
    private lateinit var queue: RequestQueue
    private const val serverUrl = "https://18.116.30.203/"

    fun postMsg(context: Context, postt: Postt) {
        val jsonObj = mapOf(
            "roomid" to postt.roomid,
            "requestdetail" to postt.requestdetail,
            "timestamp" to postt.timestamp
        )
        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            serverUrl+"postmsg/", JSONObject(jsonObj),
            { Log.d("postRequest", "request posted!") },
            { error -> Log.e("postRequest", error.localizedMessage ?: "JsonObjectRequest error")}
        )
        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(postRequest)
    }

    fun getMsg(text1: Editable, text2: Editable) {
        val builder  = (serverUrl+"getmsg/").toHttpUrlOrNull()?.newBuilder()
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
                        val msgReceived = try { JSONObject(response.body?.string() ?: "").getJSONArray("msg") } catch (e: JSONException) { JSONArray() }
                        if (msgReceived.length() == 4) {
                            MySingleton.roomid = msgReceived[0].toString().toInt()
                            MySingleton.key = msgReceived[1].toString()
                            MySingleton.starttime = msgReceived[3].toString()
                            MySingleton.endtime = msgReceived[4].toString()
                        } else {
                            Log.e("getMsg", "Received unexpected number of fields " + msgReceived.length().toString() + " instead of " + nFields.toString())
                        }
                    }
                }
            })

        }
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