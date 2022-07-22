package cn.edu.sjtu.keyworldteam.keyworld

sealed class Message {
    data class Hotel(
        val text : String,
        val time : String
    ) : Message()

    data class User(
        val text : String,
        val time : String
    ) : Message()
}
