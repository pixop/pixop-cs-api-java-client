package com.pixop.sdk.services.videos.model;

public enum InterlacedFieldOrder {
    TOP_FIRST("top_first"),
    BOTTOM_FIRST("bottom_first");

    private final String key;

    InterlacedFieldOrder(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static InterlacedFieldOrder getMatching(final String name) {
        if (name != null)
            for (final InterlacedFieldOrder interlacedFieldOrder : values())
                if (interlacedFieldOrder.getKey().equalsIgnoreCase(name))
                    return interlacedFieldOrder;
        return null;
    }
}
