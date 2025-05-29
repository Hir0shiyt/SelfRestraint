package net.hir0shiyt.selfrestraint.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SetLimiterPayload(float speed, float jump) implements CustomPayload {
    public static final Id<SetLimiterPayload> ID = new Id<>(Identifier.of("selfrestraint", "set_limiter"));

    public static final PacketCodec<RegistryByteBuf, SetLimiterPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, SetLimiterPayload::speed,
            PacketCodecs.FLOAT, SetLimiterPayload::jump,
            SetLimiterPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

