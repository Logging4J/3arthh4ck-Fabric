package me.earth.earthhack.impl.gui.chat.components.setting;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.util.TextUtil;
import me.earth.earthhack.impl.gui.chat.clickevents.SuppliedRunnableClickEvent;
import me.earth.earthhack.impl.gui.chat.components.SettingComponent;
import me.earth.earthhack.impl.gui.chat.components.SuppliedComponent;
import me.earth.earthhack.impl.gui.chat.components.SuppliedHoverableComponent;
import me.earth.earthhack.impl.gui.chat.util.ChatComponentUtil;
import me.earth.earthhack.impl.gui.chat.util.ColorEnum;
import me.earth.earthhack.impl.gui.chat.util.RainbowEnum;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.text.*;

import java.awt.*;
import java.util.function.Supplier;

public class ColorComponent extends SettingComponent<Color, ColorSetting>
{
    private int otherSettings;

    public ColorComponent(ColorSetting setting)
    {
        super(setting);

        if (!(setting.getContainer() instanceof Module))
        {
            return;
        }

        Module m = (Module) setting.getContainer();

        for (ColorEnum e : ColorEnum.values())
        {
            this.append(supply(() ->
                    TextColor.GRAY + " +" + e.getTextColor(), 0)
                .setStyle(Style.EMPTY
                    .withHoverEvent(getHoverEvent(e.name(), true))
                    .withClickEvent(
                        new SuppliedRunnableClickEvent(() ->
                            e.getCommand(setting, true, m))
                )));

            this.append(supply(() -> e.getValue(setting) + "", 0)
                    .setStyle(Style.EMPTY
                            .withHoverEvent(
                                new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Text.empty().append(e.name() +
                                                            " <0 - 255>")))
                            .withClickEvent(new ClickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    Commands.getPrefix()
                                            + "hiddensetting "
                                            + m.getName()
                                            + " "
                                            + "\"" + setting.getName() + "\"")
                                    )));

            this.append(supply(() ->
                    TextColor.GRAY + "- " + TextColor.RESET, 0)
                .setStyle(Style.EMPTY
                .withHoverEvent(getHoverEvent(e.name(), false))
                .withClickEvent(
                    new SuppliedRunnableClickEvent(() ->
                            e.getCommand(setting, false, m)))));
        }

        // more settings:
        this.append(supply(() -> (setting.isSync()
                ? TextColor.GREEN : TextColor.RED) + " Sync", 1)
            .setStyle(Style.EMPTY
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                Text.empty().append("Un/Sync this color.")))
            .withClickEvent(
                new SuppliedRunnableClickEvent(() -> () ->
                    setting.setSync(!setting.isSync())))));

        this.append(supply(() -> (setting.isRainbow()
                ? TextColor.GREEN : TextColor.RED) + " Rainbow", 1)
            .setStyle(Style.EMPTY
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    Text.empty().append("Make this color rainbow.")))
                .withClickEvent(
                    new SuppliedRunnableClickEvent(() -> () ->
                            setting.setRainbow(!setting.isRainbow())))));

        this.append(supply(() -> (setting.isStaticRainbow()
            ? TextColor.GREEN : TextColor.RED) + " Static", 1)
            .setStyle(Style.EMPTY
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    Text.empty().append(
                        "Make this color a static rainbow.")))
            .withClickEvent(
                new SuppliedRunnableClickEvent(() -> () ->
                    setting.setStaticRainbow(!setting.isStaticRainbow())))));

        // even more settings:
        for (RainbowEnum r : RainbowEnum.values())
        {
            this.append(supply(() ->
                    TextColor.GRAY + " +" + r.getColor(), 2)
                .setStyle(Style.EMPTY
                .withHoverEvent(getFloatEvent(r.name(), true))
                .withClickEvent(
                    new SuppliedRunnableClickEvent(() ->
                            r.getCommand(setting, true, m)))));

            this.append(supply(() -> r.getValue(setting) + "", 2)
                    .setStyle(Style.EMPTY
                        .withHoverEvent(
                                new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        Text.empty().append(r.name() +
                                            " " + r.getRange())))
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                Commands.getPrefix()
                                        + "hiddensetting "
                                        + m.getName()
                                        + " "
                                        + "\"" + setting.getName() + "\"")
                        )));

            this.append(supply(() ->
                    TextColor.GRAY + "- " + TextColor.RESET, 2)
                .setStyle(Style.EMPTY
                    .withHoverEvent(getFloatEvent(r.name(), false))
                    .withClickEvent(
                        new SuppliedRunnableClickEvent(() ->
                                r.getCommand(setting, false, m)
                    ))));
        }

        this.append(Text.empty().append(
                TextColor.GRAY + " \u2699")
            .setStyle(Style.EMPTY
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new SuppliedComponent(() ->
                    {
                        switch (this.otherSettings)
                        {
                            case 0:
                                return "Show more settings";
                            case 1:
                                return "Show rainbow settings.";
                            case 2:
                                return "Show r,g,b settings.";
                            default:
                                throw new IllegalStateException();
                        }
                    })))
                .withClickEvent(
                        new SuppliedRunnableClickEvent(() -> () ->
                            this.otherSettings = (++this.otherSettings) % 3))));
    }

    @Override
    public String getText()
    {
        if (setting.isRainbow() || setting.isStaticRainbow())
        {
            return super.getText()
                + TextColor.RAINBOW
                + "\u2588"; // filled rectangle
        }

        return super.getText()
                + TextColor.CUSTOM
                + TextUtil.get32BitString(setting.getValue().getRGB())
                + "\u2588"; // filled rectangle
    }

    private HoverEvent getHoverEvent(String color, boolean incr)
    {
        return ChatComponentUtil.setOffset(
            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                Text.empty().append((incr ? "In" : "De")
                    + "crement " + color.toLowerCase()
                    + " value by 1. Hold: " + TextColor.RED + "ALT "
                    + TextColor.WHITE + ": 10," + TextColor.RED + " RCTRL"
                    + TextColor.WHITE + " : " + (incr ? "Max" : "Min")
                    + TextColor.RED + " LCTRL " + TextColor.WHITE + ": 5%, "
                    + TextColor.RED + "LCTRL + ALT " + TextColor.WHITE
                    + ": 10%")));
    }

    private HoverEvent getFloatEvent(String color, boolean incr)
    {
        return ChatComponentUtil.setOffset(
            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                Text.empty().append((incr ? "In" : "De")
                    + "crement " + color.toLowerCase()
                    + " value by 0.1. Hold: " + TextColor.RED + "ALT "
                    + TextColor.WHITE + ": 1.0, " + TextColor.RED
                    + "RCTRL " + TextColor.WHITE + ": Max,"
                    + " " + TextColor.RED + "LCTRL " + TextColor.WHITE
                    + ": 5%, " + TextColor.RED + "LCTRL + ALT "
                    + TextColor.WHITE + ": 10%")));
    }

    private SuppliedComponent supply(Supplier<String> s,
                                     int isOtherSettings)
    {
        return new SuppliedHoverableComponent(() ->
        {
            if (this.otherSettings == isOtherSettings)
            {
                return s.get();
            }

            return "";
        }, () -> otherSettings == isOtherSettings);
    }

    @Override
    public MutableText copy()
    {
        ColorComponent component = new ColorComponent(this.setting);
        component.otherSettings = this.otherSettings;
        return component;
    }

}

