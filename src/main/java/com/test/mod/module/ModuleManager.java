package com.test.mod.module;

import java.util.Iterator;
import com.test.mod.module.modules.other.Teams;
import com.test.mod.module.modules.movement.Speed;
import com.test.mod.module.modules.world.NoFall;
import com.test.mod.module.modules.combat.SuperKnockback;
import com.test.mod.module.modules.world.Regen;
import com.test.mod.module.modules.combat.AimBot;
import com.test.mod.module.modules.combat.HitBox;
import com.test.mod.module.modules.world.SpeedMine;
import com.test.mod.module.modules.combat.Criticals;
import com.test.mod.module.modules.world.FuckBed;
import com.test.mod.module.modules.render.BlockESP;
import com.test.mod.module.modules.movement.NoSlowDown;
import com.test.mod.module.modules.combat.Velocity;
import com.test.mod.module.modules.combat.ChestStealer;
import com.test.mod.module.modules.movement.InvMove;
import com.test.mod.module.modules.other.AntiAFK;
import com.test.mod.module.modules.world.AutoTool;
import com.test.mod.module.modules.movement.Fly;
import com.test.mod.module.modules.combat.AutoPotion;
import com.test.mod.module.modules.movement.Scaffold;
import com.test.mod.module.modules.world.FastPlace;
import com.test.mod.module.modules.render.Hud;
import com.test.mod.module.modules.render.Xray;
import com.test.mod.module.modules.render.Projectiles;
import com.test.mod.module.modules.render.Nametags;
import com.test.mod.module.modules.render.ItemESP;
import com.test.mod.module.modules.render.FullBright;
import com.test.mod.module.modules.combat.AutoClicker;
import com.test.mod.module.modules.render.Chams;
import com.test.mod.module.modules.render.ESP;
import com.test.mod.module.modules.combat.Reach;
import com.test.mod.module.modules.other.Command;
import com.test.mod.module.modules.movement.Sprint;
import java.util.ArrayList;

public class ModuleManager
{
    private ArrayList<Module> modules;
    
    public ModuleManager() {
        super();
        this.modules = new ArrayList<Module>();
    }
    
    public void add(final Module... modules) {
        for (final Module module : modules) {
            this.modules.add(module);
        }
    }
    
    public void loadModules() {
        this.add(new Sprint(), new Command(), new Reach(), new ESP(), new Chams(), new AutoClicker(), new FullBright(), new ItemESP(), new Nametags(), new Projectiles(), new Xray(), new Hud(), new FastPlace(), new Scaffold(), new AutoPotion(), new Fly(), new AutoTool(), new AntiAFK(), new InvMove(), new ChestStealer(), new Velocity(), new NoSlowDown(), new BlockESP(), new FuckBed(), new Criticals(), new SpeedMine(), new HitBox(), new AimBot(), new Regen(), new SuperKnockback(), new NoFall(), new Speed(), new Teams());
    }
    
    public ArrayList<Module> getModules() {
        return this.modules;
    }
    
    public Module getModule(final String name) {
        for (final Module module : this.modules) {
            if (module.getName().equals(name)) {
                return module;
            }
        }
        return null;
    }
}
