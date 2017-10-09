package org.study.chat;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallbackFutureTask  extends FutureTask{
    SuccessCallback success;
    public CallbackFutureTask(Callable callable, SuccessCallback success) {
        super(callable);
        // objects?
        this.success = Objects.requireNonNull(success);
    }

    // when function end, isDone Call !
    @Override
    protected void done() {
        success.onSuccess();
    }
}
