package com.example.assignement_firbase.LoginPage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.LocusId
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.assignement_firbase.MainActivity
import com.example.assignement_firbase.R
import com.example.assignement_firbase.UserDetails_Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.Objects
import java.util.concurrent.TimeUnit
import kotlin.math.sign

class Login_Page : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var editNumber: EditText
    private lateinit var verificationEditNum: EditText
    private lateinit var submitButton: Button
    private lateinit var continueButton: Button
    private var verificationId = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()

        editNumber = findViewById(R.id.custemerPhoneNum_EditText)
        verificationEditNum = findViewById(R.id.custemerVerificationNum_EditText)
        submitButton = findViewById(R.id.VerifyBtn_Button)
        continueButton = findViewById(R.id.continueBtn_Button)

        submitButton.setOnClickListener {
            verifyOtp()
        }

        continueButton.setOnClickListener {

            getNumber()
        }
    }
    private fun getNumber() {
        val phoneNumber = editNumber.text.toString()
        if (phoneNumber.isNotEmpty()) {
            sendNumber(phoneNumber)
        } else {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }
    private fun sendNumber(phoneNumber: String) {
        val formattedPhoneNum = "+91$phoneNumber"
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            formattedPhoneNum, 60, TimeUnit.SECONDS, this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signingWithPhoneAuth(credential)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(
                        this@Login_Page,
                        "Verification failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    this@Login_Page.verificationId = verificationId
                    Toast.makeText(
                        this@Login_Page,
                        "OTP has been sent to your number.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun verifyOtp() {
        val otp = verificationEditNum.text.toString().trim()
        if (otp.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            signingWithPhoneAuth(credential)
        } else {
            Toast.makeText(this, "Please Enter Otp Number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signingWithPhoneAuth(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(
                        this,
                        "Verification Failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}