package cn.edu.sjtu.keyworldteam.keyworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.edu.sjtu.keyworldteam.keyworld.fragments.Checkout
import cn.edu.sjtu.keyworldteam.keyworld.fragments.ConnectWifi
import cn.edu.sjtu.keyworldteam.keyworld.fragments.HotelService
import cn.edu.sjtu.keyworldteam.keyworld.fragments.OpenKey
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigation : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    private val openKeyFragment = OpenKey()
    private val connectWifiFragment = ConnectWifi()
    private val hotelServiceFragment = HotelService()
    private val checkoutFragment = Checkout()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        replaceFragment(openKeyFragment)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_bar)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.ic_key  -> {
                    replaceFragment(openKeyFragment)
                }
                R.id.ic_wifi  -> {
                    replaceFragment(connectWifiFragment)
                }
                R.id.ic_service  -> {
                    replaceFragment(hotelServiceFragment)
                }
                R.id.ic_checkout  -> {
                    replaceFragment(checkoutFragment)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}