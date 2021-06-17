package com.github.eduramiba.webcamcapture.drivers.capturemanager.model;

import com.github.eduramiba.webcamcapture.utils.Utils;

public class CaptureManagerMediaType {

    private final int width;
    private final int height;
    private final String majorType;
    private final String subType;

    public CaptureManagerMediaType(int width, int height, String majorType, String subType) {
        this.width = width;
        this.height = height;
        this.majorType = Utils.trimToEmpty(majorType);
        this.subType = Utils.trimToEmpty(subType);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getMajorType() {
        return majorType;
    }

    public String getSubType() {
        return subType;
    }

    @Override
    public String toString() {
        return "CaptureManagerMediaType{" + "width=" + width + ", height=" + height + ", majorType=" + majorType + ", subType=" + subType + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaptureManagerMediaType that = (CaptureManagerMediaType) o;

        if (width != that.width) {
            return false;
        }
        if (height != that.height) {
            return false;
        }
        if (!majorType.equals(that.majorType)) {
            return false;
        }
        return subType.equals(that.subType);
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + majorType.hashCode();
        result = 31 * result + subType.hashCode();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private int width;
        private int height;
        private String majorType;
        private String subType;

        private Builder() {
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder majorType(String majorType) {
            this.majorType = majorType;
            return this;
        }

        public Builder subType(String subType) {
            this.subType = subType;
            return this;
        }

        public CaptureManagerMediaType build() {
            return new CaptureManagerMediaType(width, height, majorType, subType);
        }
    }

}
