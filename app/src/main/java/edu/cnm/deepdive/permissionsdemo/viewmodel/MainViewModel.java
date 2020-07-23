package edu.cnm.deepdive.permissionsdemo.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

  private final MutableLiveData<Boolean> permissionsChecked;

  public MainViewModel(@NonNull Application application) {
    super(application);
    permissionsChecked = new MutableLiveData<>(false);
  }

  public LiveData<Boolean> getPermissionsChecked() {
    return permissionsChecked;
  }

  public void setPermissionsChecked(boolean checked) {
    permissionsChecked.setValue(checked);
  }

}
