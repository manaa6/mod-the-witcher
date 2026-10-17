package com.witchermod.sign;

public enum SignType {
    //        chave                  cooldown(ticks)  cor RGB    exaustão
    AARD("sign.witcher.aard", 60, 0x66CCFF, 1.0f),
    IGNI("sign.witcher.igni", 100, 0xFF6A1A, 2.0f),
    QUEN("sign.witcher.quen", 600, 0xFFD93B, 3.0f),
    YRDEN("sign.witcher.yrden", 400, 0xB45CFF, 3.0f),
    AXII("sign.witcher.axii", 300, 0x5CFF8A, 2.0f);

    private final String translationKey;
    private final int cooldown;
    private final int color;
    private final float exhaustion;

    SignType(String translationKey, int cooldown, int color, float exhaustion) {
        this.translationKey = translationKey;
        this.cooldown = cooldown;
        this.color = color;
        this.exhaustion = exhaustion;
    }

    public String translationKey() { return translationKey; }
    public int cooldown() { return cooldown; }
    public int color() { return color; }
    public float exhaustion() { return exhaustion; }

    public static SignType byIndex(int index) {
        SignType[] all = values();
        return all[Math.floorMod(index, all.length)];
    }
}
