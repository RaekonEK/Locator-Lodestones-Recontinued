package com.one_studios.locator_lodestones.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import com.one_studios.locator_lodestones.WaypointNameRendering;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.contextualbar.ContextualBar;
import net.minecraft.client.gui.contextualbar.LocatorBar;

import net.minecraft.client.gui.GuiGraphicsExtractor;

@Mixin(LocatorBar.class)
public abstract class LocatorBarMixin implements ContextualBar {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "extractRenderState",
            at = @At("RETURN")
    )
    private void renderClientWaypoints(GuiGraphicsExtractor graphics, DeltaTracker tickCounter, CallbackInfo ci) {
        WaypointNameRendering.renderNames(this.minecraft, graphics, tickCounter, this.top(this.minecraft.getWindow()));
    }
}
