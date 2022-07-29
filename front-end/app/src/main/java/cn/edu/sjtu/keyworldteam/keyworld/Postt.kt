package cn.edu.sjtu.keyworldteam.keyworld

class Postt(var roomid: Int? = null,
            var requestdetail: String? = null,
            var timestamp: String? = null)

class Chat(var roomid: Int? = null,
            var chatts: String? = null)

class Review(var roomid: Int? = null,
            var rating: Float? = null,
            var review: String? = null)

object MySingleton {
    var roomid: Int = 100
    var key: String = ""
    var starttime: String = ""
    var endtime: String = ""
}