package me.bobthe28th.bmsts.gamebase.minions.entities;

import me.bobthe28th.bmsts.gamebase.GameTeam;
import me.bobthe28th.bmsts.gamebase.minions.MinionEntity;
import me.bobthe28th.bmsts.gamebase.minions.Rarity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.event.entity.EntityTargetEvent;

public class LlamaEntity extends Llama implements MinionEntity {

    GameTeam team;
    Rarity rarity;
    Boolean preview;

    public LlamaEntity(Location loc, GameTeam team, Rarity rarity, Boolean preview, FileConfiguration config) {
        super(EntityType.LLAMA, ((CraftWorld) loc.getWorld()).getHandle());
        this.team = team;
        this.rarity = rarity;
        this.preview = preview;
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        this.setCanPickUpLoot(false);
        if (!preview) {
            this.setCustomNameVisible(true);
        }
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.25, 40, 20.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this,1F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestEnemyTargetGoal(this));
    }

    public boolean hurt(DamageSource damagesource, float f) {
        if (!super.hurt(damagesource, f)) {
            return false;
        } else if (!(this.level instanceof ServerLevel)) {
            return false;
        } else {
            Entity damager;
            if (damagesource.getEntity() instanceof Projectile p) {
                damager = p.getOwner();
            } else {
                damager = damagesource.getEntity();
            }
            if (damager instanceof LivingEntity) {
                if (damager instanceof MinionEntity dm && this.getGameTeam() != dm.getGameTeam()) {
                    this.setTarget((LivingEntity) damager, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY, true);
                    if (getTarget() != null) {
                        for (Entity eE : this.level.getEntities().getAll()) {
                            if (eE instanceof LivingEntity e) {
                                if (e instanceof MinionEntity t && this.getGameTeam() == t.getGameTeam()) {
                                    if (e instanceof Mob m) {
                                        if (m.getTarget() == null || m.position().distanceToSqr(getTarget().position()) < m.position().distanceToSqr(m.getTarget().position())) {
                                            m.setTarget(getTarget(), EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY, true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    @Override
    public GameTeam getGameTeam() {
        return team;
    }

    @Override
    public boolean isPreview() {
        return preview;
    }
}
