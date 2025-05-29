package net.hir0shiyt.selfrestraint.network;

import net.hir0shiyt.selfrestraint.SelfRestraint;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class NetworkingConstants {
    public static final Identifier SET_LIMITER_ID = Identifier.of(SelfRestraint.MOD_ID, "example");
    public static final CustomPayload.Id SET_LIMITER_PAYLOAD_ID = new CustomPayload.Id(SET_LIMITER_ID);
}
