package com.example.skegoapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skegoapp.R
import com.example.skegoapp.databinding.FragmentRegisterBinding
import com.example.skegoapp.ui.main.MainActivity

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // Set up SpannableString for "Already Have Account? Sign In!"
        val spannableString = SpannableString("Already Have Account? Sign In!")
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.orange_text))
        spannableString.setSpan(colorSpan, 20, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Apply SpannableString to TextView
        binding.textViewLoginAccount.apply {
            text = spannableString
            isClickable = true
            movementMethod = LinkMovementMethod.getInstance()

            // Navigasi ke LoginFragment
            setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        // Set onClickListener for Register Button
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Agar tidak kembali ke RegisterFragment
        }

        return binding.root
    }
}


