package org.phemethyst.postmortem.datagen;

import java.util.concurrent.CompletableFuture;

import org.phemethyst.postmortem.CreatePOSTMORTEM;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

/**
 * Filling recipe generator. Shows an item combined with a fluid ingredient; the amount
 * is in millibuckets (1000 mB is one bucket).
 */
public class CreatePOSTMORTEMFillingRecipeGen extends FillingRecipeGen {

    GeneratedRecipe EXAMPLE = create("postmortem_filling", b -> b
            .require(Items.BUCKET)
            .require(Fluids.WATER, 1000)
            .output(Items.WATER_BUCKET));

    public CreatePOSTMORTEMFillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePOSTMORTEM.ID);
    }
}
