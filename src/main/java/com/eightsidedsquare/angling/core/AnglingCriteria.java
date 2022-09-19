package com.eightsidedsquare.angling.core;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.util.Identifier;

import static com.eightsidedsquare.angling.core.AnglingMod.MOD_ID;

public class AnglingCriteria {

    public static final TickCriterion TRADED_WITH_PELICAN = Criteria.register(new TickCriterion(new Identifier(MOD_ID, "traded_with_pelican")));

    public static void init() {}

}
