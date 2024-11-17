package com.example.skegoapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.skegoapp.R
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private val splashDuration = 2000L // Durasi dalam milidetik (2 detik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Menjalankan coroutine untuk menunda layar splash
        GlobalScope.launch {
            delay(splashDuration)
            withContext(Dispatchers.Main) {
                // Pindah ke MainActivity
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    //animasi
    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}
