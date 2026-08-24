package org.phemethyst.postmortem.content.kinetics;

import org.phemethyst.postmortem.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Example kinetic generator. It uses the same axle-style base as CreatePOSTMORTEMKineticBlock;
 * the difference is entirely in the block entity, CreatePOSTMORTEMGeneratorBlockEntity.
 */
public class CreatePOSTMORTEMGeneratorBlock extends RotatedPillarKineticBlock implements IBE<CreatePOSTMORTEMGeneratorBlockEntity> {

    public CreatePOSTMORTEMGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public Class<CreatePOSTMORTEMGeneratorBlockEntity> getBlockEntityClass() {
        return CreatePOSTMORTEMGeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CreatePOSTMORTEMGeneratorBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.EXAMPLE_GENERATOR.get();
    }
}
