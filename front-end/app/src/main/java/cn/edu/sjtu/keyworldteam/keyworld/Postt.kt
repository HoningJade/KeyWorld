package cn.edu.sjtu.keyworldteam.keyworld

class Postt(var roomid: Int? = null,
            var requestdetail: String? = null,
            var timestamp: String? = null)

object MySingleton {
    var roomid: Int = 100
    var key: String = ""
    var starttime: String = ""
    var endtime: String = ""
}