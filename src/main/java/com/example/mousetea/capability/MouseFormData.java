package com.example.mousetea.capability;

public class MouseFormData {

    /** 0 = человек, 4 = максимальная мышиная стадия */
    public static final int MAX_STAGE = 4;

    private int stage = 0;
    /** Тиков с момента последнего глотка — используем, чтобы стадия чуть "приживалась" не мгновенно. */
    private int ticksInStage = 0;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = Math.max(0, Math.min(MAX_STAGE, stage));
        this.ticksInStage = 0;
    }

    public void advanceStage() {
        setStage(stage + 1);
    }

    public int getTicksInStage() {
        return ticksInStage;
    }

    public void tick() {
        ticksInStage++;
    }

    public void copyFrom(MouseFormData other) {
        this.stage = other.stage;
        this.ticksInStage = other.ticksInStage;
    }
}
