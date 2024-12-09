package com.example.skegoapp.ui.onboarding.forgot_password

import android.app.ProgressDialog.show
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.skegoapp.R
import com.example.skegoapp.data.remote.repository.ForgotPasswordRepository
import com.example.skegoapp.data.remote.retrofit.ApiConfig
import com.example.skegoapp.databinding.FragmentForgotPasswordBinding
import com.example.skegoapp.ui.mycustomview.MyButton
import com.example.skegoapp.ui.mycustomview.MyEditText

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var emailEditText: MyEditText
    private lateinit var oldPasswordEditText: MyEditText
    private lateinit var newPasswordEditText: MyEditText
    private lateinit var submitButton: MyButton

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize custom views
        emailEditText = binding.editTextEmail
        oldPasswordEditText = binding.editTextOldPassword
        newPasswordEditText = binding.editTextNewPassword
        submitButton = binding.buttonChangePassword

        // Set input types for custom MyEditText views
        emailEditText.setInputType(MyEditText.InputType.EMAIL)
        oldPasswordEditText.setInputType(MyEditText.InputType.PASSWORD)
        newPasswordEditText.setInputType(MyEditText.InputType.PASSWORD)

        // Enable or disable the submit button based on input
        setSubmitButtonEnabled()

        // Add TextWatchers to handle validation dynamically
        emailEditText.addTextChangedListener(textWatcher)
        oldPasswordEditText.addTextChangedListener(textWatcher)
        newPasswordEditText.addTextChangedListener(textWatcher)

        // Initialize ViewModel
        val factory = ForgotPasswordViewModelFactory(ForgotPasswordRepository(ApiConfig.getApiService()))
        viewModel = ViewModelProvider(this, factory).get(ForgotPasswordViewModel::class.java)

        // Handle submit button click
        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()

            // Check if all fields are filled
            if (email.isNotEmpty() && oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                // Call ViewModel to change password
                viewModel.changePassword(email, oldPassword, newPassword)
            } else {
                showErrorDialog("Please fill all the fields.")
            }
        }

        // Observe ViewModel response for password change status
        viewModel.passwordUpdateStatus.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                // Show success message
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()
                // Navigate to LoginFragment after success
                findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
            } else {
                // Show error message if password update failed
                Toast.makeText(requireContext(), "Failed to update password", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setSubmitButtonEnabled() {
        // Enable the submit button if all fields are valid
        val isEmailValid = emailEditText.text.toString().isNotEmpty() && emailEditText.error == null
        val isOldPasswordValid = oldPasswordEditText.text.toString().length >= 8
        val isNewPasswordValid = newPasswordEditText.text.toString().length >= 8
        submitButton.isEnabled = isEmailValid && isOldPasswordValid && isNewPasswordValid
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Update button state based on current input validation
            setSubmitButtonEnabled()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }
}
