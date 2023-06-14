package com.example.talkit.ui.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.talkit.AddTeacherActivity;
import com.example.talkit.R;
import com.example.talkit.data.Gender;
import com.example.talkit.data.LoginRepository;
import com.example.talkit.data.adapters.TeacherAdapter;
import com.example.talkit.data.model.Teacher;
import com.example.talkit.databinding.FragmentTeacherBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TeacherFragment extends Fragment {
    private static final int ADD_TEACHER_CODE = 1;

    private LoginRepository loginRepository;
    private FragmentTeacherBinding binding;
    private TeacherViewModel teacherViewModel;
    private TeacherAdapter adapter;

    private ProgressBar teachersProgress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        teacherViewModel =
                new ViewModelProvider(this).get(TeacherViewModel.class);

        binding = FragmentTeacherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginRepository = LoginRepository.getInstance();
        teachersProgress = binding.teachersProgress;
        binding.fab.setOnClickListener(view -> {
            startActivityForResult(new Intent(requireActivity(), AddTeacherActivity.class), ADD_TEACHER_CODE);
        });

        adapter = new TeacherAdapter(requireActivity(), R.layout.teacher_row, new ArrayList<>());
        binding.listTeachers.setAdapter(adapter);
        teacherViewModel.getStatus().observe(requireActivity(), status -> {
            switch (status) {
                case TEACHER_SUCCESS:
                    binding.teachersProgress.setVisibility(View.GONE);
                    List<Teacher> teachers = loginRepository.getUser().getTeachers();
                    adapter.setData(teachers);

                    if (teachers.isEmpty())
                        binding.txtNoTeachers.setVisibility(View.VISIBLE);
                    else
                        binding.listTeachers.setVisibility(View.VISIBLE);
                    break;
                case TEACHER_FAIL:
                    teachersProgress.setVisibility(View.GONE);
                    binding.txtTeacherFail.setVisibility(View.VISIBLE);
                    break;
                case TEACHER_WAITING:
                    teachersProgress.setVisibility(View.VISIBLE);
                    binding.txtTeacherFail.setVisibility(View.GONE);
                    binding.txtNoTeachers.setVisibility(View.GONE);
                    binding.listTeachers.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        });

        binding.listTeachers.setOnItemClickListener((adapterView, view, i, l) -> {
            Teacher teacher = (Teacher) adapterView.getItemAtPosition(i);
            loginRepository.getUser().setLastTeacher(teacher);

            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.nav_classroom);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != ADD_TEACHER_CODE || data == null)
            return;

        String teacherName = data.getStringExtra("teacherName");
        String teacherGender = data.getStringExtra("teacherGender");
        String teacherPrompt = data.getStringExtra("teacherPrompt");

        Teacher teacher = new Teacher(teacherName, Gender.getByStr(teacherGender), teacherPrompt);
        teacherViewModel.newTeacher(teacher);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}