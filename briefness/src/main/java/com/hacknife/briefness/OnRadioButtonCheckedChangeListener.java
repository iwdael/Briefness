package com.hacknife.briefness;

import android.widget.RadioGroup;

public class OnRadioButtonCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
    protected int checkedId = 0;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (this.checkedId == checkedId) return;
        this.checkedId = checkedId;
        onChecked(radioGroup, checkedId);
    }

    public void onChecked(RadioGroup radioGroup, int checkedId) {

    }
}
