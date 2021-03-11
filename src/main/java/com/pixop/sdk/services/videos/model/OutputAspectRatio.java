package com.pixop.sdk.services.videos.model;

public enum OutputAspectRatio {
    AR_DISPLAY("display"),
    AR_STORAGE("storage"),
    AR_16_9("16:9"),
    AR_4_3("4:3");

    private final String key;

    OutputAspectRatio(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static OutputAspectRatio getMatching(final String name) {
        if (name != null)
            for (final OutputAspectRatio type : values())
                if (type.getKey().equalsIgnoreCase(name))
                    return type;
        return null;
    }
}
