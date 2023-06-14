package com.example.talkit.data;

import com.example.talkit.R;

public enum Gender {
    GENDER_MALE("male", R.drawable.ic_male),
    GENDER_FEMALE("female", R.drawable.ic_female);

    private final String str;
    private final int drawable;

    public static Gender getByStr(String str) {
        for (Gender gender : Gender.values())
            if (gender.getStr().equals(str))
                return gender;

        return null;
    }

    Gender(String str, int drawable) {
        this.str = str;
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    public String getStr() {
        return str;
    }
}
