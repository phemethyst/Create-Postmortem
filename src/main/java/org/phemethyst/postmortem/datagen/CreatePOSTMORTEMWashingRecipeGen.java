package org.phemethyst.postmortem.datagen;

import java.util.concurrent.CompletableFuture;

import org.phemethyst.postmortem.AllItems;
import org.phemethyst.postmortem.CreatePOSTMORTEM;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.api.data.recipe.WashingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

/**
 * Splashing recipe generator.
 */
public class CreatePOSTMORTEMWashingRecipeGen extends WashingRecipeGen {

    GeneratedRecipe EXAMPLE = create("postmortem_washing",
            b -> b.require(Items.DIRT).output(AllItems.EXAMPLE_ITEM.get()));

    public CreatePOSTMORTEMWashingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePOSTMORTEM.ID);
    }
}
