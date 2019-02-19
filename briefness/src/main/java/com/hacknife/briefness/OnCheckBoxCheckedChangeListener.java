package com.hacknife.briefness;

import android.widget.CompoundButton;


public class OnCheckBoxCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
    protected Boolean checked = null;


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (checked != null && checked == b) return;
        checked=b;
        onChecked(compoundButton,b);
    }

    public void onChecked(CompoundButton compoundButton, boolean checked) {

    }
}
