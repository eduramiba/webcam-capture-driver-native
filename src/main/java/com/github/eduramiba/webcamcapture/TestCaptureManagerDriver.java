package com.github.eduramiba.webcamcapture;

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
