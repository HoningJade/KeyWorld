package cn.edu.sjtu.keyworldteam.keyworld

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.full.declaredMemberProperties

object PostStore {
    private val _postts = arrayListOf<Postt>()
    val postts: List<Postt> = _postts
    private val nFields = Postt::class.declaredMemberProperties.size

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
            { error -> Log.e("postRequest", error.localizedMessage ?: "JsonObjectRequest error") }
        )

        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(postRequest)
    }
}