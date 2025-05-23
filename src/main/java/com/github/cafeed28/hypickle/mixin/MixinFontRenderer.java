package com.github.cafeed28.hypickle.mixin;

import net.minecraft.client.gui.FontRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {
    /// it's basically "(hypi)(x)(e)(l)" but with minecraft's § formatting
    private static final Pattern hypixelPattern = Pattern.compile("(?i)((?:§\\S)*h(?:§\\S)*y(?:§\\S)*p(?:§\\S)*i)((?:§\\S)*)(x)((?:§\\S)*)(e)((?:§\\S)*)(l)");

    private static String valueOrEmpty(String value) {
        if (value == null) return "";
        else return value;
    }

    private static String hypickle(String text) {
        try {
            Matcher matcher = hypixelPattern.matcher(text);

            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String ckFormat = valueOrEmpty(matcher.group(2));
                String ck = Character.isLowerCase(matcher.group(3).codePointAt(0)) ? "ck" : "CK";

                String lFormat = valueOrEmpty(matcher.group(4));
                String l = Character.isLowerCase(matcher.group(5).codePointAt(0)) ? "l" : "L";

                String eFormat = valueOrEmpty(matcher.group(6));
                String e = Character.isLowerCase(matcher.group(7).codePointAt(0)) ? "e" : "E";

                String hypickle = matcher.group(1) + ckFormat + ck + lFormat + l + eFormat + e;
                matcher.appendReplacement(sb, hypickle);
            }
            matcher.appendTail(sb);

            return sb.toString();
        } catch (Exception e) {
            System.out.println(text);
            e.printStackTrace();
            return text;
        }
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true)
    private String getStringWidth_text(String text) {
        return hypickle(text);
    }

    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true)
    private String renderStringAtPos_text(String text) {
        return hypickle(text);
    }
}
