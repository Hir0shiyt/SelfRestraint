package net.hir0shiyt.selfrestraint.screen.custom;

import net.hir0shiyt.selfrestraint.player.PlayerSpeedJumpLimiter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.hir0shiyt.selfrestraint.player.PlayerSpeedJumpLimiter.getLimiterForPlayer;

public class ModifierScreen extends Screen {
    private static final Identifier TEXTURE = Identifier.of("selfrestraint", "textures/gui/modifier_screen.png");
    private final int backgroundWidth = 256;
    private final int backgroundHeight = 166;

    public ModifierScreen() {
        super(Text.literal("Modifier"));
    }

    public boolean isBackgroundBlurred() {
        return false; // disable background blur
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // do nothing, no dim or blur
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xC0101010);

        int x = (this.width - backgroundWidth) / 2;
        int y = (this.height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 166);

        // Get limiter values
        PlayerSpeedJumpLimiter limiter = getLimiterForPlayer(this.client.player);
        int speedPercent = (int)(limiter.getSpeedLimiter() * 100);
        int jumpPercent = (int)(limiter.getJumpLimiter() * 100);

        // Build the texts
        String speedText = "Speed Modifier : " + speedPercent + "%";
        String jumpText = "Jump Modifier : " + (jumpPercent >= 49 ? "Limiter toggled" : "Limiter untoggled");

        // Draw texts at positions relative to GUI top-left (x, y)
        int textX = x + 20;  // 20 px from left border
        int speedTextY = y + 10;  // 10 px down from top border
        int jumpTextY = speedTextY + 12; // 12 px below the speedText

        context.drawText(this.textRenderer, speedText, textX, speedTextY, 0xFFFFFF, false);
        context.drawText(this.textRenderer, jumpText, textX, jumpTextY, 0xFFFFFF, false);

        super.render(context, mouseX, mouseY, delta);
    }


    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        int guiCenter = height / 2;
        int centerX = (width - 256) / 2;
        int sliderX = centerX + (256 - 100) / 2 + 16;

        int sliderY1 = guiCenter - 37 - (15 / 2); // Upper slider
        int sliderY2 = guiCenter + 37 - (15 / 2); // Lower slider

        PlayerSpeedJumpLimiter limiter = getLimiterForPlayer(this.client.player);

        this.addDrawableChild(new LeverWidget(sliderX, sliderY1, 100, 15, limiter.getSpeedLimiter(), value -> {
            limiter.setSpeedLimiter(value);
        }));

        this.addDrawableChild(new LeverWidget(sliderX, sliderY2, 100, 15, limiter.getJumpLimiter(), value -> {
            limiter.setJumpLimiter(value < 0.5f ? 0f : 1f);
        }));
    }
}