package ru.kelcuprum.alinzoom.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.slider.SliderIntegerBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.slider.SliderPercentBuilder;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.alinzoom.AlinZoom;

public class ConfigScreen {
    public Screen build(Screen parent){
        return new ConfigScreenBuilder(parent, Component.translatable("alinzoom"), InterfaceUtils.DesignType.FLAT)
                .addPanelWidget(new ButtonBuilder(Component.translatable("alinzoom.configs"), (o) -> {
                    AlinZoom.MINECRAFT.setScreen(new ConfigScreen().build(parent));
                }).build())
                .addWidget(new TextBox(Component.translatable("alinzoom.configs"), true))
                .addWidget(new SliderIntegerBuilder(Component.translatable("alinzoom.configs.zoom.mount")).setDefaultValue(3).setMin(1).setMax(100).setConfig(AlinZoom.config, "ZOOM.MOUNT").build())
                .addWidget(new SliderIntegerBuilder(Component.translatable("alinzoom.configs.zoom.max")).setDefaultValue(7).setMin(1).setMax(100).setConfig(AlinZoom.config, "ZOOM.MAX").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("alinzoom.configs.zoom.smooth_camera"), true).setConfig(AlinZoom.config, "ZOOM.SMOOTH_CAMERA").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("alinzoom.configs.scroll"), true).setConfig(AlinZoom.config, "SCROLL").build())
                .addWidget(new SliderPercentBuilder(Component.translatable("alinzoom.configs.scroll.mount")).setDefaultValue(0.1).setConfig(AlinZoom.config, "SCROLL.MOUNT").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("alinzoom.configs.zoom.mouse_sensitivity"), false).setConfig(AlinZoom.config, "ZOOM.MOUSE_SENSITIVITY").build())
                .build();
    }
}
