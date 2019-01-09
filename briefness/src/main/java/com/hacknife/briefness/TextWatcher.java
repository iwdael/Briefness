package com.hacknife.briefness;

import android.text.Editable;

public class TextWatcher implements android.text.TextWatcher {
    String content;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (content == null || (!editable.toString().trim().equals(content))) {
            content = editable.toString().trim();
            onTextChange(content);
        }
    }

    public void onTextChange(String content) {

    }
}
