package com.hacknife.briefness;

import android.widget.SeekBar;

public class OnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    public enum State {
        Start, Changing, Stop
    }

    @Deprecated
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        onProgressChanged(seekBar, State.Changing, progress, fromUser);
    }

    @Deprecated
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        onProgressChanged(seekBar, State.Start, seekBar.getProgress(), true);
    }

    @Deprecated
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        onProgressChanged(seekBar, State.Stop, seekBar.getProgress(), true);
    }

    public void onProgressChanged(SeekBar seekBar, State state, int progress, boolean fromUser) {
    }
}
