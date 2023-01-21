package blue.lhf.valkea;

import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.locks.*;

public abstract class FileWatcher extends Thread implements Closeable {
    private final MessageDigest message;
    private final Duration period;
    private final Path path;

    private byte[] lastDigest;
    private boolean closed = false;

    private final int bufferSize;

    protected FileWatcher(final Path path) {
        this(path, Duration.ofSeconds(1));
    }

    protected FileWatcher(final Path path, final Duration period) {
        this(path, period, 131072);
    }

    protected FileWatcher(final Path path, final Duration period, int bufferSize) {
        this.path = path;
        this.period = period;
        try {
            this.message = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("JVM does not support MD5", e);
        }
        this.bufferSize = bufferSize;
        this.message.reset();
    }

    public abstract void onChange();

    @Override
    public void run() {
        while (!closed) {
            final byte[] current = computeDigest();
            if (!Arrays.equals(lastDigest, current)) {
                onChange();
            }

            lastDigest = current;
            LockSupport.parkNanos(period.toNanos());
        }
    }

    public byte @Nullable [] computeDigest() {
        if (!Files.exists(path) || !Files.isRegularFile(path) || !Files.isReadable(path)) return null;
        try (final InputStream stream = Files.newInputStream(path)) {
            final byte[] buffer = new byte[bufferSize];
            for (int read; (read = stream.read(buffer)) != -1;) {
                message.update(buffer, 0, read);
            }
        } catch (IOException ignored) {
            return null;
        }

        return message.digest();
    }

    public void close() {
        this.closed = true;
    }
}
