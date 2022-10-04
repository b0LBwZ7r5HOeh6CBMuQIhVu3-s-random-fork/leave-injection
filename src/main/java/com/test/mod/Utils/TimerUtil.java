package com.test.mod.Utils;

import net.minecraft.util.Timer;

public class TimerUtil
{
    private long lastMS;
    public Timer timerSpeed;
    
    public TimerUtil() {
        super();
        this.lastMS = 0L;
    }
    
    public boolean check(final float milliseconds) {
        return this.getTime() >= milliseconds;
    }
    
    public boolean sleep(final long time) {
        if (this.time() >= time) {
            this.reset();
            return true;
        }
        return false;
    }
    
    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public long time() {
        return System.nanoTime() / 1000000L - this.time();
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.lastMS >= milliSec;
    }
    
    public boolean delay(final Double milliSec) {
        return this.getTime() - this.lastMS >= milliSec;
    }
    
    public boolean isDelayComplete(final long delay) {
        return System.currentTimeMillis() - this.lastMS > delay;
    }
    
    public boolean reach(final long time) {
        return this.time() >= time;
    }
    
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (this.time() >= time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public boolean hasTimeElapsed(final long time) {
        return this.time() >= time;
    }
    
    public boolean hasReached(final double milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }
    
    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
}
