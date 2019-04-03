package com.yoox.samplerxjavaapp.common;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseRxAndroidViewModel extends AndroidViewModel {

    private CompositeDisposable disposables = new CompositeDisposable();

    public BaseRxAndroidViewModel(@NonNull Application application) {
        super(application);
    }

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
