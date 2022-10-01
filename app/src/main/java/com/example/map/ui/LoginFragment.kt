package com.example.map.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.map.R
import com.example.map.core.Resource
import com.example.map.data.remote.UserDataSource
import com.example.map.data.repository.RetrofitClient.webService
import com.example.map.data.repository.UserRepoImpl
import com.example.map.databinding.FragmentLoginBinding
import com.example.map.presentation.UserViewModel
import com.example.map.presentation.UserViewModelFactory


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<UserViewModel>{UserViewModelFactory(UserRepoImpl(
        UserDataSource(webService)
    ))}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        doLogin()
    }


    private fun doLogin(){
        binding.btnSignin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password= binding.editTextPassword.text.toString().trim()
            validateCredentials(email, password)
            singIn(email, password)
        }
    }

    private fun validateCredentials(email: String, password : String) {
        if (email.isEmpty()){
            binding.editTextEmail.error = "E-mail is empty"
            return
        }

        if (password.isEmpty()){
            binding.editTextPassword.error= "Password is empty"
        }
    }

    private fun singIn( email: String, password: String) {
        viewModel.singIn(email, password).observe(viewLifecycleOwner, Observer {  result ->
            when(result){
                is Resource.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignin.isEnabled = false
                }
                is Resource.Success ->{
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_mapFragment)
                }
                is Resource.Failure ->{
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignin.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

    }

}
