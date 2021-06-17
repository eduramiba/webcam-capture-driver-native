package com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks;

import com.github.eduramiba.webcamcapture.utils.Utils;

public class SinkValuePart {

    private final String title;
    private final String value;
    private final String mime;
    private final String description;
    private final int maxPortCount;
    private final String guid;

    public SinkValuePart(String title, String value, String mime, String description, int maxPortCount, String guid) {
        this.title = Utils.trimToEmpty(title);
        this.value = Utils.trimToEmpty(value);
        this.mime = Utils.trimToEmpty(mime);
        this.description = Utils.trimToEmpty(description);
        this.maxPortCount = maxPortCount;
        this.guid = Utils.trimToEmpty(guid);
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public String getMime() {
        return mime;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxPortCount() {
        return maxPortCount;
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public String toString() {
        return "SinkValuePart{" + "title=" + title + ", value=" + value + ", mime=" + mime + ", description=" + description + ", maxPortCount=" + maxPortCount + ", guid=" + guid + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SinkValuePart valuePart = (SinkValuePart) o;

        if (maxPortCount != valuePart.maxPortCount) {
            return false;
        }
        if (!title.equals(valuePart.title)) {
            return false;
        }
        if (!value.equals(valuePart.value)) {
            return false;
        }
        if (!mime.equals(valuePart.mime)) {
            return false;
        }
        if (!description.equals(valuePart.description)) {
            return false;
        }
        return guid.equals(valuePart.guid);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + mime.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + maxPortCount;
        result = 31 * result + guid.hashCode();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String title;
        private String value;
        private String mime;
        private String description;
        private int maxPortCount;
        private String guid;

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder mime(String mime) {
            this.mime = mime;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder maxPortCount(int maxPortCount) {
            this.maxPortCount = maxPortCount;
            return this;
        }

        public Builder guid(String guid) {
            this.guid = guid;
            return this;
        }

        public SinkValuePart build() {
            return new SinkValuePart(title, value, mime, description, maxPortCount, guid);
        }
    }
}
