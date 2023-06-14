package com.example.talkit.data;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.talkit.TalkIt;
import com.example.talkit.data.model.Teacher;
import com.example.talkit.data.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginRepository {
    public enum LoginStatus {
        LOGIN_NONE,
        LOGIN_SUCCESS,
        LOGIN_FAIL,
        LOGIN_WAITING
    }

    private static LoginRepository instance;

    private User user;
    private final MutableLiveData<LoginStatus> status;

    public static LoginRepository getInstance() {
        if (instance == null)
            instance = new LoginRepository();

        return instance;
    }

    public LoginRepository() {
        user = null;
        status = new MutableLiveData<>(LoginStatus.LOGIN_NONE);
    }

    public MutableLiveData<LoginStatus> getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void login(String email, String password) {
        status.setValue(LoginStatus.LOGIN_WAITING);

        try {
            RequestQueue requestQueue = ApiRequest.getInstance(TalkIt.getContext()).getRequestQueue();

            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    TalkIt.BACKEND_URL + "login",
                    params,
                    response -> {
                        String username = null;
                        String apiCode = null;
                        List<Teacher> teachers = new ArrayList<>();

                        try {
                            username = response.getString("username");
                            apiCode = response.getString("apiCode");
                            JSONArray teachersJson = response.getJSONArray("teachers");

                            for (int i = 0; i < teachersJson.length(); i++) {
                                JSONObject teacherJson = teachersJson.getJSONObject(i);

                                Teacher teacher = new Teacher(
                                        teacherJson.getString("teacher_name"),
                                        Gender.getByStr(teacherJson.getString("teacher_gender")),
                                        teacherJson.getString("teacher_prompt")
                                );

                                teachers.add(teacher);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        this.user = new User(email, username, apiCode, teachers);
                        if (!teachers.isEmpty())
                            this.user.setLastTeacher(teachers.get(0));

                        status.setValue(LoginStatus.LOGIN_SUCCESS);
                    },
                    error -> {
                        status.setValue(LoginStatus.LOGIN_FAIL);
                    }
            );

            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        if (user == null)
            return;

        RequestQueue requestQueue = ApiRequest.getInstance(TalkIt.getContext()).getRequestQueue();
        JSONObject params = new JSONObject();

        try {
            params.put("apiCode", user.getApiCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                TalkIt.BACKEND_URL + "logout",
                params,
                response -> { },
                error -> { }
        );

        user = null;
        status.setValue(LoginStatus.LOGIN_NONE);
        requestQueue.add(request);
    }
}
