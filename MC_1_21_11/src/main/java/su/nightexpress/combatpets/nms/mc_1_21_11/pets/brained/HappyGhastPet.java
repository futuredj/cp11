package su.nightexpress.combatpets.nms.mc_1_21_11.pets.brained;

import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.combatpets.api.pet.PetEntity;
import su.nightexpress.combatpets.nms.mc_1_21_11.brain.PetAI;
import su.nightexpress.combatpets.nms.mc_1_21_11.brain.PetBrain;

public class HappyGhastPet extends HappyGhast implements PetEntity {

    public HappyGhastPet(@NotNull ServerLevel level) {
        super(EntityType.HAPPY_GHAST, level);
    }

    @Override
    public void setGoals() {

    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        Brain.Provider provider = Brain.provider(PetBrain.MEMORY_TYPES, PetBrain.getSensorTypes(this));
        Brain brain = provider.makeBrain(dynamic);
        return PetBrain.refreshBrainRaw(this, brain);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller filler = Profiler.get();
        filler.push("happyGhastBrain");
        Brain brain = this.getBrain();
        brain.tick(level, this);
        filler.pop();
        filler.push("happyGhastActivityUpdate");
        this.updateActivity();
        filler.pop();
    }

    protected void updateActivity() {
        PetAI.updateActivity(this, this.getBrain());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData groupData) {
        return groupData;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return this.level().isClientSide() ? InteractionResult.CONSUME : InteractionResult.SUCCESS_SERVER;
    }

    @Override
    protected void hurtArmor(DamageSource source, float amount) {
        this.doHurtEquipment(source, amount, PetAI.ARMOR_SLOTS);
    }
}
