package net.hir0shiyt.selfrestraint.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LeverWidget extends ClickableWidget {

    private static final Identifier HANDLE_TEXTURE = Identifier.of("selfrestraint", "textures/gui/modifier_screen_scroller.png");

    private float percentage; // From 0.0 to 1.0
    private final OnValueChanged onChange;

    public LeverWidget(int x, int y, int width, int height, float initialPercent, OnValueChanged onChange) {
        super(x, y, width, height, Text.empty());
        this.percentage = initialPercent;
        this.onChange = onChange;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int handleWidth = 12;
        int handleHeight = 15;
        int extraRangeLeft = 32;

        int handleX = (int) ((this.getX() - extraRangeLeft) + percentage * (this.getWidth() - handleWidth + extraRangeLeft));
        int handleY = this.getY();

        context.drawTexture(HANDLE_TEXTURE, handleX, handleY, 0, 0, handleWidth, handleHeight, handleWidth, handleHeight);
    }



    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.active && this.visible) {
            int extraRangeLeft= 32;
            float newPercent = (float) ((mouseX - (this.getX() - extraRangeLeft)) / (this.getWidth() - 12 + extraRangeLeft));
            this.percentage = Math.max(0f, Math.min(1f, newPercent));
            onChange.onChange(this.percentage);
            return true;
        }
        return false;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.mouseDragged(mouseX, mouseY, 0, 0, 0);
    }

    public float getPercentage() {
        return this.percentage;
    }

    public interface OnValueChanged {
        void onChange(float value);
    }
}
