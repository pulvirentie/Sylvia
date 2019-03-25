package com.yoox.samplejavaapp.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.yoox.net.Items;
import com.yoox.net.ItemsBuilder;
import com.yoox.net.Request;
import com.yoox.net.models.outbound.Item;
import com.yoox.sylvia.androidcallback.AndroidExecutor;
import com.yoox.sylvia.androidcallback.Callback;

public class DetailViewModel extends ViewModel {

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


        AndroidExecutor.execute(request, new Callback<Item>() {
            @Override
            public void onComplete(Item result) {
                DetailViewModel.this.itemLiveData.setValue(result);
            }

            @Override
            public void onError(@NonNull Throwable t) {
                DetailViewModel.this.errorLiveData.setValue(t.toString());
            }
        });

    }

}
