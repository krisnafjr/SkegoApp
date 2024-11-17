package com.example.skegoapp.view

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skegoapp.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Temukan TextView dengan ID yang sesuai
        val textView: TextView = findViewById(R.id.textViewLoginAccount)

        // Membuat SpannableString untuk teks
        val spannableString = SpannableString("Already Have Account? Sign In!")

        // Mengatur warna untuk "Sign In!" menjadi orange
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.orange_text)) // Warna orange
        spannableString.setSpan(colorSpan, 20, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Menambahkan teks ke TextView dan membuatnya dapat diklik
        textView.text = spannableString
        textView.isClickable = true
        textView.movementMethod = LinkMovementMethod.getInstance()

        // Menambahkan listener untuk klik pada "Create one!"
        textView.setOnClickListener {
            // Aksi ketika teks "Create one!" diklik, misalnya menavigasi ke halaman register
            val intent = Intent(this@RegisterActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}