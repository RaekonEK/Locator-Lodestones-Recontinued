package com.one_studios.locator_lodestones.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import com.one_studios.locator_lodestones.config.ConfigManager;
import com.one_studios.locator_lodestones.config.DisplaySetting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.Hud;

@Mixin(Hud.class)
public abstract class HudMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "nextContextualInfoState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void forceLocatorBarWhenNeeded(CallbackInfoReturnable<Hud.ContextualInfo> info) {
        if (ConfigManager.tabDisplaySetting() != DisplaySetting.TAB_FORCES) return;

        boolean canShow = ConfigManager.shouldShowInSpectator() || (minecraft.player != null && !minecraft.player.isSpectator());
        if (canShow && minecraft.options.keyPlayerList.isDown()) {
            info.setReturnValue(Hud.ContextualInfo.LOCATOR);
        }
    }

    @WrapOperation(
            method = "nextContextualInfoState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Hud;willPrioritizeExperienceInfo()Z"
            )
    )
    private boolean blockLocatorBarWhenNeeded(Hud instance, Operation<Boolean> original) {
        if (ConfigManager.tabDisplaySetting() == DisplaySetting.TAB_ONLY) {
            return !minecraft.options.keyPlayerList.isDown();
        } else {
            return original.call(instance);
        }
    }
}
