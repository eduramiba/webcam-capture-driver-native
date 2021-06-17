# Introduction

This is a native driver for [Webcam Capture](https://github.com/sarxos/webcam-capture) that is reliable, has very good performance, fast startup time and is able to correctly list the detailed capabilities of video devices such as resolutions and device IDs.

Currently it works on Windows only, with the `CaptureManagerDriver`, based on [CaptureManager-SDK](https://www.codeproject.com/Articles/1017223/CaptureManager-SDK-Capturing-Recording-and-Streami), which uses the MediaFoundation Windows API.

# How to use

1. Download this repository and run `mvn install`
2. Add `com.github.eduramiba:webcam-capture-driver-native:1.0.0-SNAPSHOT` dependency to your application.
3. Copy the DLLs of the `natives` folder for your system into the java library path.
4. Use the driver with `Webcam.setDriver(new CaptureManagerDriver())`
5. List the devices with `Webcam.getWebcams()` as normal and use the library in your preferred way. In JavaFX it's recommended to do it as in the example below.

# Simple example with JavaFX

```java
import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceWithBufferOperations;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.CaptureManagerDriver;
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

public class TestCaptureManagerDriver extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(TestCaptureManagerDriver.class);
    
    public static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(4);

    public static void main(String[] args) {
        Webcam.setDriver(new CaptureManagerDriver());

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ImageView imageView = new ImageView();
        HBox root = new HBox();
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
                    });

                    camera.open();
                    if (device instanceof WebcamDeviceWithBufferOperations) {
                        EXECUTOR.scheduleAtFixedRate(() -> {
                            ((WebcamDeviceWithBufferOperations) device).updateFXIMage(fxImage);
                        }, 0, 16, TimeUnit.MILLISECONDS);
                    }
                });

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the scene to the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("Displaying an Image");
        // Display the Stage
        stage.show();
    }
}
```

# Future work

* Publish this as a maven central artifact. At the moment you will need to build it yourself.
* Implement MacOS and Linux native driver that uses LibUVC.

# Notes

The source code in `natives` folder and `capturemanager` java package has been copied from [CaptureManager-SDK](https://www.codeproject.com/Articles/1017223/CaptureManager-SDK-Capturing-Recording-and-Streami) and slightly improved for this driver.