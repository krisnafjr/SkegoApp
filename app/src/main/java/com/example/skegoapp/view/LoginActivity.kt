package com.example.skegoapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.text.method.LinkMovementMethod
import com.example.skegoapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Temukan TextView dengan ID yang sesuai
        val textView: TextView = findViewById(R.id.textViewCreateAccount)

        // Membuat SpannableString untuk teks
        val spannableString = SpannableString("Don't Have Account? Create one!")

        // Mengatur warna untuk "Create one!" menjadi orange
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.orange_text)) // Warna orange
        spannableString.setSpan(colorSpan, 20, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Menambahkan teks ke TextView dan membuatnya dapat diklik
        textView.text = spannableString
        textView.isClickable = true
        textView.movementMethod = LinkMovementMethod.getInstance()

        // Menambahkan listener untuk klik pada "Create one!"
        textView.setOnClickListener {
            // Aksi ketika teks "Create one!" diklik, misalnya menavigasi ke halaman register
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
