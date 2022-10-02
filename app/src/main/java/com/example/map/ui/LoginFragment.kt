package com.example.map.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.util.PatternsCompat
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
import java.util.regex.Pattern


class LoginFragment : Fragment(R.layout.fragment_login) {
    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<UserViewModel>{UserViewModelFactory(UserRepoImpl(
        UserDataSource(webService)
    ))}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        validate()
        createChannel()
    }




    private fun validate(){
        binding.btnSignin.setOnClickListener {
            val result = arrayOf(validateEmail(), validatePassword())
            if (false in result){
                return@setOnClickListener
            }else(singIn(email = String(), password = String()))
        }
    }

    private fun validateEmail() : Boolean{
        val email = binding.editTextEmail.text.toString()
        return if (email.isEmpty()){
            binding.editTextEmail.error = "E-mail is empty"
            false
        }else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editTextEmail.error = "Please enter a valid email address"
            false
        }else{
            binding.editTextEmail.error = null
            true
        }
    }

    private fun validatePassword() : Boolean{

        val password= binding.editTextPassword.text.toString()
        val passwordRegex = Pattern.compile(
            "^"+
                    "(?=.*[0-9])"+           // al menos un número
                    "(?=.*[a-z])"+           // al menos una letra minúscula
                    "(?=.*[A-Z])"+           // al menos una letra mayúscula
                    "(?=.*[@#$%^^+=])"+      //al menos un carácter especial
                    "(?=\\S+$)" +            // sin espacios
                    ".{0,}"+                 // más de cuatro letras
                    "$"
        )
        return if (password.isEmpty()){
            binding.editTextPassword.error= "Field can not is empty"
            false
        }else if (!passwordRegex.matcher(password).matches()){
            binding.editTextPassword.error = "Password is too weak"
            false
        }else{
            binding.editTextPassword.error = null
            true
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
                    result.data.code
                    findNavController().navigate(R.id.action_loginFragment_to_mapsFragment2)
                    result.data.code?.let { createSimpleNotification(it) }
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

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MY_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = ""
            }

            val notificationManager: NotificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createSimpleNotification( message:Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        context?.let { _context ->
            val builder = NotificationCompat.Builder(_context, MY_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setContentTitle("${message}")
                .setContentText("Bienvenido")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(_context)) {
                notify(1, builder.build())
            }
        }

    }
}