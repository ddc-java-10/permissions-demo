/*
 *  Copyright 2020 Deep Dive Coding/CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.permissionsdemo.controller;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.permissionsdemo.R;
import edu.cnm.deepdive.permissionsdemo.service.PermissionsService;
import edu.cnm.deepdive.permissionsdemo.viewmodel.MainViewModel;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

  private static final int PERMISSIONS_REQUEST_CODE = 999;

  private final PermissionsService permissionsService = PermissionsService.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    observePermissions();
    checkPermissionsOnce();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == PERMISSIONS_REQUEST_CODE) {
      permissionsService.updatePermissions(permissions, grantResults);
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private void observePermissions() {
    permissionsService.getPermissions().observe(this, (perms) -> {
      // Display the permissions in a list view.
      ListView permissions = findViewById(R.id.permissions);
      ArrayAdapter<String> adapter =
          new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new LinkedList<>(perms));
      permissions.setAdapter(adapter);
    });
  }

  private void checkPermissionsOnce() {
    MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    // Observe a flag indicating whether permissions have been checked previously.
    viewModel.getPermissionsChecked().observe(this, (checked) -> {
      // If permissions have not yet been checked, do so; then set the flag accordingly.
      if (!checked) {
        viewModel.setPermissionsChecked(true);
        permissionsService.checkPermissions(this, PERMISSIONS_REQUEST_CODE);
      }
    });
  }

}
