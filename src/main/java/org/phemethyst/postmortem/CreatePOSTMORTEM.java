package org.phemethyst.postmortem;

import java.util.concurrent.CompletableFuture;

import org.phemethyst.postmortem.content.ponder.CreatePOSTMORTEMPonderPlugin;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMCompactingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMCrushingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMCuttingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMDeployingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMEmptyingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMFillingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMLangMerger;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMHauntingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMMillingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMMixingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMPressingRecipeGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMSequencedAssemblyGen;
import org.phemethyst.postmortem.datagen.CreatePOSTMORTEMWashingRecipeGen;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import com.tterrag.registrate.providers.ProviderType;
import net.createmod.ponder.foundation.PonderIndex;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CreatePOSTMORTEM.ID)
public class CreatePOSTMORTEM {
    public static final String ID = "postmortem";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public CreatePOSTMORTEM(IEventBus modBus) {
        REGISTRATE.registerEventListeners(modBus);

        AllCreativeModeTabs.register();
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.MAIN_TAB);
        registerLangPartials();
        registerPonderLang();
        AllItems.register();
        AllDisplaySources.register();
        AllBlocks.register();
        AllBlockEntityTypes.register();

        modBus.addListener(this::onCommonSetup);
        modBus.addListener(this::onClientSetup);
        modBus.addListener(this::onGatherData);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Common setup...");
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Client setup...");
        event.enqueueWork(() -> {
            // Ponder is client-only
            PonderIndex.addPlugin(new CreatePOSTMORTEMPonderPlugin());
        });
    }

    /**
     * Feeds the hand-authored language partials (assets/postmortem/lang/default/*.json)
     * into Registrate's lang provider so runData merges them with the generated block and
     * item names into a single en_us.json. Keeps English copy out of Java, mirroring how
     * Create authors its own translations.
     */
    private void registerLangPartials() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, provider ->
                CreatePOSTMORTEMLangMerger.mergeInto(provider::add));
    }

    /**
     * Feeds the Ponder scenes' text (titles and captions) into Registrate's lang provider
     * so runData writes it into the same en_us.json as the block and item names. The
     * registered callback only runs during data generation, so it is safe to touch the
     * client-only PonderIndex from here.
     */
    private void registerPonderLang() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            PonderIndex.addPlugin(new CreatePOSTMORTEMPonderPlugin());
            PonderIndex.getLangAccess().provideLang(ID, provider::add);
        });
    }

    /**
     * Registers the data generators. Running gradlew runData writes their output into
     * src/generated/resources.
     */
    private void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMSequencedAssemblyGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMWashingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMHauntingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMCrushingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMMillingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMPressingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMCuttingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMMixingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMCompactingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMFillingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMEmptyingRecipeGen(output, registries));
        generator.addProvider(event.includeServer(), new CreatePOSTMORTEMDeployingRecipeGen(output, registries));
    }
}
