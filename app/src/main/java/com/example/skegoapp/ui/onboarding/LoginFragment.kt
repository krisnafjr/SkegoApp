package com.example.skegoapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skegoapp.R
import com.example.skegoapp.databinding.FragmentLoginBinding
import com.example.skegoapp.ui.main.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TextView "Create one!" untuk navigasi ke RegisterFragment
        val spannableString = SpannableString("Don't Have Account? Create one!")
        val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.orange_text))
        spannableString.setSpan(colorSpan, 20, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textViewCreateAccount.text = spannableString

        binding.textViewCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Tombol login, arahkan langsung ke MainActivity tanpa logika
        binding.buttonLogin.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Agar tidak kembali ke LoginFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





