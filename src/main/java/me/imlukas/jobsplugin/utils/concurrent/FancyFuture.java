package me.imlukas.jobsplugin.utils.concurrent;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FancyFuture<T> extends CompletableFuture<T> {

    public FancyFuture() {
        // handleExceptions();
    }

    private FancyFuture<T> handleExceptions() {
        this.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        return this;
    }

    public static <T> FancyFuture<T> completedFuture(T value) {
        FancyFuture<T> future = new FancyFuture<>();
        future.complete(value);
        return future;
    }

    public static <T> FancyFuture<T> failedFuture(Throwable throwable) {
        FancyFuture<T> future = new FancyFuture<>();
        future.completeExceptionally(throwable);
        return future;
    }

    public static <T> FancyFuture<T> supplyAsync(Supplier<T> supplier) {
        FancyFuture<T> future = new FancyFuture<>();
        CompletableFuture.supplyAsync(supplier).thenAccept(future::complete);
        return future.handleExceptions();
    }

    public static FancyFuture<Void> runAsync(Runnable runnable) {
        FancyFuture<Void> future = new FancyFuture<>();
        CompletableFuture.runAsync(runnable).thenAccept(v -> future.complete(null));
        return future.handleExceptions();
    }

    public static <T> FancyFuture<T> completed(T value) {
        FancyFuture<T> future = new FancyFuture<>();
        future.complete(value);
        return future.handleExceptions();
    }

    public static <T> FancyFuture<T> failed(Throwable throwable) {
        FancyFuture<T> future = new FancyFuture<>();
        future.completeExceptionally(throwable);
        return future.handleExceptions();
    }

    public static <T> FancyFuture<T> cancelled() {
        FancyFuture<T> future = new FancyFuture<>();
        future.cancel(true);
        return future.handleExceptions();
    }

    // exceptionally


    @Override
    public FancyFuture<T> exceptionally(Function<Throwable, ? extends T> fn) {
        return (FancyFuture<T>) super.exceptionally(fn);
    }

    public FancyFuture<T> exceptionallyAsync(Function<Throwable, ? extends T> function) {
        return exceptionallyAsync(function, null);
    }

    public FancyFuture<T> exceptionallyAsync(Function<Throwable, ? extends T> function, Executor executor) {
        return new FancyFuture<T>().handleExceptions().completeAsync(() -> {
            try {
                return get();
            } catch (Throwable throwable) {
                return function.apply(throwable);
            }
        }, executor);
    }

    @Override
    public FancyFuture<T> completeAsync(Supplier<? extends T> supplier, Executor executor) {
        CompletableFuture<T> future = super.completeAsync(supplier, executor);
        return new FancyFuture<T>().handleExceptions().completeAsync(() -> {
            try {
                return future.get();
            } catch (Throwable throwable) {
                return null;
            }
        }, executor);
    }

    @Override
    public <U> FancyFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        handleExceptions();
        return (FancyFuture<U>) super.thenApply(fn);
    }

    @Override
    public FancyFuture<Void> thenAccept(Consumer<? super T> action) {
        handleExceptions();
        return (FancyFuture<Void>) super.thenAccept(action);
    }

    @Override
    public <U> FancyFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn) {
        handleExceptions();
        return (FancyFuture<U>) super.thenCompose(fn);
    }

    @Override
    public FancyFuture<Void> thenRun(Runnable action) {
        return (FancyFuture<Void>) super.thenRun(action);
    }

    public static <T> FancyFuture<T> empty() {
        return new FancyFuture<>();
    }

    @Override
    public <U> FancyFuture<U> newIncompleteFuture() {
        return new FancyFuture<>();
    }

    public <U> FancyFuture<U> thenSupply(Supplier<U> supplier) {
        return thenApply((ignored) -> supplier.get());
    }

    public <U> FancyFuture<U> thenSupply(U value) {
        return thenApply((ignored) -> value);
    }

    public <U> FancyFuture<U> thenSupplyAsync(Supplier<U> supplier) {
        return (FancyFuture<U>) thenApplyAsync((ignored) -> supplier.get());
    }

    public <U> FancyFuture<U> thenSupplyAsync(Supplier<U> supplier, Executor executor) {
        return (FancyFuture<U>) thenApplyAsync((ignored) -> supplier.get(), executor);
    }

    public static <T> FancyFuture<Void> all(FancyFuture<?>... futures) {
        FancyFuture<T> future = new FancyFuture<>();
        CompletableFuture.allOf(futures).thenAccept(ignored -> future.complete(null));
        return future.handleExceptions().thenRun(() -> {});
    }

    public static <T> FancyFuture<Void> all(Collection<FancyFuture<?>> futures) {
        return all(futures.toArray(new FancyFuture[0]));
    }

    public static <T> FancyFuture<T> exceptionally(FancyFuture<?>... futures) {
        FancyFuture<T> future = new FancyFuture<>();
        FancyFuture.anyOf(futures).exceptionally(throwable -> {
            future.completeExceptionally(throwable);
            return null;
        });
        return future.handleExceptions();
    }



}
