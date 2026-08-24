package org.phemethyst.postmortem.datagen;

import java.util.concurrent.CompletableFuture;

import org.phemethyst.postmortem.AllItems;
import org.phemethyst.postmortem.CreatePOSTMORTEM;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.api.data.recipe.PressingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

/**
 * Pressing recipe generator.
 */
public class CreatePOSTMORTEMPressingRecipeGen extends PressingRecipeGen {

    GeneratedRecipe EXAMPLE = create("postmortem_pressing", b -> b
            .require(Items.IRON_INGOT)
            .output(AllItems.EXAMPLE_ITEM.get()));

    public CreatePOSTMORTEMPressingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePOSTMORTEM.ID);
    }
}
