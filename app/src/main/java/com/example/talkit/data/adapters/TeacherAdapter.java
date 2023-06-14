package com.example.talkit.data.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.talkit.R;
import com.example.talkit.data.model.Teacher;

import java.util.List;

public class TeacherAdapter extends ArrayAdapter<Teacher> {
    private static class ViewHolder {
        TextView name;
        TextView prompt;
        ImageView gender;
    }

    private Activity context;
    private List<Teacher> teachers;

    public TeacherAdapter(@NonNull Activity context, int resource, @NonNull List<Teacher> teachers) {
        super(context, resource, teachers);

        this.context = context;
        this.teachers = teachers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.teacher_row, null);

            holder = new ViewHolder();
            holder.name = row.findViewById(R.id.txt_teacher_name);
            holder.prompt = row.findViewById(R.id.txt_teacher_prompt);
            holder.gender = row.findViewById(R.id.img_teacher_gender);

            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();

        Teacher teacher = teachers.get(position);

        holder.name.setText(teacher.getName());
        holder.prompt.setText(teacher.getPrompt() == null ? "Personalidad por defecto" : teacher.getPrompt());
        holder.gender.setImageResource(teacher.getGender().getDrawable());
        return row;
    }

    public void setData(List<Teacher> teachers) {
        clear();
        addAll(teachers);
    }
}
