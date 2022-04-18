package com.example.devproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.devproject.databinding.ActivitySignUpAcitivtyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpAcitivty : AppCompatActivity() {

    lateinit var binding: ActivitySignUpAcitivtyBinding
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpAcitivtyBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.TvFieldInputId.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutId.helperText = "아이디를 비울 수 없습니다"
                }
                if(InputString.toString().isNotEmpty()){
                    binding.TextLayoutId.helperText = null
                }

                if(InputString.toString().contains(" ")){
                    binding.TextLayoutId.helperText = "띄어쓰기는 허용되지 않습니다"
                }
            }
        })

        binding.TvFieldInputEmail.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val email = s.toString()
                val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                if(!isValid){
                    binding.TextLayoutEmail.helperText = "올바른 이메일은 형식은 abc@example.com 입니다"
                }
                else{
                    binding.TextLayoutEmail.helperText = null
                }
            }

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutEmail.helperText = "이메일을 비울 수 없습니다"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutEmail.helperText = "띄어쓰기는 허용되지 않습니다"
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(InputString).matches()){
                    binding.TextLayoutEmail.helperText = "올바른 이메일은 형식은 abc@example.com 입니다"
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
                    binding.TextLayoutPassword.helperText = "비밀번호를 비울 수 없습니다"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutPassword.helperText = "띄어쓰기는 허용되지 않습니다"
                }
                else{
                    binding.TextLayoutPassword.helperText = null
                }
                if(binding.TvFieldInputPasswordConfirm.text.toString() == InputString.toString()){
                    binding.TextLayoutPasswordConfirm.helperText = null
                }
                if(InputString!!.length < 6 && InputString!!.length < 21){
                    binding.TextLayoutPassword.helperText = "비밀번호는 6자리 이상 20자리 이하여야합니다"
                }
            }

        })

        binding.TvFieldInputPasswordConfirm.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutPasswordConfirm.helperText = "비밀번호를 재입력 해야합니다"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutPasswordConfirm.helperText = "띄어쓰기는 허용되지 않습니다"
                }
                else{
                    binding.TextLayoutPasswordConfirm.helperText = null
                }

                if(binding.TvFieldInputPassword.text.toString() != InputString.toString()){
                    binding.TextLayoutPasswordConfirm.helperText = "비밀번호가 일치하지 않습니다"
                }
                else{
                    binding.TextLayoutPasswordConfirm.helperText = null
                }
            }

        })

        auth = Firebase.auth

        binding.BtnSinUp.setOnClickListener {
            if(binding.TvFieldInputPassword.text.length > 20){
                Toast.makeText(this, "비밀번호는 20자리 이하여야 합니다", Toast.LENGTH_SHORT).show()
            }
            else if(binding.TvFieldInputEmail.text!!.isNotEmpty() && binding.TvFieldInputPassword.text!!.isNotEmpty()
                && binding.TvFieldInputId.text!!.isNotEmpty() && binding.TvFieldInputPasswordConfirm.text!!.isNotEmpty()
                && binding.TextLayoutId.helperText == null && binding.TextLayoutEmail.helperText == null
                && binding.TextLayoutPassword.helperText == null && binding.TextLayoutPasswordConfirm.helperText == null){

                auth.createUserWithEmailAndPassword(binding.TvFieldInputEmail.text.toString(), binding.TvFieldInputPassword.text.toString())
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful){
                            Log.d("TAG", "createUserWithEmail:success")
                            val user = auth.currentUser
                            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this, "존재하는 아이디입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                if(binding.TvFieldInputId.text == null || binding.TextLayoutId.helperText != null){
                    Toast.makeText(this, "아이디가 입력되지 않았거나 입력이 잘못되었습니다", Toast.LENGTH_SHORT).show()
                }
                else if(binding.TvFieldInputEmail.text == null || binding.TextLayoutEmail.helperText != null){
                    Toast.makeText(this, "이메일이 입력되지 않았거나 입력이 잘못되었습니다", Toast.LENGTH_SHORT).show()
                }
                else if(binding.TvFieldInputPassword.text == null || binding.TextLayoutPassword.helperText != null){
                    Toast.makeText(this, "비밀번호가 입력되지 않았거나 입력이 잘못되었습니다", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "공란이 있습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}