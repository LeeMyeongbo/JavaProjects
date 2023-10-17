package com.player.mediaplayer;

import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class JavaFXMockMaker implements MockMaker {

    private final MockMaker wrapped = new InlineByteBuddyMockMaker();
    private boolean jfxIsSetup;

    private void doOnJavaFXThread(Runnable pRun) throws RuntimeException {
        if (!jfxIsSetup) {
            setupJavaFX();
            jfxIsSetup = true;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            pRun.run();
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setupJavaFX() throws RuntimeException {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        AtomicReference<T> result = new AtomicReference<>();
        Runnable run = () -> result.set(wrapped.createMock(settings, handler));
        doOnJavaFXThread(run);
        return result.get();
    }

    @Override
    public <T> StaticMockControl<T> createStaticMock(Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        return wrapped.createStaticMock(type, settings, handler);
    }

    @Override
    public MockHandler getHandler(Object mock) {
        AtomicReference<MockHandler> result = new AtomicReference<>();
        Runnable run = () -> result.set(wrapped.getHandler(mock));
        doOnJavaFXThread(run);
        return result.get();
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        Runnable run = () -> wrapped.resetMock(mock, newHandler, settings);
        doOnJavaFXThread(run);
    }

    @Override
    public TypeMockability isTypeMockable(Class<?> aClass) {
        return wrapped.isTypeMockable(aClass);
    }
}
