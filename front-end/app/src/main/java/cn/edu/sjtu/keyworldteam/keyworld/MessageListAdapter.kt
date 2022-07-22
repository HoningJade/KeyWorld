package cn.edu.sjtu.keyworldteam.keyworld

import android.provider.ContactsContract.CommonDataKinds.Relation.TYPE_FRIEND
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections.addAll

class MessageListAdapter(private val messageList: ArrayList<Message>):
    RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private fun bindHotel(item : Message.Hotel) {
            val timeText : TextView = itemView.findViewById(R.id.text_gchat_timestamp_other)
            val messageText : TextView = itemView.findViewById(R.id.text_gchat_message_other)
            timeText.text =  item.time
            messageText.text = item.text
        }

        private fun bindUser(item : Message.User) {
            val timeText : TextView = itemView.findViewById(R.id.text_gchat_timestamp_me)
            val messageText : TextView = itemView.findViewById(R.id.text_gchat_message_me)
            timeText.text =  item.time
            messageText.text = item.text
        }

        fun bind(message : Message) {
            when (message) {
                is Message.Hotel -> bindHotel(message)
                is Message.User -> bindUser(message)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_MESSAGE_SENT -> R.layout.item_chat_me
            VIEW_TYPE_MESSAGE_RECEIVED -> R.layout.item_chat_other
            else -> throw IllegalArgumentException("Invalid type")
        }

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (messageList[position]) {
            is Message.Hotel -> VIEW_TYPE_MESSAGE_RECEIVED
            else -> VIEW_TYPE_MESSAGE_SENT
        }
    }

    fun setMessage(data : ArrayList<Message>) {
        messageList.apply {
            clear()
            addAll(data)
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 0
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 1
    }
}