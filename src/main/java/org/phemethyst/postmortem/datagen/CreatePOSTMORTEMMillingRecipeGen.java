package org.phemethyst.postmortem.datagen;

import java.util.concurrent.CompletableFuture;

import org.phemethyst.postmortem.CreatePOSTMORTEM;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider.GeneratedRecipe;
import com.simibubi.create.api.data.recipe.MillingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

/**
 * Milling recipe generator.
 */
public class CreatePOSTMORTEMMillingRecipeGen extends MillingRecipeGen {

    GeneratedRecipe EXAMPLE = create("postmortem_milling", b -> b
            .require(Items.COBBLESTONE)
            .output(Items.SAND)
            .duration(100));

    public CreatePOSTMORTEMMillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePOSTMORTEM.ID);
    }
}
