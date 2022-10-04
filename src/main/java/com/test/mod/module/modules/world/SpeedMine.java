package com.test.mod.module.modules.world;

import com.test.mod.Utils.Side;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import com.test.mod.Utils.Connection;
import java.lang.reflect.Field;
import com.test.mod.Client;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.Minecraft;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.EnableSetting;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import com.test.mod.module.Module;

public class SpeedMine extends Module
{
    public BlockPos blockPos;
    public EnumFacing facing;
    public C07PacketPlayerDigging curPacket;
    private boolean bzs;
    private float bzx;
    private EnableSetting RangeBreak;
    private EnableSetting C03;
    private EnableSetting pot;
    private EnableSetting ultraBoost;
    private EnableSetting Reflection;
    public boolean packet;
    public boolean damage;
    
    public SpeedMine() {
        super("SpeedMine", 0, ModuleType.World, false);
        this.bzs = false;
        this.bzx = 0.0f;
        this.RangeBreak = new EnableSetting("RangeBreak", false);
        this.C03 = new EnableSetting("Packet", false);
        this.pot = new EnableSetting("Pot", false);
        this.ultraBoost = new EnableSetting("UltraBoost", false);
        this.Reflection = new EnableSetting("Reflection", false);
        this.packet = true;
        this.damage = true;
        this.getSettings().add(this.C03);
        this.getSettings().add(this.pot);
        this.getSettings().add(this.ultraBoost);
        this.getSettings().add(this.Reflection);
        this.getSettings().add(this.RangeBreak);
    }
    
    public static void setCurBlockDamageMP(Minecraft mc, float damage) {
        try {
            Field m = PlayerControllerMP.class.getDeclaredField(Client.isObfuscate ? "field_78770_f" : "curBlockDamageMP");
            m.setAccessible(true);
            m.set(mc.playerController, damage);
            m.setAccessible(false);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }
    }
    
