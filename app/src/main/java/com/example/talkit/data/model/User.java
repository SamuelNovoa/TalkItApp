package com.example.talkit.data.model;

import androidx.lifecycle.MutableLiveData;

import com.example.talkit.ui.teacher.TeacherViewModel;

import java.util.List;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class User {
    private final String email;
    private final String username;
    private final String apiCode;
    private final List<Teacher> teachers;
    private Teacher lastTeacher;

    public User(String email, String username, String apiCode, List<Teacher> teachers) {
        this.email = email;
        this.username = username;
        this.apiCode = apiCode;
        this.teachers = teachers;
        this.lastTeacher = null;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getApiCode() {
        return apiCode;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public Teacher getLastTeacher() {
        return lastTeacher;
    }

    public void setLastTeacher(Teacher lastTeacher) {
        this.lastTeacher = lastTeacher;
    }
}