package com.example.skegoapp.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.skegoapp.R

class OnboardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        // Navigasi ke LoginFragment
        view.findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            navController.navigate(R.id.action_onboardingFragment_to_loginFragment)
        }

        // Navigasi ke RegisterFragment
        view.findViewById<Button>(R.id.createAccountButton).setOnClickListener {
            navController.navigate(R.id.action_onboardingFragment_to_registerFragment)
        }
    }
}