package com.example.talkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.talkit.data.Gender;
import com.example.talkit.databinding.ActivityAddTeacherBinding;
import com.example.talkit.databinding.ActivityLoginBinding;

public class AddTeacherActivity extends AppCompatActivity {
    private ActivityAddTeacherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    public void onSubmit(View view) {
        String teacherName = binding.newTeacherName.getText().toString();
        String teacherGender = binding.newTeacherGender.getCheckedRadioButtonId() == binding.newTeacherMale.getId() ? Gender.GENDER_MALE.getStr() : Gender.GENDER_FEMALE.getStr();
        String teacherPrompt = binding.newTeacherPrompt.getText().toString();

        if (teacherName.equals("") || teacherPrompt.equals(""))
            return;

        Intent i = new Intent();
        i.putExtra("teacherName", teacherName);
        i.putExtra("teacherGender", teacherGender);
        i.putExtra("teacherPrompt", teacherPrompt);

        setResult(RESULT_OK, i);
        finish();
    }
}