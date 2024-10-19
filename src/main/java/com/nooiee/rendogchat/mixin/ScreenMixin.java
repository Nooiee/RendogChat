package com.nooiee.rendogchat.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nooiee.rendogchat.config.ConfigManager;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.lwjgl.glfw.GLFW;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    // 위아래 키가 눌릴 때 포커스가 이동하지 않도록 방지
    @WrapWithCondition(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;switchFocus(Lnet/minecraft/client/gui/navigation/GuiNavigationPath;)V"))
    private boolean cancelChatSwitchFocus(Screen screen, GuiNavigationPath path, int keyCode, int scanCode, int modifiers) {
        // ChatScreen일 때 위/아래 키 입력이 포커스를 변경하지 않도록 설정
        return !(screen instanceof ChatScreen && (keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_DOWN));
    }

}
