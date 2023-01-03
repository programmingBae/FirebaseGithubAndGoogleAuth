package com.example.googlegithubauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.googlegithubauth.databinding.ActivityLoginWithGithubBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class LoginWithGithub : AppCompatActivity() {
    private lateinit var binding: ActivityLoginWithGithubBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithGithubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.buttonLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (email.isEmpty()){
                Toast.makeText(this, "Please fill in the email", Toast.LENGTH_SHORT).show()
            } else {
                val provider = OAuthProvider.newBuilder("github.com")
                provider.addCustomParameter("login", email)

                val scopes: ArrayList<String?> = object : ArrayList<String?>() {
                    init {
                        add("user:email")
                    }
                }
                provider.scopes = scopes
                val pendingResultTask: Task<AuthResult>? = auth.getPendingAuthResult()
                if (pendingResultTask != null) {
                    // There's something already here! Finish the sign-in for your user.
                    pendingResultTask
                        .addOnSuccessListener(
                            OnSuccessListener<AuthResult?> {
                                // User is signed in.
                                // IdP data available in
                                // authResult.getAdditionalUserInfo().getProfile().
                                // The OAuth access token can also be retrieved:
                                // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                            })
                        .addOnFailureListener(
                            OnFailureListener {
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            })
                } else {
                    auth
                        .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                        .addOnSuccessListener(

                            OnSuccessListener<AuthResult?> {
                                openNextActivity()
                            })
                        .addOnFailureListener(
                            OnFailureListener {
                                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            })

                }


            }
        }
    }

    private fun openNextActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", binding.etEmail.text)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()

    }
}