package com.pixop.sdk.services.videos.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * {
 *  "metrics": [
 *              {
 *               "name": "noise",
 *               "sampleMean": 0.14742042,
 *               "subjectiveLabel": "good",
 *               "subjectiveScore": 3.0483122
 *              },
 *              {
 *               "name": "details",
 *               "sampleMean": 0.2734557,
 *               "subjectiveLabel": "good",
 *               "subjectiveScore": 3.6174307
 *              },
 *              {
 *               "name": "colors",
 *               "sampleMean": 0.13250291,
 *               "subjectiveLabel": "good",
 *               "subjectiveScore": 3.1855805
 *              }
 *             ],
 *  "overallSubjectiveScore": 3.3455958
 * }
 */

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoQualityAssessment implements java.io.Serializable {

    private static final long serialVersionUID = 2840338257614626613L;

    private Metric[] metrics;
    private Double overallSubjectiveScore;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Metric implements java.io.Serializable {

        private static final long serialVersionUID = -1408573928554780333L;

        private String name;
        private Double sampleMean;
        private String subjectiveLabel;
        private Double subjectiveScore;

        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public Double getSampleMean() {
            return this.sampleMean;
        }

        public void setSampleMean(final Double sampleMean) {
            this.sampleMean = sampleMean;
        }

        public String getSubjectiveLabel() {
            return this.subjectiveLabel;
        }

        public void setSubjectiveLabel(final String subjectiveLabel) {
            this.subjectiveLabel = subjectiveLabel;
        }

        public Double getSubjectiveScore() {
            return this.subjectiveScore;
        }

        public void setSubjectiveScore(final Double subjectiveScore) {
            this.subjectiveScore = subjectiveScore;
        }

    }

    public VideoQualityAssessment() {
    }

    public final Metric[] getMetrics() {
        return this.metrics;
    }

    public final void setMetrics(final Metric[] metrics) {
        this.metrics = metrics;
    }

    public final Double getOverallSubjectiveScore() {
        return this.overallSubjectiveScore;
    }

    public final void setOverallSubjectiveScore(final Double overallSubjectiveScore) {
        this.overallSubjectiveScore = overallSubjectiveScore;
    }

}