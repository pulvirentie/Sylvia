package com.yoox.samplerxjavaapp.common;

import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseRxViewModel extends ViewModel {

    private CompositeDisposable disposables = new CompositeDisposable();

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
        super.onCleared();
    }
}
