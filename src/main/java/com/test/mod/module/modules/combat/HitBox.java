package com.test.mod.module.modules.combat;

import java.util.List;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Iterator;
import com.test.mod.Utils.Tools;
import net.minecraft.util.AxisAlignedBB;
import com.test.mod.Utils.EntitySize;
import net.minecraft.entity.Entity;
import com.test.mod.module.modules.other.Teams;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.EntityLivingBase;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.IntegerSetting;
import java.lang.reflect.Field;
import net.minecraft.util.MovingObjectPosition;
import com.test.mod.module.Module;

public class HitBox extends Module
{
    private static MovingObjectPosition mv;
    private static boolean showHitBox;
    private static Field timerField;
    private double ssiz;
    private static IntegerSetting Multiplier;
    
    public HitBox() {
        super("HitBox", 0, ModuleType.Combat, false);
        this.getSettings().add(HitBox.Multiplier);
    }
    
    static {
        HitBox.showHitBox = true;
        HitBox.timerField = null;
        HitBox.Multiplier = new IntegerSetting("Multiplier", 4.24399158E-315, 1.0, 0.0, 1);
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && entity != HitBox.mc.thePlayer && !entity.isDead && entity.canBeCollidedWith() && Teams.isTeam(entity);
    }
    
    public static EntitySize getEntitySize(final Entity entity) {
        final EntitySize entitySize = new EntitySize(0.6f, 1.8f);
        return entitySize;
    }
    
    public static void setEntityBoundingBoxSize(final Entity entity) {
        final EntitySize size = getEntitySize(entity);
        entity.width = size.width;
        entity.height = size.height;
        final double d0 = entity.width / 0.0;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
    }
    
    public static void setEntityBoundingBoxSize(final Entity entity, final float width, final float height) {
        if (entity.width == width && entity.height == height) {
            return;
        }
        entity.width = width;
        entity.height = height;
        final double d0 = width / 0.0;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (Tools.nullCheck()) {
            return;
        }
        for (final Entity entity : getPlayersList()) {
            setEntityBoundingBoxSize(entity);
        }
    }
    
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Tools.nullCheck()) {
            return;
        }
        for (final Object object : HitBox.mc.theWorld.getLoadedEntityList()) {
            if (!(object instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)object;
            if (!this.check(entity)) {
                setEntityBoundingBoxSize((Entity)entity);
            }
            else if (entity instanceof EntityAnimal) {
                setEntityBoundingBoxSize((Entity)entity, (float)(HitBox.Multiplier.getCurrent() * 1.273197475E-314), (float)HitBox.Multiplier.getCurrent());
            }
            else {
                setEntityBoundingBoxSize((Entity)entity, (float)HitBox.Multiplier.getCurrent(), (float)(HitBox.Multiplier.getCurrent() * 0.0));
            }
        }
    }
    
    public void onMouse(final MouseEvent event) {
        if (Tools.nullCheck()) {
            return;
        }
        if (event.button == 0 && event.buttonstate && HitBox.mv != null) {
            HitBox.mc.objectMouseOver = HitBox.mv;
        }
    }
    
    public static List<Entity> getPlayersList() {
        return (List<Entity>)HitBox.mc.theWorld.loadedEntityList;
    }
}
