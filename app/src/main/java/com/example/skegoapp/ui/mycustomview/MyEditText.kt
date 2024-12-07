package com.example.skegoapp.ui.mycustomview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.skegoapp.R

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private val clearButtonImage: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_close_black_24)
            ?: throw IllegalArgumentException("Drawable not found")
    private var inputType: InputType? = null

    init {
        setOnTouchListener(this)

        // Listener untuk menangani perubahan teks
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && (inputType == InputType.USERNAME || inputType == InputType.EMAIL)) {
                    showClearButton()
                } else {
                    hideClearButton()
                }
                validateInput(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun validateInput(s: CharSequence) {
        when (inputType) {
            InputType.USERNAME -> {
                error = if (s.isEmpty()) "Username tidak boleh kosong" else null
            }

            InputType.EMAIL -> {
                error = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s)
                        .matches()
                ) "Email tidak valid" else null
            }

            InputType.PASSWORD -> {
                error = if (s.length < 8) "Password tidak boleh kurang dari 8 karakter" else null
            }

            else -> {}
        }
    }

    fun setInputType(type: InputType) {
        this.inputType = type
        if (type == InputType.PASSWORD) {
            hideClearButton() // Pastikan tombol hapus disembunyikan untuk input password
        }
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (inputType != InputType.PASSWORD && compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            val isClearButtonClicked: Boolean

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                isClearButtonClicked = event.x < clearButtonEnd
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                isClearButtonClicked = event.x > clearButtonStart
            }

            if (isClearButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        showClearButton()
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        text?.clear()
                        hideClearButton()
                        true
                    }

                    else -> false
                }
            }
        }
        return false
    }

    enum class InputType {
        USERNAME, EMAIL, PASSWORD
    }
}