package com.example.devproject.activity.account

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.devproject.R
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.KeyboardVisibilityUtils
import com.example.devproject.format.UserInfo
import com.example.devproject.databinding.ActivitySignUpAcitivtyBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpAcitivtyBinding
    private lateinit var  auth: FirebaseAuth
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private val languages = listOf<String>("Kotlin" , "Java", "JavaScript" , "C++", "C")
    private var pos = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpAcitivtyBinding.inflate(layoutInflater)

        setContentView(binding.root)

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = {keyboardHeight ->
                binding.signUpScrollView.run {
                    smoothScrollBy(scrollX, scrollY)
                }
            })


       var languageAdpater = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages)
        binding.languageSpinner.adapter = languageAdpater
        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               pos = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.TvFieldInputId.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutId.helperText = "???????????? ?????? ??? ????????????"
                }
                if(InputString.toString().isNotEmpty()){
                    binding.TextLayoutId.helperText = null
                }

                if(InputString.toString().contains(" ")){
                    binding.TextLayoutId.helperText = "??????????????? ???????????? ????????????"
                }
            }
        })

        binding.TvFieldInputEmail.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val email = s.toString()
                val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                if(!isValid){
                    binding.TextLayoutEmail.helperText = "????????? ???????????? ????????? abc@example.com ?????????"
                }
                else{
                    binding.TextLayoutEmail.helperText = null
                }
            }

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutEmail.helperText = "???????????? ?????? ??? ????????????"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutEmail.helperText = "??????????????? ???????????? ????????????"
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(InputString).matches()){
                    binding.TextLayoutEmail.helperText = "????????? ???????????? ????????? abc@example.com ?????????"
                }
                else{
                    binding.TextLayoutEmail.helperText = null
                }
            }

        })

        binding.TvFieldInputPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, count: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutPassword.helperText = "??????????????? ?????? ??? ????????????"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutPassword.helperText = "??????????????? ???????????? ????????????"
                }
                else{
                    binding.TextLayoutPassword.helperText = null
                }
                if(binding.TvFieldInputPasswordConfirm.text.toString() == InputString.toString()){
                    binding.TextLayoutPasswordConfirm.helperText = null
                }
                if(InputString!!.length < 6 && InputString!!.length < 21){
                    binding.TextLayoutPassword.helperText = "??????????????? 6?????? ?????? 20?????? ?????????????????????"
                }
            }

        })

        binding.TvFieldInputPasswordConfirm.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutPasswordConfirm.helperText = "??????????????? ????????? ???????????????"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutPasswordConfirm.helperText = "??????????????? ???????????? ????????????"
                }
                else{
                    binding.TextLayoutPasswordConfirm.helperText = null
                }

                if(binding.TvFieldInputPassword.text.toString() != InputString.toString()){
                    binding.TextLayoutPasswordConfirm.helperText = "??????????????? ???????????? ????????????"
                }
                else{
                    binding.TextLayoutPasswordConfirm.helperText = null
                }
            }

        })

        auth = Firebase.auth

        binding.BtnSinUp.setOnClickListener {
            if(binding.TvFieldInputPassword.text.length > 20){
                Toast.makeText(this, "??????????????? 20?????? ???????????? ?????????", Toast.LENGTH_SHORT).show()
            }
            else if(binding.TvFieldInputEmail.text!!.isNotEmpty() && binding.TvFieldInputPassword.text!!.isNotEmpty()
                && binding.TvFieldInputId.text!!.isNotEmpty() && binding.TvFieldInputPasswordConfirm.text!!.isNotEmpty()
                && binding.TextLayoutId.helperText == null && binding.TextLayoutEmail.helperText == null
                && binding.TextLayoutPassword.helperText == null && binding.TextLayoutPasswordConfirm.helperText == null){
                    FirebaseIO.read("UserInfo", binding.TvFieldInputId.text.toString())
                        .addOnSuccessListener {
                        if(it.data != null){
                            Toast.makeText(this, "????????? ??????????????????", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            auth.createUserWithEmailAndPassword(binding.TvFieldInputEmail.text.toString(), binding.TvFieldInputPassword.text.toString())
                                .addOnCompleteListener(this){ task ->
                                    if(task.isSuccessful){

                                        storeUserId()

                                        Toast.makeText(this, "???????????? ??????", Toast.LENGTH_SHORT).show()
                                        val mIntent = Intent(this, LoginActivity::class.java)
                                        mIntent.putExtra("LoginId", binding.TvFieldInputEmail.text.toString())
                                        mIntent.putExtra("LoginPassword", binding.TvFieldInputPassword.text.toString())
                                        setResult(RESULT_OK, mIntent)
                                        finish()
                                    }
                                    else{
                                        Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }
            }
            else{
                if(binding.TvFieldInputId.text == null || binding.TextLayoutId.helperText != null){
                    Toast.makeText(this, "???????????? ???????????? ???????????? ????????? ?????????????????????", Toast.LENGTH_SHORT).show()
                }
                else if(binding.TvFieldInputEmail.text == null || binding.TextLayoutEmail.helperText != null){
                    Toast.makeText(this, "???????????? ???????????? ???????????? ????????? ?????????????????????", Toast.LENGTH_SHORT).show()
                }
                else if(binding.TvFieldInputPassword.text == null || binding.TextLayoutPassword.helperText != null){
                    Toast.makeText(this, "??????????????? ???????????? ???????????? ????????? ?????????????????????", Toast.LENGTH_SHORT).show()
                }
                else if (binding.TextLayoutPasswordConfirm.helperText != null){
                    Toast.makeText(this, "??????????????? ???????????? ????????????", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "????????? ????????????", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun storeUserId(){
        var userInfo = UserInfo()
        userInfo.uid = auth.uid
        userInfo.Id = binding.TvFieldInputId.text.toString()
        userInfo.Email = binding.TvFieldInputEmail.text.toString()
        userInfo.mainLanguage = languages[pos]

        FirebaseIO.write("UserInfo", binding.TvFieldInputId.text.toString(), userInfo)

    }
}

