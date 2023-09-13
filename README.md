# Introduction

This is a native driver for [Webcam Capture](https://github.com/sarxos/webcam-capture) that is reliable, has very good performance, fast startup time and is able to correctly list the detailed capabilities of video devices such as resolutions and device IDs.

Currently it works on Windows and Mac.

For Windows, it uses the `CaptureManagerDriver`, based on [CaptureManager-SDK](https://www.codeproject.com/Articles/1017223/CaptureManager-SDK-Capturing-Recording-and-Streami), which uses the MediaFoundation Windows API.
For Mac, it uses `AVFDriver`, based on a [custom library](https://github.com/eduramiba/libvideocapture-avfoundation) that uses [AVFoundation](https://developer.apple.com/av-foundation/).

# How to use

1. Add `io.github.eduramiba:webcam-capture-driver-native:1.0.0` dependency to your application.
2. Use the driver with `Webcam.setDriver(new NativeDriver())`
3. List the devices with `Webcam.getWebcams()` as normal and use the library in your preferred way. In JavaFX it's recommended to do it as in the example below.

# Simple example with JavaFX

```java
import com.github.eduramiba.webcamcapture.drivers.NativeDriver;
import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceWithBufferOperations;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDriver extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(TestDriver.class);

    public static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(4);

    public static void main(String[] args) {
        Webcam.setDriver(new NativeDriver());

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final ImageView imageView = new ImageView();
        final HBox root = new HBox();
        root.getChildren().add(imageView);

        Webcam.getWebcams().stream()
                .findFirst()
                .ifPresent((final Webcam camera) -> {
                    final WebcamDevice device = camera.getDevice();
                    LOG.info("Found camera: {}, device = {}", camera, device);

                    final int width = device.getResolution().width;
                    final int height = device.getResolution().height;
                    final WritableImage fxImage = new WritableImage(width, height);
                    Platform.runLater(() -> {
                        imageView.setImage(fxImage);
                        stage.setWidth(width);
                        stage.setHeight(height);
                        stage.centerOnScreen();
                    });

                    camera.getLock().disable();
                    camera.open();
                    if (device instanceof WebcamDeviceWithBufferOperations) {
                        final WebcamDeviceWithBufferOperations dev = ((WebcamDeviceWithBufferOperations) device);
                        EXECUTOR.scheduleAtFixedRate(new Runnable() {
                            private long lastFrameTimestamp = -1;

                            @Override
                            public void run() {
                                if (dev.updateFXIMage(fxImage, lastFrameTimestamp)) {
                                    lastFrameTimestamp = dev.getLastFrameTimestamp();
                                }

                            }
                        }, 0, 16, TimeUnit.MILLISECONDS);
                    }
                });

        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        // Create the Scene
        final Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Webcam example");
        stage.show();
    }
}
```

# Future work

* Publish this as a maven central artifact. At the moment you will need to build it yourself.
* Implement Linux driver

# Notes

The source code in `natives` folder and `capturemanager` java package has been copied from [CaptureManager-SDK](https://www.codeproject.com/Articles/1017223/CaptureManager-SDK-Capturing-Recording-and-Streami) and slightly improved for this driver. This code is not idiomatic java and needs improvement.
The DLLs for Windows can just be copied along with your program.

The native dynamic libraries for Mac are on `src/main/resources` and loaded by JNA from inside the JAR.
Note that if you want to distribute a Mac app you will need to properly codesign the dylib files with entitlements, have an Info.plist, notarization...
