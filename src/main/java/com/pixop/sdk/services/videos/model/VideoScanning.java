package com.pixop.sdk.services.videos.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoScanning implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("metadata")
    private ScanningContext metadata;

    @JsonProperty("heuristics")
    private ScanningContext heuristics;

    @JsonCreator
    public VideoScanning(@JsonProperty("metadata") final ScanningContext _metadata,
                         @JsonProperty("heuristics") final ScanningContext _heuristics) {
        metadata = _metadata;
        heuristics = _heuristics;
    }

    public ScanningContext getMetadata() {
        return metadata;
    }

    public void setMetadata(final ScanningContext _metadata) {
        metadata = _metadata;
    }

    public ScanningContext getHeuristics() {
        return heuristics;
    }

    public void setHeuristics(final ScanningContext _heuristics) {
        heuristics = _heuristics;
    }
}