    public static float getCurBlockDamageMP(Minecraft mc) {
        try {
            Field m = PlayerControllerMP.class.getDeclaredField(Client.isObfuscate ? "field_78770_f" : "curBlockDamageMP");
            m.setAccessible(true);
            float curv = (float)m.get(mc.playerController);
            m.setAccessible(false);
            return curv;
        }
        catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
            return 0.0f;
        }
    }
    
    @Override
    public boolean onPacket(Object packet, Side side) {
        if (this.Reflection.getEnable()) {
            if (!SpeedMine.mc.playerController.isInCreativeMode()) {
                Field field = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "curBlockDamageMP", "field_78770_f" });
                Field blockdelay = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "blockHitDelay", "field_78781_i" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (!blockdelay.isAccessible()) {
                        blockdelay.setAccessible(true);
                    }
                    blockdelay.setInt(SpeedMine.mc.playerController, 0);
                    if (field.getFloat(SpeedMine.mc.playerController) >= 0.7f) {
                        field.setFloat(SpeedMine.mc.playerController, 1.0f);
                    }
                }
                catch (Exception ex) {}
            }
        }
        else {
            try {
                if (packet instanceof C07PacketPlayerDigging && packet != this.curPacket && !SpeedMine.mc.playerController.extendedReach() && SpeedMine.mc.playerController != null) {
                    if (packet instanceof C07PacketPlayerDigging) {
                        C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)packet;
                        if (this.RangeBreak.getEnable()) {
                            if (c07PacketPlayerDigging.getFacing() == EnumFacing.DOWN || c07PacketPlayerDigging.getFacing() == EnumFacing.UP) {
                                BlockPos BP1 = new BlockPos(c07PacketPlayerDigging.getPosition().getX() + 1, c07PacketPlayerDigging.getPosition().getY(), c07PacketPlayerDigging.getPosition().getZ());
                                this.breakblock(BP1);
                                BlockPos BP2 = new BlockPos(c07PacketPlayerDigging.getPosition().getX() - 1, c07PacketPlayerDigging.getPosition().getY(), c07PacketPlayerDigging.getPosition().getZ());
                                this.breakblock(BP2);
                                BlockPos BP3 = new BlockPos(c07PacketPlayerDigging.getPosition().getX(), c07PacketPlayerDigging.getPosition().getY(), c07PacketPlayerDigging.getPosition().getZ() + 1);
                                this.breakblock(BP3);
                                BlockPos BP4 = new BlockPos(c07PacketPlayerDigging.getPosition().getX(), c07PacketPlayerDigging.getPosition().getY(), c07PacketPlayerDigging.getPosition().getZ() - 1);
                                this.breakblock(BP4);
                            }
                            else {
                                BlockPos BP1 = new BlockPos(c07PacketPlayerDigging.getPosition().getX() + 1, c07PacketPlayerDigging.getPosition().getY(), c07PacketPlayerDigging.getPosition().getZ());
                                this.breakblock(BP1);
                                BlockPos BP2 = new BlockPos(c07PacketPlayerDigging.getPosition().getX() - 1, c07PacketPlayerDigging.getPosition().getY(), c07PacketPlayerDigging.getPosition().getZ());
                                this.breakblock(BP2);
                                BlockPos BP3 = new BlockPos(c07PacketPlayerDigging.getPosition().getX(), c07PacketPlayerDigging.getPosition().getY() + 1, c07PacketPlayerDigging.getPosition().getZ());
                                this.breakblock(BP3);
                                BlockPos BP4 = new BlockPos(c07PacketPlayerDigging.getPosition().getX(), c07PacketPlayerDigging.getPosition().getY() - 1, c07PacketPlayerDigging.getPosition().getZ());
                                this.breakblock(BP4);
                            }
                        }
                    }
                    C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)packet;
                    if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                        this.bzs = true;
                        this.blockPos = c07PacketPlayerDigging.getPosition();
                        this.facing = c07PacketPlayerDigging.getFacing();
                        this.bzx = 0.0f;
                        this.onBreak(this.blockPos, this.facing);
                    }
                    else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.bzs = false;
                        this.blockPos = null;
                        this.facing = null;
                        this.onBreak(this.blockPos, this.facing);
                    }
                }
            }
            catch (Exception ex2) {}
        }
        return true;
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        try {
            if (this.pot.getEnable()) {
                SpeedMine.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, 1));
            }
            if (SpeedMine.mc.playerController.extendedReach()) {
                setBlockHitDelay(SpeedMine.mc, 0);
                if (getCurBlockDamageMP(SpeedMine.mc) >= 0.6f) {
                    setCurBlockDamageMP(SpeedMine.mc, 1.0f);
                }
            }
            else if (this.bzs) {
                Block block = SpeedMine.mc.theWorld.getBlockState(this.blockPos).getBlock();
                this.bzx += block.getPlayerRelativeBlockHardness((EntityPlayer)SpeedMine.mc.thePlayer, (World)SpeedMine.mc.theWorld, this.blockPos) * 8.48798316E-315;
                if (this.bzx >= 1.0f) {
                    SpeedMine.mc.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                    C07PacketPlayerDigging packet = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing);
                    this.curPacket = packet;
                    if (this.C03.getEnable()) {
                        new C03PacketPlayer.C04PacketPlayerPosition(SpeedMine.mc.thePlayer.posX, SpeedMine.mc.thePlayer.posY, SpeedMine.mc.thePlayer.posZ, true);
                    }
                    SpeedMine.mc.thePlayer.sendQueue.getNetworkManager().sendPacket((Packet)packet);
                    this.bzx = 0.0f;
                    this.bzs = false;
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void setBlockHitDelay(Minecraft mc, int delay) {
        try {
            Field m = PlayerControllerMP.class.getDeclaredField(Client.isObfuscate ? "field_78781_i" : "blockHitDelay");
            m.setAccessible(true);
            m.set(mc.playerController, delay);
            m.setAccessible(false);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }
    }
    
    public void onBreak(BlockPos pos, EnumFacing facing) {
        try {
            if (this.canBreak(pos)) {
                if (this.ultraBoost.getEnable()) {
                    SpeedMine.mc.thePlayer.swingItem();
                    SpeedMine.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
                    SpeedMine.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
                }
                if (this.ultraBoost.getEnable()) {
                    setBlockHitDelay(SpeedMine.mc, 0);
                    if (getCurBlockDamageMP(SpeedMine.mc) >= 0.7f) {
                        setCurBlockDamageMP(SpeedMine.mc, 1.0f);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    private void breakblock(BlockPos newBP) {
        MinecraftServer server = MinecraftServer.getServer();
        EntityPlayerMP playerEntity = server.getConfigurationManager().getPlayerByUsername(SpeedMine.mc.thePlayer.getName());
        playerEntity.theItemInWorldManager.onBlockClicked(newBP, EnumFacing.DOWN);
        SpeedMine.mc.thePlayer.swingItem();
        playerEntity.theItemInWorldManager.blockRemoving(newBP);
    }
    
    private boolean canBreak(BlockPos pos) {
        try {
            IBlockState blockState = SpeedMine.mc.theWorld.getBlockState(pos);
            Block block = blockState.getBlock();
            return block.getBlockHardness((World)SpeedMine.mc.theWorld, pos) != -1.0f;
        }
        catch (Exception ex) {
            return true;
        }
    }
}
