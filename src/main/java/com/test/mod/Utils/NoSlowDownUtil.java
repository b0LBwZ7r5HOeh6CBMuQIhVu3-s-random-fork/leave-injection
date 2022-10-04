package com.test.mod.Utils;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;

public class NoSlowDownUtil extends MovementInput
{
    private GameSettings gameSettings;
    boolean NoSlowBoolean;
    
    public NoSlowDownUtil(final GameSettings par1GameSettings) {
        super();
        this.NoSlowBoolean = true;
        this.gameSettings = par1GameSettings;
    }
    
    public void updatePlayerMoveState() {
        super.moveStrafe = 0.0f;
        super.moveForward = 0.0f;
        if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++super.moveForward;
        }
        if (this.gameSettings.keyBindBack.isKeyDown()) {
            --super.moveForward;
        }
        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++super.moveStrafe;
        }
        if (this.gameSettings.keyBindRight.isKeyDown()) {
            --super.moveStrafe;
        }
        super.jump = this.gameSettings.keyBindJump.isKeyDown();
        super.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        if (super.sneak) {
            super.moveStrafe *= 4.24399158E-315;
            super.moveForward *= 4.24399158E-315;
        }
        if (this.NoSlowBoolean) {
            super.moveStrafe *= 5.0f;
            super.moveForward *= 5.0f;
        }
    }
    
    public void setNSD(final boolean a) {
        this.NoSlowBoolean = a;
    }
}
