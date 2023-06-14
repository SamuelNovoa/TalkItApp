package com.example.talkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.talkit.data.LoginRepository;
import com.example.talkit.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginRepository loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginRepository = LoginRepository.getInstance();

        loginRepository.getStatus().observe(this, loginStatus -> {
            switch (loginStatus) {
                case LOGIN_SUCCESS:
                    startActivity(new Intent(this, TeacherActivity.class));
                    binding.progress.setVisibility(View.INVISIBLE);
                    break;
                case LOGIN_FAIL:
                    binding.progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });
    }

    public void onLogin(View view) {
        String email = binding.txtEmail.getText().toString();
        String pwd = binding.txtPwd.getText().toString();

        binding.progress.setVisibility(View.VISIBLE);
        loginRepository.login(email, pwd);
    }
}