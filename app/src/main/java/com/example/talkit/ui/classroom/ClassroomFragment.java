package com.example.talkit.ui.classroom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.talkit.R;
import com.example.talkit.TalkIt;
import com.example.talkit.data.ApiRequest;
import com.example.talkit.data.LoginRepository;
import com.example.talkit.data.model.Teacher;
import com.example.talkit.databinding.FragmentClassroomBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class ClassroomFragment extends Fragment {
    public interface OnGotoTeachers {
        void gotoTeachers();
    }

    private FragmentClassroomBinding binding;
    private OnGotoTeachers callback;
    private LoginRepository loginRepository;
    private NavController navController;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String fileName;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClassroomViewModel classroomViewModel =
                new ViewModelProvider(this).get(ClassroomViewModel.class);

        binding = FragmentClassroomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginRepository = LoginRepository.getInstance();
        fileName = requireActivity().getExternalCacheDir().getAbsolutePath() + "/student_conversation";
        System.out.println(fileName);

        binding.btnConversation.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startRecording();
                    break;
                case MotionEvent.ACTION_UP:
                    stopRecording();
                    break;
                default:
                    break;
            }

            return false;
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Teacher teacher = loginRepository.getUser().getLastTeacher();

        if (teacher == null) {
            navController.navigate(R.id.nav_teacher);
            return;
        }

        binding.txtClassTeacherName.setText(teacher.getName());
        binding.txtClassTeacherGender.setImageResource(teacher.getGender().getDrawable());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void startPlaying(String audio) {
        byte[] audioDec = Base64.getDecoder().decode(audio);

        try (OutputStream outputStream = new FileOutputStream(fileName + ".wav")) {
            outputStream.write(audioDec);
        } catch (IOException ex) {
            Log.e("TalkIt", "write temp failed");
        }

        try {
            player = new MediaPlayer();
            player.setDataSource(fileName + ".wav");
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("TalkIt", "prepare() failed");
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName + ".3gp");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("TalkIt", "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        try {
            RequestQueue requestQueue = ApiRequest.getInstance(TalkIt.getContext()).getRequestQueue();

            JSONObject params = new JSONObject();
            params.put("apiCode", loginRepository.getUser().getApiCode());
            params.put("teacher_name", loginRepository.getUser().getLastTeacher().getName());
            params.put("question", getFileData());

            binding.progressBar.setVisibility(View.VISIBLE);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    TalkIt.BACKEND_URL + "talk/response",
                    params,
                    response -> {
                        String answer;

                        try {
                            answer = response.getString("answer_voice");
                            System.out.println(answer);
                            startPlaying(answer);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    }
            );

            request.setRetryPolicy(new DefaultRetryPolicy(
                    (int) TimeUnit.SECONDS.toMillis(600), //After the set time elapses the request will timeout
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );

            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileData() {
        File audio = new File(fileName + ".3gp");

        int size = (int) audio.length();
        byte[] bytes = new byte[size];
        byte[] tmpBuff = new byte[size];

        try (FileInputStream inputStream = new FileInputStream(audio)) {
            int read = inputStream.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = inputStream.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(Base64.getEncoder().encode(bytes));
    }
}