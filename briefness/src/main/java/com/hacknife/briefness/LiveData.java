package com.hacknife.briefness;

public abstract class LiveData {
    private Briefnessor briefnessor;

    protected void notifyDataChange() {
        if (briefnessor != null)
            briefnessor.notifyDataChange(this.getClass());
    }

    @Deprecated
    public <B extends Briefnessor> void bindTape(B briefnessor) {
        this.briefnessor = briefnessor;
    }
}
