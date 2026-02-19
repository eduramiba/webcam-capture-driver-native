# Introduction

This is a native driver for [Webcam Capture](https://github.com/sarxos/webcam-capture) that is reliable, has very good performance, fast startup time and is able to correctly list the detailed capabilities of video devices such as resolutions and device IDs.

Currently it works on Windows, Linux and MacOS.

For Linux, it uses the `NokhwaDriver`, based on [nokhwa](https://github.com/l1npengtul/nokhwa) through a [simple and small C binding](https://github.com/eduramiba/lib-cnokhwa), which uses V4L2 in Linux.
For Windows, `NativeDriver` uses `NokhwaDriver` by default, and Nokhwa uses Microsoft Media Foundation on Windows. You can optionally use a DirectShow backend (`CdshowDriver`) through `cdshow.dll` for retro compatibility with a [different small C binding](https://github.com/eduramiba/lib-cdshow).
For MacOS, it uses `AVFDriver`, based on a [custom library](https://github.com/eduramiba/libvideocapture-avfoundation) that uses [AVFoundation](https://developer.apple.com/av-foundation/). When Nokhwa is stable in MacOS, this library will be updated to use `NokhwaDriver` for every OS.

# How to use

1. Add `io.github.eduramiba:webcam-capture-driver-native:1.2.3` dependency to your application.
2. Use the driver with `Webcam.setDriver(new NativeDriver())`
3. List the devices with `Webcam.getWebcams()` as normal and use the library in your preferred way. In JavaFX it's recommended to do it as in the example below.

## Windows backend selection

By default, Windows uses `NokhwaDriver`.

To use DirectShow (`cdshow.dll`), use one of these options:

1. Java code:

```java
Webcam.setDriver(new NativeDriver(NativeDriver.WindowsBackend.DIRECTSHOW));
```

2. System property:

```bash
-Dwebcamcapture.windows.backend=directshow
```

Accepted property values are `nokhwa`, `directshow`, and `cdshow`.

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

# Notes

The native dynamic libraries for Mac are on `src/main/resources` and loaded by JNA from inside the JAR.
Note that if you want to distribute a Mac app you will need to properly codesign the dylib files with entitlements, have an Info.plist, notarization...
