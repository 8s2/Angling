package com.eightsidedsquare.angling.client.renderer;

import com.eightsidedsquare.angling.client.model.CrabEntityModel;
import com.eightsidedsquare.angling.common.entity.CrabEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CrabEntityRenderer extends GeoEntityRenderer<CrabEntity> {
    public CrabEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CrabEntityModel());
    }
}
