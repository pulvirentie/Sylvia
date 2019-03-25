package com.yoox.samplejavaapp.visual;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yoox.net.Items;
import com.yoox.net.ItemsBuilder;
import com.yoox.net.Request;
import com.yoox.net.models.outbound.Gender;
import com.yoox.net.models.outbound.VisualSearch;
import com.yoox.samplejavaapp.common.ImageEncoder;
import com.yoox.sylvia.androidcallback.AndroidExecutor;

import java.util.Collections;

public class VisualViewModel extends AndroidViewModel {


    public VisualViewModel(@NonNull Application application) {
        super(application);
    }


    private final Items items = new ItemsBuilder("IT").build();
    private final Gender women = Gender.Women;

    private final MutableLiveData<VisualState> stateLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();


    public LiveData<VisualState> getState() {
        return stateLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }


    void loadItems(String remoteImageUri, Uri localImageUri) {

        Request<VisualSearch> request;

        if (remoteImageUri != null) {
            request = items.visualSearchByUrl(women, remoteImageUri);
        } else {
            ContentResolver contentResolver = getApplication().getContentResolver();
            String encoded = ImageEncoder.encode(localImageUri, contentResolver);
            request = items.visualSearchByBase64(women, encoded);
        }

        executeRequest(request);

    }

    private void executeRequest(Request<VisualSearch> request) {
        VisualState loadingState = new VisualState(true, Collections.emptyList());
        stateLiveData.setValue(loadingState);
        AndroidExecutor.execute(request,
                this::onResult,
                throwable -> VisualViewModel.this.errorLiveData.setValue(throwable.toString()));
    }


    private void onResult(VisualSearch result) {
        VisualState state = new VisualState(false, result.getItems());
        VisualViewModel.this.stateLiveData.setValue(state);
    }
}
