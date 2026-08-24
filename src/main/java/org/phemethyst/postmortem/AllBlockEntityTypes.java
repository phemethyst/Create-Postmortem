package org.phemethyst.postmortem;

import org.phemethyst.postmortem.content.kinetics.CreatePOSTMORTEMGeneratorBlockEntity;
import org.phemethyst.postmortem.content.kinetics.CreatePOSTMORTEMKineticBlockEntity;
import org.phemethyst.postmortem.content.kinetics.CreatePOSTMORTEMShaftRenderer;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

/**
 * Block entity type registration.
 */
public class AllBlockEntityTypes {

    /**
     * Block entity for EXAMPLE_KINETIC_BLOCK, rendered with CreatePOSTMORTEMShaftRenderer so a
     * shaft visibly spins through the casing.
     */
    public static final BlockEntityEntry<CreatePOSTMORTEMKineticBlockEntity> EXAMPLE_KINETIC = CreatePOSTMORTEM.REGISTRATE
            .blockEntity("postmortem_kinetic", CreatePOSTMORTEMKineticBlockEntity::new)
            // visual for flywheel renderer
            .visual(() -> ShaftVisual::new)
            .validBlock(AllBlocks.EXAMPLE_KINETIC_BLOCK)
            // fallback renderer if flywheel is not available
            .renderer(() -> CreatePOSTMORTEMShaftRenderer::new)
            .register();

    /**
     * Block entity for EXAMPLE_GENERATOR_BLOCK, also rendered with CreatePOSTMORTEMShaftRenderer.
     */
    public static final BlockEntityEntry<CreatePOSTMORTEMGeneratorBlockEntity> EXAMPLE_GENERATOR = CreatePOSTMORTEM.REGISTRATE
            .blockEntity("postmortem_generator", CreatePOSTMORTEMGeneratorBlockEntity::new)
            .visual(() -> ShaftVisual::new)
            .validBlock(AllBlocks.EXAMPLE_GENERATOR_BLOCK)
            .renderer(() -> CreatePOSTMORTEMShaftRenderer::new)
            .register();

    public static void register() {
        // Force class loading to trigger Registrate calls
    }
}
