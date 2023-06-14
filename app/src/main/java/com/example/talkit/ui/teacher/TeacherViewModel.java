package com.example.talkit.ui.teacher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.talkit.TalkIt;
import com.example.talkit.data.ApiRequest;
import com.example.talkit.data.LoginRepository;
import com.example.talkit.data.model.Teacher;

import org.json.JSONObject;

public class TeacherViewModel extends ViewModel {
    public enum TeacherStatus {
        TEACHER_NONE,
        TEACHER_SUCCESS,
        TEACHER_FAIL,
        TEACHER_WAITING
    }

    private final LoginRepository loginRepository;
    private final MutableLiveData<TeacherStatus> status;

    public TeacherViewModel() {
        loginRepository = LoginRepository.getInstance();
        status = new MutableLiveData<>();

        status.setValue(TeacherStatus.TEACHER_SUCCESS);
//        loadTeachers();
    }

    public MutableLiveData<TeacherStatus> getStatus() {
        return status;
    }

    public void newTeacher(Teacher teacher) {
        status.setValue(TeacherStatus.TEACHER_WAITING);

        try {
            RequestQueue requestQueue = ApiRequest.getInstance(TalkIt.getContext()).getRequestQueue();

            JSONObject params = new JSONObject();
            params.put("apiCode", loginRepository.getUser().getApiCode());
            params.put("teacher_name", teacher.getName());
            params.put("teacher_gender", teacher.getGender().getStr());
            params.put("teacher_prompt", teacher.getPrompt());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    TalkIt.BACKEND_URL + "teachers/",
                    params,
                    response -> {
                        loginRepository.getUser().addTeacher(teacher);
                        status.setValue(TeacherStatus.TEACHER_SUCCESS);
                    },
                    error -> {
                        status.setValue(TeacherStatus.TEACHER_FAIL);
                    }
            );

            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void loadTeachers() {
//        try {
//            status.setValue(TeacherStatus.TEACHER_WAITING);
//            RequestQueue requestQueue = ApiRequest.getInstance(TalkIt.getContext()).getRequestQueue();
//
//            JsonObjectRequest request = new JsonObjectRequest(
//                    Request.Method.GET,
//                    TalkIt.BACKEND_URL + String.format("teachers?apiCode=%s", loginRepository.getUser().getApiCode()),
//                    null,
//                    response -> {
//                        JSONArray teachersJson = null;
//                        try {
//                            teachersJson = response.getJSONArray("teachers");
//
//                            for (int i = 0; i < teachersJson.length(); i++) {
//                                JSONObject teacherJson = teachersJson.getJSONObject(i);
//
//                                Teacher teacher = new Teacher(
//                                        teacherJson.getString("teacher_name"),
//                                        Gender.getByStr(teacherJson.getString("teacher_gender")),
//                                        teacherJson.getString("teacher_prompt")
//                                );
//
//                                teachers.add(teacher);
//                            }
//                        } catch (JSONException e) {
//                            status.setValue(TeacherStatus.TEACHER_FAIL);
//                            return;
//                        }
//
//                        status.setValue(TeacherStatus.TEACHER_SUCCESS);
//                    },
//                    error -> {
//                        status.setValue(TeacherStatus.TEACHER_FAIL);
//                    }
//            );
//
//            System.out.println(request);
//
//            requestQueue.add(request);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}