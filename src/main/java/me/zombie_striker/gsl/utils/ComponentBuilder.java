package me.zombie_striker.gsl.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.awt.*;

public class ComponentBuilder {

    public static final Color RED = new Color(200,24,20);
    public static final Color BLUE = new Color(02,20,200);
    public static final Color WHITE = new Color(200,200,200);
    public static final Color GREEN = new Color (40,200,40);

    private Component core;

    public ComponentBuilder(String message, Color color){
        core = Component.text(message).color(TextColor.color(color.getRGB()));
    }
    public ComponentBuilder append(String message, Color color){
        core=core.append(Component.text(message).color(TextColor.color(color.getRGB())));
        return this;
    }

    public Component build() {
        return core;
    }
}
