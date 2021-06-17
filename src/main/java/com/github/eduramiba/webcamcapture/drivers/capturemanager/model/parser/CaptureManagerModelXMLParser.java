package com.github.eduramiba.webcamcapture.drivers.capturemanager.model.parser;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerMediaType;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerSource;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerStreamDescriptor;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.CaptureManagerSinkFactory;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.SinkValuePart;
import com.github.eduramiba.webcamcapture.utils.Utils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaptureManagerModelXMLParser {

    private static final Logger LOG = LoggerFactory.getLogger(CaptureManagerModelXMLParser.class);

    public List<CaptureManagerSource> parseVideoSources(final String xml) throws XMLStreamException {
        final List<CaptureManagerSource> list = new ArrayList<>();

        final AsyncXMLInputFactory inputF = new InputFactoryImpl(); // sub-class of XMLStreamReader2
        final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser = inputF.createAsyncFor(xml.getBytes(StandardCharsets.UTF_8));

        try {
            while (parser.hasNext()) {
                final int type = parser.next();

                if (isPrematureEnd(parser, type, "root")) {
                    break;
                }

                if (type == AsyncXMLStreamReader.START_ELEMENT && parser.hasName()) {
                    final String name = parser.getLocalName();

                    if ("Source".equalsIgnoreCase(name)) {
                        parseSource(parser).ifPresent(list::add);
                    }
                }
            }
        } finally {
            parser.close();
        }

        return list.stream()
                .filter(d -> !d.isSoftwareDevice())
                .filter(CaptureManagerSource::isVideoDevice)
                .collect(Collectors.toList());
    }

    private Optional<CaptureManagerSource> parseSource(final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser) throws XMLStreamException {
        LOG.debug("Parse source - start");
        final CaptureManagerSource.Builder sourceBuilder = CaptureManagerSource.builder();
        final List<CaptureManagerStreamDescriptor> streams = new ArrayList<>();

        int nestingCount = 0;
        while (parser.hasNext()) {
            final int type = parser.next();

            if (isPrematureEnd(parser, type, "source")) {
                return Optional.empty();
            }

            if (type == AsyncXMLStreamReader.END_ELEMENT) {
                if (nestingCount <= 0) {
                    sourceBuilder.streamDescriptors(streams);

                    if (Utils.containsIgnoreCase(sourceBuilder.getFriendlyName(), "Virtual Camera")
                            || Utils.containsIgnoreCase(sourceBuilder.getSymbolicLink(), "@device:sw:")) {
                        sourceBuilder.isSoftwareDevice(true);//OBS Virtual Camera presents itself as hardware device
                    }

                    LOG.debug("Parse source - ok");
                    return Optional.of(sourceBuilder.build());
                }

                nestingCount--;
            }

            if (type == AsyncXMLStreamReader.START_ELEMENT) {
                nestingCount++;

                if (parser.hasName()) {
                    final String name = parser.getLocalName();

                    if ("Source.Attributes".equalsIgnoreCase(name)) {
                        parseSourceAttributes(parser, sourceBuilder);
                        nestingCount--;
                    } else if ("StreamDescriptor".equalsIgnoreCase(name)) {
                        parseStreamDescriptor(parser).ifPresent(streams::add);
                        nestingCount--;
                    }
                }
            }
        }

        LOG.warn("Parse source - no data");
        return Optional.empty();
    }

    private void parseSourceAttributes(final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser, final CaptureManagerSource.Builder sourceBuilder) throws XMLStreamException {
        int nestingCount = 0;
        String attributeName = "?";

        while (parser.hasNext()) {
            final int type = parser.next();

            if (isPrematureEnd(parser, type, "source attributes")) {
                return;
            }

            if (type == AsyncXMLStreamReader.END_ELEMENT) {
                if (nestingCount <= 0) {
                    return;
                }

                nestingCount--;
            }

            if (type == AsyncXMLStreamReader.START_ELEMENT) {
                nestingCount++;

                if (parser.hasName()) {
                    final String name = parser.getLocalName();

                    if ("Attribute".equalsIgnoreCase(name)) {
                        attributeName = parser.getAttributeValue(null, "Name");
                    } else if ("SingleValue".equalsIgnoreCase(name)) {
                        final String value = parser.getAttributeValue(null, "Value");
                        switch (attributeName) {
                            case "MF_DEVSOURCE_ATTRIBUTE_SOURCE_TYPE_VIDCAP_SYMBOLIC_LINK":
                                sourceBuilder.symbolicLink(value);
                                break;
                            case "CM_DEVICE_LINK":
                                sourceBuilder.deviceLink(value);
                                break;
                            case "MF_DEVSOURCE_ATTRIBUTE_FRIENDLY_NAME":
                                sourceBuilder.friendlyName(value);
                                break;
                            case "MF_DEVSOURCE_ATTRIBUTE_SOURCE_TYPE_VIDCAP_HW_SOURCE":
                                sourceBuilder.isSoftwareDevice(!Utils.containsIgnoreCase(value, "hardware"));
                                break;
                        }
                    } else if ("ValuePart".equalsIgnoreCase(name)) {
                        final String title = parser.getAttributeValue(null, "Title");
                        final String value = parser.getAttributeValue(null, "Value");

                        switch (title) {
                            case "MajorType":
                                sourceBuilder.majorType(value);
                                break;
                            case "SubType":
                                sourceBuilder.subType(value);
                                break;
                        }
                    }
                }
            }
        }
    }

    private Optional<CaptureManagerStreamDescriptor> parseStreamDescriptor(final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser) throws XMLStreamException {
        LOG.debug("Parse stream descriptor - start");
        final CaptureManagerStreamDescriptor.Builder streamBuilder = CaptureManagerStreamDescriptor.builder();
        final List<CaptureManagerMediaType> mediaTypes = new ArrayList<>();

        final String majorType = parser.getAttributeValue(null, "MajorType");
        final String majorTypeGUID = parser.getAttributeValue(null, "MajorTypeGUID");

        streamBuilder.majorType(majorType);
        streamBuilder.majorTypeGUID(majorTypeGUID);

        int nestingCount = 0;
        while (parser.hasNext()) {
            final int type = parser.next();

            if (isPrematureEnd(parser, type, "stream descriptor")) {
                return Optional.empty();
            }

            if (type == AsyncXMLStreamReader.END_ELEMENT) {
                if (nestingCount <= 0) {
                    streamBuilder.mediaTypes(mediaTypes);
                    LOG.debug("Parse stream descriptor - ok");
                    return Optional.of(streamBuilder.build());
                }

                nestingCount--;
            }

            if (type == AsyncXMLStreamReader.START_ELEMENT) {
                nestingCount++;

                if (parser.hasName()) {
                    final String name = parser.getLocalName();

                    if ("MediaType".equalsIgnoreCase(name)) {
                        parseMediaType(parser).ifPresent(mediaTypes::add);
                        nestingCount--;
                    }
                }
            }
        }

        LOG.warn("Parse stream descriptor - no data");
        return Optional.empty();
    }

    private Optional<CaptureManagerMediaType> parseMediaType(final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser) throws XMLStreamException {
        LOG.debug("Parse media type - start");
        final CaptureManagerMediaType.Builder mediaTypeBuilder = CaptureManagerMediaType.builder();

        String mediaTypeItemName = "?";
        int nestingCount = 0;
        while (parser.hasNext()) {
            final int type = parser.next();

            if (isPrematureEnd(parser, type, "media type")) {
                return Optional.empty();
            }

            if (type == AsyncXMLStreamReader.END_ELEMENT) {
                if (nestingCount <= 0) {
                    final CaptureManagerMediaType mediaType = mediaTypeBuilder.build();
                    LOG.debug("Parse media type - correct - subtype = {}", mediaType.getSubType());
                    return Optional.of(mediaType);
                }

                nestingCount--;
            }

            if (type == AsyncXMLStreamReader.START_ELEMENT) {
                nestingCount++;

                if (parser.hasName()) {
                    final String name = parser.getLocalName();

                    if ("MediaTypeItem".equalsIgnoreCase(name)) {
                        mediaTypeItemName = parser.getAttributeValue(null, "Name");
                    } else if ("SingleValue".equalsIgnoreCase(name)) {
                        final String value = parser.getAttributeValue(null, "Value");
                        switch (mediaTypeItemName) {
                            case "MF_MT_MAJOR_TYPE":
                                mediaTypeBuilder.majorType(value);
                                break;
                            case "MF_MT_SUBTYPE":
                                mediaTypeBuilder.subType(value);
                                break;
                        }
                    } else if ("ValuePart".equalsIgnoreCase(name)) {
                        final String title = parser.getAttributeValue(null, "Title");
                        final String value = parser.getAttributeValue(null, "Value");

                        if ("MF_MT_FRAME_SIZE".equalsIgnoreCase(mediaTypeItemName)) {
                            switch (title) {
                                case "Width":
                                    mediaTypeBuilder.width(Integer.parseInt(value));
                                    break;
                                case "Height":
                                    mediaTypeBuilder.height(Integer.parseInt(value));
                                    break;
                            }
                        }
                    }
                }
            }
        }

        LOG.warn("Parse media type - no data");
        return Optional.empty();
    }

    public List<CaptureManagerSinkFactory> parseSinkFactories(final String xml) throws XMLStreamException {
        final List<CaptureManagerSinkFactory> list = new ArrayList<>();

        final AsyncXMLInputFactory inputF = new InputFactoryImpl(); // sub-class of XMLStreamReader2
        final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser = inputF.createAsyncFor(xml.getBytes(StandardCharsets.UTF_8));

        try {
            while (parser.hasNext()) {
                final int type = parser.next();

                if (isPrematureEnd(parser, type, "root")) {
                    break;
                }

                if (type == AsyncXMLStreamReader.START_ELEMENT && parser.hasName()) {
                    final String name = parser.getLocalName();

                    if ("SinkFactory".equalsIgnoreCase(name)) {
                        parseSinkFactory(parser).ifPresent(list::add);
                    }
                }
            }
        } finally {
            parser.close();
        }

        return list;
    }

    private Optional<CaptureManagerSinkFactory> parseSinkFactory(final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser) throws XMLStreamException {
        LOG.debug("Parse sink factory - start");
        final CaptureManagerSinkFactory.Builder sinkFactoryBuilder = CaptureManagerSinkFactory.builder();
        final List<SinkValuePart> parts = new ArrayList<>();

        sinkFactoryBuilder.name(
                parser.getAttributeValue(null, "Name")
        );
        sinkFactoryBuilder.guid(
                parser.getAttributeValue(null, "GUID")
        );
        sinkFactoryBuilder.title(
                parser.getAttributeValue(null, "Title")
        );

        int nestingCount = 0;
        while (parser.hasNext()) {
            final int type = parser.next();

            if (isPrematureEnd(parser, type, "sink factory")) {
                return Optional.empty();
            }

            if (type == AsyncXMLStreamReader.END_ELEMENT) {
                if (nestingCount <= 0) {
                    sinkFactoryBuilder.valueParts(parts);
                    LOG.debug("Parse sink factory - ok");
                    return Optional.of(sinkFactoryBuilder.build());
                }

                nestingCount--;
            }

            if (type == AsyncXMLStreamReader.START_ELEMENT) {
                nestingCount++;

                if (parser.hasName()) {
                    final String name = parser.getLocalName();

                    if ("ValuePart".equalsIgnoreCase(name)) {
                        parseSinkValuePart(parser).ifPresent(parts::add);
                        nestingCount--;
                    }
                }
            }
        }

        LOG.warn("Parse sink factory - no data");
        return Optional.empty();
    }

    private Optional<SinkValuePart> parseSinkValuePart(final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser) throws XMLStreamException {
        LOG.debug("Parse sink value part - start");
        final SinkValuePart.Builder partBuilder = SinkValuePart.builder();

        partBuilder.title(
                parser.getAttributeValue(null, "Title")
        );
        partBuilder.value(
                parser.getAttributeValue(null, "Value")
        );
        partBuilder.mime(
                parser.getAttributeValue(null, "MIME")
        );
        partBuilder.description(
                parser.getAttributeValue(null, "Description")
        );
        partBuilder.maxPortCount(
                Integer.parseInt(parser.getAttributeValue(null, "MaxPortCount"))
        );
        partBuilder.guid(
                parser.getAttributeValue(null, "GUID")
        );

        int nestingCount = 0;
        while (parser.hasNext()) {
            final int type = parser.next();

            if (isPrematureEnd(parser, type, "sink value part")) {
                return Optional.empty();
            }

            if (type == AsyncXMLStreamReader.END_ELEMENT) {
                if (nestingCount <= 0) {
                    LOG.debug("Parse sink value part - ok");
                    return Optional.of(partBuilder.build());
                }

                nestingCount--;
            }

            if (type == AsyncXMLStreamReader.START_ELEMENT) {
                nestingCount++;
            }
        }

        LOG.warn("Parse sink value part - no data");
        return Optional.empty();
    }

    private boolean isPrematureEnd(final AsyncXMLStreamReader<AsyncByteArrayFeeder> parser, final int type, final String where) {
        if (type == AsyncXMLStreamReader.EVENT_INCOMPLETE) {
            parser.getInputFeeder().endOfInput();
        }

        final boolean result = type == AsyncXMLStreamReader.EVENT_INCOMPLETE || type == AsyncXMLStreamReader.END_DOCUMENT;

        if (result) {
            LOG.debug("Premature end of XML stream at {}. Event type = {}", where, type);
        }

        return result;
    }
}
