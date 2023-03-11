package com.eightsidedsquare.angling.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "angling")
public class AnglingConfig implements ConfigData {
    @ConfigEntry.Category("client")
    public boolean hideWaterBehindGlass = true;
}
