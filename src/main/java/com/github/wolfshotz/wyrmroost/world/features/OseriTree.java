package com.github.wolfshotz.wyrmroost.world.features;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

// todo
public class OseriTree extends Tree
{
    private final Type type;

    public OseriTree(Type type)
    {
        this.type = type;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> createTreeFeature(Random p_225546_1_, boolean p_225546_2_)
    {
        return null;
    }

    public enum Type { BLUE, GOLD, PINK, PURPLE, WHITE }
}
