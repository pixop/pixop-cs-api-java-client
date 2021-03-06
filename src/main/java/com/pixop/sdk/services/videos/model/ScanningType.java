package com.pixop.sdk.services.videos.model;

public enum ScanningType {
    PROGRESSIVE("progressive"),
    INTERLACED("interlaced");

    private final String key;

    ScanningType(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static ScanningType getMatching(final String name) {
        if (name != null)
            for (final ScanningType type : values())
                if (type.getKey().equalsIgnoreCase(name))
                    return type;
        return null;
    }
}
