package com.github.eduramiba.webcamcapture.drivers.capturemanager.model;

import com.github.eduramiba.webcamcapture.utils.Utils;
import java.util.Collections;
import java.util.List;

public class CaptureManagerSource {

    private final String symbolicLink;
    private final String deviceLink;
    private final String friendlyName;
    private final String majorType;
    private final String subType;
    private final boolean isSoftwareDevice;
    private final List<CaptureManagerStreamDescriptor> streamDescriptors;

    public CaptureManagerSource(String symbolicLink, String deviceLink, String friendlyName, String majorType, String subType, boolean isSoftwareDevice, List<CaptureManagerStreamDescriptor> streamDescriptors) {
        this.symbolicLink = Utils.trimToEmpty(symbolicLink);
        this.deviceLink = Utils.trimToEmpty(deviceLink);
        this.friendlyName = Utils.trimToEmpty(friendlyName);
        this.majorType = Utils.trimToEmpty(majorType);
        this.subType = Utils.trimToEmpty(subType);
        this.isSoftwareDevice = isSoftwareDevice;
        this.streamDescriptors = Utils.coalesce(streamDescriptors, Collections.emptyList());
    }

    public String getSymbolicLink() {
        return symbolicLink;
    }

    public String getDeviceLink() {
        return deviceLink;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getMajorType() {
        return majorType;
    }

    public String getSubType() {
        return subType;
    }

    public boolean isSoftwareDevice() {
        return isSoftwareDevice;
    }

    public boolean isVideoDevice() {
        return streamDescriptors.stream().anyMatch(CaptureManagerStreamDescriptor::isVideoStream);
    }

    public List<CaptureManagerStreamDescriptor> getStreamDescriptors() {
        return streamDescriptors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaptureManagerSource that = (CaptureManagerSource) o;

        if (isSoftwareDevice != that.isSoftwareDevice) {
            return false;
        }
        if (!symbolicLink.equals(that.symbolicLink)) {
            return false;
        }
        if (!deviceLink.equals(that.deviceLink)) {
            return false;
        }
        if (!friendlyName.equals(that.friendlyName)) {
            return false;
        }
        if (!majorType.equals(that.majorType)) {
            return false;
        }
        if (!subType.equals(that.subType)) {
            return false;
        }
        return streamDescriptors.equals(that.streamDescriptors);
    }

    @Override
    public int hashCode() {
        int result = symbolicLink.hashCode();
        result = 31 * result + deviceLink.hashCode();
        result = 31 * result + friendlyName.hashCode();
        result = 31 * result + majorType.hashCode();
        result = 31 * result + subType.hashCode();
        result = 31 * result + (isSoftwareDevice ? 1 : 0);
        result = 31 * result + streamDescriptors.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CaptureManagerSource{" + "symbolicLink=" + symbolicLink + ", deviceLink=" + deviceLink + ", friendlyName=" + friendlyName + ", majorType=" + majorType + ", subType=" + subType + ", isSoftwareDevice=" + isSoftwareDevice + ", streamDescriptors=" + streamDescriptors + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String symbolicLink;
        private String deviceLink;
        private String friendlyName;
        private String majorType;
        private String subType;
        private boolean isSoftwareDevice = false;
        private List<CaptureManagerStreamDescriptor> streamDescriptors;

        private Builder() {
        }

        public String getSymbolicLink() {
            return symbolicLink;
        }

        public String getDeviceLink() {
            return deviceLink;
        }

        public String getFriendlyName() {
            return friendlyName;
        }

        public String getMajorType() {
            return majorType;
        }

        public String getSubType() {
            return subType;
        }

        public boolean isSoftwareDevice() {
            return isSoftwareDevice;
        }

        public List<CaptureManagerStreamDescriptor> getStreamDescriptors() {
            return streamDescriptors;
        }

        public Builder symbolicLink(String symbolicLink) {
            this.symbolicLink = symbolicLink;
            return this;
        }

        public Builder deviceLink(String deviceLink) {
            this.deviceLink = deviceLink;
            return this;
        }

        public Builder friendlyName(String friendlyName) {
            this.friendlyName = friendlyName;
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

        public Builder isSoftwareDevice(boolean isSoftwareDevice) {
            this.isSoftwareDevice = isSoftwareDevice;
            return this;
        }

        public Builder streamDescriptors(List<CaptureManagerStreamDescriptor> streamDescriptors) {
            this.streamDescriptors = streamDescriptors;
            return this;
        }

        public CaptureManagerSource build() {
            return new CaptureManagerSource(symbolicLink, deviceLink, friendlyName, majorType, subType, isSoftwareDevice, streamDescriptors);
        }
    }
}
