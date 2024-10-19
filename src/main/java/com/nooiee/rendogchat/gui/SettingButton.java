package com.nooiee.rendogchat.gui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingButton extends ButtonWidget {
    public SettingButton(int x, int y, int width, int height, PressAction onPress) {
        super(x, y, width, height, Text.literal("Settings"), onPress, textSupplier -> Text.literal("설정 화면으로 이동합니다"));
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
