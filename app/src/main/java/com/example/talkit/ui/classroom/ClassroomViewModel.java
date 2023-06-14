package com.example.talkit.ui.classroom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.talkit.data.model.Teacher;

public class ClassroomViewModel extends ViewModel {

    private final MutableLiveData<Teacher> teacher;

    public ClassroomViewModel() {
        teacher = new MutableLiveData<>();
    }

    public MutableLiveData<Teacher> getTeacher() {
        return teacher;
    }
}