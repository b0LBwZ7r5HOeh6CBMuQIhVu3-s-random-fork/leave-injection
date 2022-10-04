package com.test.mod.module.modules.movement;

import com.test.mod.Utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockIce;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.Arrays;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.ModeSetting;
import net.minecraft.util.Timer;
import com.test.mod.module.Module;

public class Speed extends Module
{
    public boolean shouldslow;
    double count;
    int jumps;
    private float air;
    private float ground;
    private float aacSlow;
    public static TimerUtils timer;
    boolean collided;
    boolean lessSlow;
    int spoofSlot;
    double less;
    double stair;
    private double speed;
    private double speedvalue;
    private double lastDist;
    public static int stage;
    public static int aacCount;
    public static Timer timers;
    double movementSpeed;
    TimerUtils aac;
    TimerUtils lastFall;
    TimerUtils lastCheck;
    Timer mctimer;
    private int level;
    private double moveSpeed;
    private double lastDist_;
    private int timerDelay;
    private ModeSetting mode;
    private IntegerSetting speedVaule;
    private EnableSetting lagback;
    private EnableSetting fastfall;
    
    public Speed() {
        super("Speed", 0, ModuleType.Movement, false);
        this.shouldslow = false;
        this.count = 0.0;
        this.collided = false;
        this.spoofSlot = 0;
        this.aac = new TimerUtils();
        this.lastFall = new TimerUtils();
        this.lastCheck = new TimerUtils();
        this.mctimer = (Timer)ReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)Speed.mc, new String[] { Mappings.timer });
        this.level = 1;
        this.moveSpeed = 1.1441801305E-314;
        this.lastDist_ = 0.0;
        this.timerDelay = 0;
        this.mode = new ModeSetting("Mode", "New", Arrays.<String>asList("Basic", "Old", "BHOP", "OnGround", "AAC", "New"), this);
        this.speedVaule = new IntegerSetting("SpeedValue", 0.0, 5.941588215E-315, 0.0, 2);
        this.lagback = new EnableSetting("LagBack", true);
        this.fastfall = new EnableSetting("FastFall", false);
        this.getSettings().add(this.mode);
        this.getSettings().add(this.speedVaule);
        this.getSettings().add(this.lagback);
        this.getSettings().add(this.fastfall);
    }
    
    static {
        Speed.timer = new TimerUtils();
        Speed.timers = (Timer)ReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)Speed.mc, new String[] { Mappings.timer });
    }
    
    public static boolean MineLandMovementInput() {
        return (Speed.mc.gameSettings.keyBindLeft.isKeyDown() || Speed.mc.gameSettings.keyBindRight.isKeyDown() || Speed.mc.gameSettings.keyBindBack.isKeyDown()) && !Speed.mc.gameSettings.keyBindForward.isKeyDown();
    }
    
    @Override
    public boolean onPacket(Object packet, Side side) {
        if (this.lagback.getEnable() && side == Side.IN && packet instanceof S08PacketPlayerPosLook) {
            ChatUtils.warning("Lagback checks->Speed");
            Speed.mc.thePlayer.onGround = false;
            EntityPlayerSP thePlayer = Speed.mc.thePlayer;
            thePlayer.motionX *= 0.0;
            EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
            thePlayer2.motionZ *= 0.0;
            Speed.mc.thePlayer.jumpMovementFactor = 0.0f;
            this.toggleModule(true);
            Speed.stage = -4;
        }
        return super.onPacket(packet, side);
    }
    
    @Override
    public void onEnable() {
        this.less = 0.0;
        this.jumps = 0;
        this.count = 0.0;
        this.lastDist = 0.0;
        Speed.stage = 2;
        this.air = 0.0f;
        Speed.timers.timerSpeed = 1.0f;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Timer timer = (Timer)ReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)Speed.mc, new String[] { Mappings.timer });
        timer.timerSpeed = 1.0f;
        this.moveSpeed = this.baseMoveSpeed();
        this.level = 0;
        super.onDisable();
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.mode.getCurrent().equals("Basic")) {
            boolean boost = Math.abs(Speed.mc.thePlayer.rotationYawHead - Speed.mc.thePlayer.rotationYaw) < 90.0f;
            if (Speed.mc.thePlayer.moveForward > 0.0f && Speed.mc.thePlayer.hurtTime < 5) {
                if (Speed.mc.thePlayer.onGround) {
                    Speed.mc.thePlayer.motionY = 2.54639495E-315;
                    float f = getDirection();
                    EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                    thePlayer.motionX -= MathHelper.sin(f) * 0.2f;
                    EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                    thePlayer2.motionZ += MathHelper.cos(f) * 0.2f;
                }
                else {
                    double currentSpeed = Math.sqrt(Speed.mc.thePlayer.motionX * Speed.mc.thePlayer.motionX + Speed.mc.thePlayer.motionZ * Speed.mc.thePlayer.motionZ);
                    double speed = boost ? 1.880937069E-314 : 1.2222695754E-314;
                    double direction = (double)getDirection();
                    Speed.mc.thePlayer.motionX = -Math.sin(direction) * speed * currentSpeed;
                    Speed.mc.thePlayer.motionZ = Math.cos(direction) * speed * currentSpeed;
                }
            }
        }
        else if (this.mode.getCurrent().equals("Old")) {
            if (Speed.mc.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                Speed.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.0;
            }
            this.less -= ((this.less > 1.0) ? 1.9522361275E-314 : 1.612716801E-314);
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(5.941588215E-315) && isMoving2()) {
                this.collided = Speed.mc.thePlayer.isCollidedHorizontally;
                if (Speed.stage >= 0 || this.collided) {
                    Speed.stage = 0;
                    double motY = 1.5448129356E-314 + MoveUtils.getJumpEffect() * 1.273197475E-314;
                    if (this.stair == 0.0) {
                        Speed.mc.thePlayer.jump();
                        Speed.mc.thePlayer.motionY = motY;
                    }
                    ++this.less;
                    if (this.less > 1.0 && !this.lessSlow) {
                        this.lessSlow = true;
                    }
                    else {
                        this.lessSlow = false;
                    }
                    if (this.less > 2.54639495E-315) {
                        this.less = 2.54639495E-315;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(Speed.stage) + 1.483699457E-314;
            this.speed *= 6.790386532E-315;
            if (this.stair > 0.0) {
                this.speed *= 8.48798316E-315 - MoveUtils.getSpeedEffect() * 1.273197475E-314;
            }
            if (Speed.stage < 0) {
                this.speed = MoveUtils.defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 8.48798316E-315;
            }
            if (BlockUtils.isInLiquid()) {
                this.speed = 1.273197475E-314;
            }
            if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                this.setMotion(this.speed);
                ++Speed.stage;
            }
        }
        else if (this.mode.getCurrent().equals("BHOP")) {
            if (Speed.mc.thePlayer.moveForward == 0.0f && Speed.mc.thePlayer.moveStrafing == 0.0f) {
                this.speed = MoveUtils.defaultSpeed();
            }
            if (Speed.stage == 1 && Speed.mc.thePlayer.isCollidedVertically && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                this.speed = 1.273197475E-314 + MoveUtils.defaultSpeed() - 5.941588215E-315;
            }
            if (!BlockUtils.isInLiquid() && Speed.stage == 2 && Speed.mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(5.941588215E-315) && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
                    Speed.mc.thePlayer.motionY = 1.3262473857E-314 + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 1.273197475E-314;
                }
                else {
                    Speed.mc.thePlayer.motionY = 1.3262473857E-314;
                }
                Speed.mc.thePlayer.jump();
                this.speed *= 1.6975966E-316;
            }
            else if (Speed.stage == 3) {
                double difference = 6.790386532E-315 * (this.lastDist - MoveUtils.defaultSpeed());
                this.speed = this.lastDist - difference;
            }
            else {
                List collidingList = Speed.mc.theWorld.getCollidingBoundingBoxes((Entity)Speed.mc.thePlayer, Speed.mc.thePlayer.getEntityBoundingBox().offset(0.0, Speed.mc.thePlayer.motionY, 0.0));
                if ((collidingList.size() > 0 || Speed.mc.thePlayer.isCollidedVertically) && Speed.stage > 0) {
                    Speed.stage = ((Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                }
                this.speed = this.lastDist - this.lastDist / 0.0;
            }
            this.speed = Math.max(this.speed, MoveUtils.defaultSpeed());
            if (Speed.stage > 0) {
                if (BlockUtils.isInLiquid()) {
                    this.speed = 1.273197475E-314;
                }
                this.setMotion(this.speed);
            }
            if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                ++Speed.stage;
            }
        }
        else if (this.mode.getCurrent().equals("AAC")) {
            if (Speed.mc.thePlayer.fallDistance > 4.24399158E-315) {
                this.lastFall.reset();
            }
            if (!BlockUtils.isInLiquid() && Speed.mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(5.941588215E-315) && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                Speed.stage = 0;
                Speed.mc.thePlayer.jump();
                EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                EntityPlayerSP thePlayer4 = Speed.mc.thePlayer;
                double n = 1.3262473857E-314 + MoveUtils.getJumpEffect();
                thePlayer4.motionY = n;
                thePlayer3.motionY = n;
                if (Speed.aacCount < 4) {
                    ++Speed.aacCount;
                }
            }
            this.speed = this.getAACSpeed(Speed.stage, Speed.aacCount);
            if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                if (BlockUtils.isInLiquid()) {
                    this.speed = 4.24399158E-315;
                }
                this.setMotion(this.speed);
            }
            if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                ++Speed.stage;
            }
        }
    }
    
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (this.mode.getCurrent().equals("OnGround")) {
            Speed.timers.timerSpeed = 1.085f;
            double forward = (double)Speed.mc.thePlayer.movementInput.moveForward;
            double strafe = (double)Speed.mc.thePlayer.movementInput.moveStrafe;
            if ((forward == 0.0 && strafe == 0.0) || Speed.mc.gameSettings.keyBindJump.isKeyDown() || Speed.mc.thePlayer.isInWater() || Speed.mc.thePlayer.isOnLadder() || Speed.mc.thePlayer.isCollidedHorizontally || !Speed.mc.theWorld.getCollidingBoundingBoxes((Entity)Speed.mc.thePlayer, Speed.mc.thePlayer.getEntityBoundingBox().offset(0.0, 1.273197475E-314, 0.0)).isEmpty()) {}
            this.speed = Math.max((Speed.mc.thePlayer.ticksExisted % 2 == 0) ? 1.697596633E-314 : 1.697596633E-314, MoveUtils.defaultSpeed());
            float yaw = Speed.mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                Speed.mc.thePlayer.motionX = 0.0;
                Speed.mc.thePlayer.motionZ = 0.0;
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 4.24399158E-315;
                    }
                    else if (forward < 0.0) {
                        forward = 4.24399158E-315;
                    }
                }
                if (strafe > 0.0) {
                    strafe = 4.24399158E-315;
                }
                else if (strafe < 0.0) {
                    strafe = 4.24399158E-315;
                }
                Speed.mc.thePlayer.motionX = forward * this.speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * this.speed * Math.sin(Math.toRadians((double)(yaw + 90.0f)));
                Speed.mc.thePlayer.motionZ = forward * this.speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * this.speed * Math.cos(Math.toRadians((double)(yaw + 90.0f)));
            }
        }
        else if (this.mode.getCurrent().equals("New")) {
            if (Speed.mc.thePlayer.onGround && MoveUtils.isMoving()) {
                Speed.mc.thePlayer.jump();
                Speed.stage = 0;
                this.speed = 1.3262473694E-314;
            }
            this.speed -= 1.748524532E-314;
            MoveUtils.setSpeed(MoveUtils.getBaseMoveSpeed() * this.speed);
        }
    }
    
    private double getAACSpeed(int stage, int jumps) {
        double value = 3.395193264E-315;
        double firstvalue = 8.012656107E-315;
        double thirdvalue = 1.083066652E-314 - stage / 0.0;
        if (stage == 0) {
            value = 1.867356296E-315;
            if (jumps >= 2) {
                value += 8.284271566E-315;
            }
            if (jumps >= 3) {
                value += 1.8503803297E-314;
            }
            Block block = MoveUtils.getBlockUnderPlayer((EntityPlayer)Speed.mc.thePlayer, 5.941588215E-315);
            if (block instanceof BlockIce || block instanceof BlockPackedIce) {
                value = 1.4429571377E-314;
            }
        }
        else if (stage == 1) {
            value = 1.150970517E-314;
            if (jumps >= 2) {
                value += 5.66997275E-315;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 2) {
            value = 1.714572599E-314;
            if (jumps >= 2) {
                value += 1.880937069E-314;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 3) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 4.75327055E-316;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 4) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 1.7587101116E-314;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 5) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 1.347891726E-314;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 6) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 1.877541876E-314;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 7) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 2.851962345E-315;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 8) {
            value = firstvalue;
            if (MoveUtils.isOnGround(1.273197475E-314)) {
                value -= 1.748524532E-314;
            }
            if (jumps >= 2) {
                value += 1.755314918E-314;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 9) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 1.1034378113E-314;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 10) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 1.392029239E-314;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 11) {
            value = 4.24399158E-315;
            if (jumps >= 2) {
                value += 9.33678148E-315;
            }
            if (jumps >= 3) {
                value += 1.1713416764E-314;
            }
        }
        else if (stage == 12) {
            value = 1.0694858786E-314;
            if (jumps <= 2) {
                Speed.aacCount = 0;
            }
            if (jumps >= 2) {
                value += 3.05567394E-315;
            }
            if (jumps >= 3) {
                value += thirdvalue + 1.748524532E-314;
            }
        }
        else if (stage == 13) {
            value = 1.256221508E-314;
            if (jumps >= 2) {
                value += 3.05567394E-315;
            }
            if (jumps >= 3) {
                value += thirdvalue + 1.748524532E-314;
            }
        }
        else if (stage == 14) {
            value = 6.111347877E-315;
            if (jumps >= 2) {
                value += 3.05567394E-315;
            }
            if (jumps >= 3) {
                value += thirdvalue + 1.748524532E-314;
            }
        }
        if (Speed.mc.thePlayer.moveForward <= 0.0f) {
            value -= 1.9522361275E-314;
        }
        if (Speed.mc.thePlayer.isCollidedHorizontally) {
            value -= 1.273197475E-314;
            Speed.aacCount = 0;
        }
        return value;
    }
    
    public static boolean MovementInput() {
        return Speed.mc.gameSettings.keyBindForward.isKeyDown() || Speed.mc.gameSettings.keyBindLeft.isKeyDown() || Speed.mc.gameSettings.keyBindRight.isKeyDown() || Speed.mc.gameSettings.keyBindBack.isKeyDown();
    }
    
    private void setMotion(double speed) {
        double forward = (double)Speed.mc.thePlayer.movementInput.moveForward;
        double strafe = (double)Speed.mc.thePlayer.movementInput.moveStrafe;
        float yaw = Speed.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Speed.mc.thePlayer.motionX = 0.0;
            Speed.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = 0.0;
                }
            }
            Speed.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f)));
            Speed.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f)));
        }
    }
    
    private double getHypixelSpeed(int stage) {
        double value = MoveUtils.defaultSpeed() + 4.07423192E-315 * MoveUtils.getSpeedEffect() + MoveUtils.getSpeedEffect() / 0.0;
        double firstvalue = 1.6975966E-316 + MoveUtils.getSpeedEffect() / 0.0;
        double decr = stage / 0.0 * 0.0;
        if (stage == 0) {
            if (Speed.timer.delay(300.0f)) {
                Speed.timer.reset();
            }
            if (!this.lastCheck.delay(500.0f)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            }
            else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 5.941588215E-315 + (MoveUtils.getSpeedEffect() + 4.07423192E-315 * MoveUtils.getSpeedEffect()) * 1.0015820135E-314;
        }
        else if (stage == 1) {
            value = firstvalue;
        }
        else if (stage >= 2) {
            value = firstvalue - decr;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0f) || this.collided) {
            value = 1.273197475E-314;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : (MoveUtils.defaultSpeed() + 4.07423192E-315 * MoveUtils.getSpeedEffect()));
    }
    
    private double defaultSpeed() {
        double baseSpeed = 1.1441801305E-314;
        return baseSpeed;
    }
    
    private double baseMoveSpeed() {
        double baseSpeed = 1.1441801305E-314;
        if (Speed.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 1.273197475E-314 * (Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static void setSpeed(double speed) {
        Speed.mc.thePlayer.motionX = -Math.sin(getDirection()) * speed;
        Speed.mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }
    
    public int getSpeedEffect() {
        if (Speed.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static float getDirection(float yaw) {
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }
    
    public static float getDirection() {
        float var1 = Speed.mc.thePlayer.rotationYaw;
        if (Speed.mc.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Speed.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Speed.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Speed.mc.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Speed.mc.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        return var1 *= 0.017453292f;
    }
    
    public static boolean isMoving2() {
        return Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f;
    }
}
