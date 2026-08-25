package org.phemethyst.postmortem.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.SoundPool;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import com.simibubi.create.content.fluids.tank.BoilerData;
import org.spongepowered.asm.mixin.Shadow;

import java.util.EnumMap;
import java.util.List;

@Mixin(BoilerData.class)
public class BoilerDataMixin  {
    @Shadow
    private int maxHeatForSize = 0;
    @Shadow
    private int maxHeatForWater = 0;
    @Shadow
    public float waterSupply;
    @Shadow
    public int attachedEngines;
    @Shadow
    public int activeHeat;
    @Shadow
    public boolean passiveHeat;
    @Shadow
    private static final int waterSupplyPerLevel = 10;
    @Shadow
    static final int SAMPLE_RATE = 5;
    @Shadow
    public LerpedFloat gauge = LerpedFloat.linear();
    @Shadow
    private final EnumMap<Direction, SoundPool> pools = new EnumMap<>(Direction.class);
    @Shadow
    public boolean needsHeatLevelUpdate;
    @Shadow
    int ticksUntilNextSample;
    @Shadow
    float[] supplyOverTime = new float[10];
    @Shadow
    int currentIndex;
    @Shadow
    int gatheredSupply;

    @Shadow
    public void calcMinMaxForSize(int boilerSize) {}

    @Shadow
    public MutableComponent getHeatLevelTextComponent() {
        throw new AssertionError();
    }

    @Shadow
    public MutableComponent getSizeComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        throw new AssertionError();
    }

    @Shadow
    public MutableComponent getWaterComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        throw new AssertionError();
    }

    @Shadow
    public MutableComponent getHeatComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        throw new AssertionError();
    }

    @Shadow
    public boolean updateTemperature(FluidTankBlockEntity controller) {
        throw new AssertionError();
    }

    @Shadow
    public float getEngineEfficiency(int boilerSize) {
        throw new AssertionError();
    }

    @Shadow
    public boolean isActive() {
        throw new AssertionError();
    }

    @Shadow
    private int getActualHeat(int boilerSize) {
        throw new AssertionError();
    }

    private int tankSize; // The size of the boiler's tank. Used for calculating maximum pressure.

    private static final int maxPressurePerTank = 80000;
    private static final int pressurePerHeatLevel = 4;

    private int currentPressure; // The current pressure of the boiler.
    private int maxPressure; // The maximum pressure of the boiler.

    public void tick(FluidTankBlockEntity controller) {
        if (!isActive())
            return;

        tankSize = controller.getTotalTankSize();
        maxPressure = tankSize * maxPressurePerTank;

        currentPressure = (int) (currentPressure + waterSupply * activeHeat * pressurePerHeatLevel);

        Level level = controller.getLevel();
        if (level.isClientSide) {
            pools.values().forEach(p -> p.play(level));
            gauge.tickChaser();
            float current = gauge.getValue(1);
            if (current > 1 && level.random.nextFloat() < 1 / 2f)
                gauge.setValueNoUpdate(current + Math.min(-(current - 1) * level.random.nextFloat(), 0));
            return;
        }
        if (needsHeatLevelUpdate && updateTemperature(controller))
            controller.notifyUpdate();
        ticksUntilNextSample--;
        if (ticksUntilNextSample > 0)
            return;
        int capacity = controller.getTankInventory().getCapacity();
        if (capacity == 0)
            return;

        ticksUntilNextSample = SAMPLE_RATE;
        supplyOverTime[currentIndex] = gatheredSupply / (float) SAMPLE_RATE;
        waterSupply = Math.max(waterSupply, supplyOverTime[currentIndex]);
        currentIndex = (currentIndex + 1) % supplyOverTime.length;
        gatheredSupply = 0;

        if (currentIndex == 0) {
            waterSupply = 0;
            for (float i : supplyOverTime)
                waterSupply = Math.max(i, waterSupply);
        }

        if (controller instanceof CreativeFluidTankBlockEntity)
            waterSupply = waterSupplyPerLevel * 20;

        if (getActualHeat(controller.getTotalTankSize()) == 18)
            controller.award(AllAdvancements.STEAM_ENGINE_MAXED);

        controller.notifyUpdate();
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, int boilerSize) {
        if (!isActive())
            return false;

        calcMinMaxForSize(boilerSize);

        CreateLang.translate("boiler.status", getHeatLevelTextComponent().withStyle(ChatFormatting.GREEN))
                .forGoggles(tooltip);
        CreateLang.builder().add(getSizeComponent(true, false)).forGoggles(tooltip, 1);
        CreateLang.builder().add(getWaterComponent(true, false)).forGoggles(tooltip, 1);
        CreateLang.builder().add(getHeatComponent(true, false)).forGoggles(tooltip, 1);

        tooltip.add(CommonComponents.EMPTY);

        CreateLang.translate("boiler.pressure")
                .style(ChatFormatting.LIGHT_PURPLE)
                .add(CreateLang.text(" "))
                .add(CreateLang.number(currentPressure))
                    .style(ChatFormatting.AQUA)
                .add(CreateLang.text("PU/"))
                    .style(ChatFormatting.GRAY)
                .add(CreateLang.number(maxPressure))
                    .style(ChatFormatting.GRAY)
                .add(CreateLang.text("PU"))
                    .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, 1);

        if (attachedEngines == 0)
            return true;

        int boilerLevel = Math.min(activeHeat, Math.min(maxHeatForWater, maxHeatForSize));
        double totalSU = getEngineEfficiency(boilerSize) * 16 * Math.max(boilerLevel, attachedEngines)
                * BlockStressValues.getCapacity(AllBlocks.STEAM_ENGINE.get());

        tooltip.add(CommonComponents.EMPTY);

        CreateLang.translate("boiler.water_input_rate")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        CreateLang.number(waterSupply)
                .style(ChatFormatting.BLUE)
                .add(CreateLang.translate("generic.unit.millibuckets"))
                .add(CreateLang.text(" / ")
                        .style(ChatFormatting.GRAY))
                .add(CreateLang.translate("boiler.per_tick", CreateLang.number(waterSupplyPerLevel * boilerLevel)
                                .add(CreateLang.translate("generic.unit.millibuckets")))
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

        CreateLang.translate("tooltip.capacityProvided")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        CreateLang.number(totalSU)
                .translate("generic.unit.stress")
                .style(ChatFormatting.AQUA)
                .space()
                .add((attachedEngines == 1 ? CreateLang.translate("boiler.via_one_engine")
                        : CreateLang.translate("boiler.via_engines", attachedEngines)).style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

        tooltip.add(CommonComponents.EMPTY);

        CreateLang.text("Currently being tainted by POSTMORTEM...")
                .style(ChatFormatting.LIGHT_PURPLE)
                .forGoggles(tooltip);

        CreateLang.text("more fun soon(tm)")
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);

        return true;
    }
}
