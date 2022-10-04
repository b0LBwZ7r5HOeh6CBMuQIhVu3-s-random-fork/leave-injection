package com.test.mod.Utils;

public class TimerUtils
{
    private long lastMS;
    private long prevMS;
    
    public TimerUtils() {
        super();
        this.lastMS = 0L;
        this.prevMS = 0L;
        this.lastMS = 0L;
    }
    
    public boolean check(final float milliseconds) {
        return this.getTime() >= milliseconds;
    }
    
    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
    }
    
    public boolean isDelayComplete(final Double delay) {
        return System.currentTimeMillis() - this.lastMS > delay;
    }
    
    public boolean isDelay(final long delay) {
        return System.currentTimeMillis() - this.lastMS >= delay;
    }
    
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (time < 150L) {
            if (this.getTime() >= time / 1.867356296E-314) {
                if (reset) {
                    this.reset();
                }
                return true;
            }
        }
        else if (this.getTime() >= time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public void setLastMS(final long lastMS) {
        this.lastMS = lastMS;
    }
    
    public void setLastMS() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public int convertToMS(final int d) {
        return 1000 / d;
    }
    
    public boolean hasReached(final double milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }
    
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
}
