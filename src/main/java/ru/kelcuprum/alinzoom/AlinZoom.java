package ru.kelcuprum.alinzoom;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import org.lwjgl.glfw.GLFW;
import ru.kelcuprum.alinlib.config.Config;

import static net.minecraft.util.Mth.clamp;

public class AlinZoom implements ClientModInitializer {
    public static  KeyMapping zoomKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                    "alinzoom.zoom",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_C, // The keycode of the key
                    "alinzoom"
    ));
    public static Config config = new Config("config/AlinZoom.json");
    public static Minecraft MINECRAFT = Minecraft.getInstance();
    public static Double currentLevel;
    public static long lastTime = 0;
    public static long lastBackTime = 0;
    public static boolean back = false;
    public static Double defaultMouseSensitivity;
    public static Boolean cinemaMode = false;

    @Override
    public void onInitializeClient() {

    }
    public static double changeFov(double fov){
        OptionInstance<Double> mouse = MINECRAFT.options.mouseWheelSensitivity();
        if(currentLevel == null) currentLevel = (double) Math.min(config.getNumber("ZOOM.MOUNT", 3).intValue(), config.getNumber("ZOOM.MAX", 7).intValue());
        if(lastTime == 0) lastTime = System.currentTimeMillis();
        if(!zoomKey.isDown()){
            if(back){
                if(lastBackTime == 0) lastBackTime = System.currentTimeMillis();
                if(lastTime != 0) lastTime = 0;
                double peepohuy = (double) Math.min(System.currentTimeMillis() - lastBackTime, 2000) / 2000;
                if(peepohuy >= 1.0) peepohuy = 1.0; else if(peepohuy <= 0.01) peepohuy = 0.01;
                double changedFov = fov * Math.min(1, currentLevel*peepohuy);
                if(fov == changedFov) back = false;
                return Math.min(fov, changedFov);
            } else {
                currentLevel = null;
                if(lastBackTime != 0) lastBackTime = 0;
                if(lastTime != 0) lastTime = 0;
                if(defaultMouseSensitivity != null && config.getBoolean("ZOOM.MOUSE_SENSITIVITY", false)){
                    mouse.set(defaultMouseSensitivity);
                    defaultMouseSensitivity = null;
                }
                if(cinemaMode && config.getBoolean("ZOOM.SMOOTH_CAMERA", true)){
                    if(cinemaMode) cinemaMode = false;
                    MINECRAFT.options.smoothCamera = false;
                }
                return fov;
            }
        }
        if(config.getBoolean("ZOOM.MOUSE_SENSITIVITY", false)) {
            if (defaultMouseSensitivity == null) defaultMouseSensitivity = mouse.get();
            mouse.set(defaultMouseSensitivity * (currentLevel*0.1));
        }
        if(!cinemaMode && config.getBoolean("ZOOM.SMOOTH_CAMERA", true)){
            cinemaMode = true;
            MINECRAFT.options.smoothCamera = true;
        }
        back = true;
        if(lastBackTime != 0) lastBackTime = 0;
        double peepohuy = (double) Math.min(System.currentTimeMillis() - lastTime, 400) / 400;
        if(peepohuy >= 1.0) peepohuy = 1.0; else if(peepohuy <= 0.01) peepohuy = 0.01;
        double changedFov = fov / (currentLevel*peepohuy);
        return Math.min(fov, changedFov);
    }
    public static void onMouseScroll(double mount){
        if(!zoomKey.isDown() || !config.getBoolean("SCROLL", true)) return;
        if(currentLevel == null) currentLevel = (double) Math.min(config.getNumber("ZOOM.MOUNT", 3).intValue(), config.getNumber("ZOOM.MAX", 7).intValue());
        if(mount > 0) currentLevel *= (1.0+config.getNumber("SCROLL.MOUNT", 0.1).doubleValue());
        else if(mount < 0) currentLevel *= (1.0-config.getNumber("SCROLL.MOUNT", 0.1).doubleValue());
        else return;
        currentLevel = clamp(currentLevel, 1, config.getNumber("ZOOM.MAX", 7).intValue());
    }
}
