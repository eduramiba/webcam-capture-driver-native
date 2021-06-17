package com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks;

import com.github.eduramiba.webcamcapture.utils.Utils;
import java.util.Collections;
import java.util.List;

public class CaptureManagerSinkFactory {

    private final String name;
    private final String title;
    private final String guid;
    private final List<SinkValuePart> valueParts;

    public CaptureManagerSinkFactory(String name, String title, String guid, List<SinkValuePart> valueParts) {
        this.name = Utils.trimToEmpty(name);
        this.title = Utils.trimToEmpty(title);
        this.guid = Utils.trimToEmpty(guid);
        this.valueParts = Utils.coalesce(valueParts, Collections.emptyList());
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getGuid() {
        return guid;
    }

    public List<SinkValuePart> getValueParts() {
        return valueParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaptureManagerSinkFactory that = (CaptureManagerSinkFactory) o;

        if (!name.equals(that.name)) {
            return false;
        }
        if (!title.equals(that.title)) {
            return false;
        }
        if (!guid.equals(that.guid)) {
            return false;
        }
        return valueParts.equals(that.valueParts);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + guid.hashCode();
        result = 31 * result + valueParts.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CaptureManagerSinkFactory{" + "name=" + name + ", title=" + title + ", guid=" + guid + ", valueParts=" + valueParts + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private String title;
        private String guid;
        private List<SinkValuePart> valueParts;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder guid(String guid) {
            this.guid = guid;
            return this;
        }

        public Builder valueParts(List<SinkValuePart> valueParts) {
            this.valueParts = valueParts;
            return this;
        }

        public CaptureManagerSinkFactory build() {
            return new CaptureManagerSinkFactory(name, title, guid, valueParts);
        }
    }
}
