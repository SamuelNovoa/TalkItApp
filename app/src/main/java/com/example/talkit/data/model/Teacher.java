package com.example.talkit.data.model;

import com.example.talkit.data.Gender;

public class Teacher {
    private final String name;
    private final Gender gender;
    private final String prompt;

    public Teacher(String name, Gender gender, String prompt) {
        this.name = name;
        this.gender = gender;
        this.prompt = prompt;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPrompt() {
        return prompt;
    }
}
