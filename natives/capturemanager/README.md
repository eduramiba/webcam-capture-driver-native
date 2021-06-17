# Introduction

This is the code for the proxy DLL that the Java JNI code in this repository uses to communicate with the real CaptureManagerSDK DLL.

I have not coded this, it's just a copy of `CaptureManagerToJavaProxy.zip` downloaded from https://www.codeproject.com/Articles/1017223/CaptureManager-SDK-Capturing-Recording-and-Streami with a very simple modification in method `Java_capturemanager_classes_SampleGrabberCallNative_readData` of file `SampleGrabberCallNative.cpp` to avoid very high memory usage due to copying. This method received an array argument where data was copied. With my modified version it receives a direct Java ByteBuffer instance.

The already-built DLLs can be found in the natives directory of this same repository. The code is here for reference and bulding a custom DLL.

Note: Only `SampleGrabberCallNative` mode is used in the Java driver. I was not able to make Callback mode work correctly. Fortunately reading directly from the buffer into JavaFX images is possible, avoiding the conversion to a `BufferedImage` every frame.