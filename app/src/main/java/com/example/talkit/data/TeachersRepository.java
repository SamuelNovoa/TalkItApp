package com.example.talkit.data;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.talkit.TalkIt;
import com.example.talkit.data.model.Teacher;
import com.example.talkit.data.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TeachersRepository {
    private static TeachersRepository instance;

    private LoginRepository loginRepository;

    public static TeachersRepository getInstance() {
        return instance;
    }

    public TeachersRepository() {
        loginRepository = LoginRepository.getInstance();
    }
}
