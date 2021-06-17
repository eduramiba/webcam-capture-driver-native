package com.github.eduramiba.webcamcapture.drivers.capturemanager.model;

import com.github.eduramiba.webcamcapture.utils.Utils;
import java.util.Collections;
import java.util.List;

public class CaptureManagerStreamDescriptor {

    private final String majorType;
    private final String majorTypeGUID;
    private final String frameSourceTypes;
    private final String categoryName;
    private final String categoryGUID;
    private final String streamName;
    private final List<CaptureManagerMediaType> mediaTypes;

    public CaptureManagerStreamDescriptor(String majorType, String majorTypeGUID, String frameSourceTypes, String categoryName, String categoryGUID, String streamName, List<CaptureManagerMediaType> mediaTypes) {
        this.majorType = Utils.trimToEmpty(majorType);
        this.majorTypeGUID = Utils.trimToEmpty(majorTypeGUID);
        this.frameSourceTypes = Utils.trimToEmpty(frameSourceTypes);
        this.categoryName = Utils.trimToEmpty(categoryName);
        this.categoryGUID = Utils.trimToEmpty(categoryGUID);
        this.streamName = Utils.trimToEmpty(streamName);
        this.mediaTypes = Utils.coalesce(mediaTypes, Collections.emptyList());
    }

    public String getMajorType() {
        return majorType;
    }

    public String getMajorTypeGUID() {
        return majorTypeGUID;
    }

    public String getFrameSourceTypes() {
        return frameSourceTypes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryGUID() {
        return categoryGUID;
    }

    public String getStreamName() {
        return streamName;
    }

    public List<CaptureManagerMediaType> getMediaTypes() {
        return mediaTypes;
    }

    public boolean isVideoStream() {
        return "MFMediaType_Video".equalsIgnoreCase(majorType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaptureManagerStreamDescriptor that = (CaptureManagerStreamDescriptor) o;

        if (!majorType.equals(that.majorType)) {
            return false;
        }
        if (!majorTypeGUID.equals(that.majorTypeGUID)) {
            return false;
        }
        if (!frameSourceTypes.equals(that.frameSourceTypes)) {
            return false;
        }
        if (!categoryName.equals(that.categoryName)) {
            return false;
        }
        if (!categoryGUID.equals(that.categoryGUID)) {
            return false;
        }
        if (!streamName.equals(that.streamName)) {
            return false;
        }
        return mediaTypes.equals(that.mediaTypes);
    }

    @Override
    public int hashCode() {
        int result = majorType.hashCode();
        result = 31 * result + majorTypeGUID.hashCode();
        result = 31 * result + frameSourceTypes.hashCode();
        result = 31 * result + categoryName.hashCode();
        result = 31 * result + categoryGUID.hashCode();
        result = 31 * result + streamName.hashCode();
        result = 31 * result + mediaTypes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CaptureManagerStreamDescriptor{" + "majorType=" + majorType + ", majorTypeGUID=" + majorTypeGUID + ", frameSourceTypes=" + frameSourceTypes + ", categoryName=" + categoryName + ", categoryGUID=" + categoryGUID + ", streamName=" + streamName + ", mediaTypes=" + mediaTypes + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String majorType;
        private String majorTypeGUID;
        private String frameSourceTypes;
        private String categoryName;
        private String categoryGUID;
        private String streamName;
        private List<CaptureManagerMediaType> mediaTypes;

        private Builder() {
        }

        public Builder majorType(String majorType) {
            this.majorType = majorType;
            return this;
        }

        public Builder majorTypeGUID(String majorTypeGUID) {
            this.majorTypeGUID = majorTypeGUID;
            return this;
        }

        public Builder frameSourceTypes(String frameSourceTypes) {
            this.frameSourceTypes = frameSourceTypes;
            return this;
        }

        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder categoryGUID(String categoryGUID) {
            this.categoryGUID = categoryGUID;
            return this;
        }

        public Builder streamName(String streamName) {
            this.streamName = streamName;
            return this;
        }

        public Builder mediaTypes(List<CaptureManagerMediaType> mediaTypes) {
            this.mediaTypes = mediaTypes;
            return this;
        }

        public CaptureManagerStreamDescriptor build() {
            return new CaptureManagerStreamDescriptor(majorType, majorTypeGUID, frameSourceTypes, categoryName, categoryGUID, streamName, mediaTypes);
        }
    }
}
