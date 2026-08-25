package org.phemethyst.postmortem.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import com.simibubi.create.content.fluids.tank.BoilerData;
import org.spongepowered.asm.mixin.Shadow;

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
    public float getEngineEfficiency(int boilerSize) {
        throw new AssertionError();
    }

    @Shadow
    public boolean isActive() {
        throw new AssertionError();
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
