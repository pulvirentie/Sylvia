package com.yoox.samplerxjavaapp.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yoox.net.Items;
import com.yoox.net.ItemsBuilder;
import com.yoox.net.Request;
import com.yoox.net.models.outbound.Item;
import com.yoox.samplerxjavaapp.common.BaseRxViewModel;
import com.yoox.sylvia.rx.RXKt;
import io.reactivex.disposables.Disposable;

public class DetailViewModel extends BaseRxViewModel {

    private final Items items = new ItemsBuilder("IT").build();

    private final MutableLiveData<Item> itemLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<Item> getItem() {
        return itemLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }


    void loadItem(String id) {

        Request<Item> request = items.get(id);

        Disposable disposable = RXKt.asSingle(request).subscribe(
                DetailViewModel.this.itemLiveData::postValue,
                throwable -> DetailViewModel.this.errorLiveData.postValue(throwable.toString()));

        addDisposable(disposable);
    }

}
