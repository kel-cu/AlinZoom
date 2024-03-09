package ru.kelcuprum.alinzoom.mixin;

import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinzoom.AlinZoom;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Inject(at = @At("RETURN"), method = "onScroll")
    private void onScroll(long windowPointer, double xOffset, double yOffset, CallbackInfo ci){
        AlinZoom.onMouseScroll(yOffset);
    }

    @Redirect(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V"))
    private void swapPoint(Inventory instance, double direction){
        if(!(AlinZoom.zoomKey.isDown() && AlinZoom.config.getBoolean("SCROLL", true))) instance.swapPaint(direction);
    }
}
