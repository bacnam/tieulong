package ch.qos.logback.core.rolling.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsynchronousCompressor {
    Compressor compressor;

    public AsynchronousCompressor(Compressor compressor) {
        this.compressor = compressor;
    }

    public Future<?> compressAsynchronously(String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName) {
        ExecutorService executor = Executors.newScheduledThreadPool(1);
        Future<?> future = executor.submit(new CompressionRunnable(this.compressor, nameOfFile2Compress, nameOfCompressedFile, innerEntryName));

        executor.shutdown();
        return future;
    }
}

