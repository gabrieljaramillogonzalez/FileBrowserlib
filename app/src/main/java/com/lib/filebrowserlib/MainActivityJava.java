package com.lib.filebrowserlib;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gabriel.jg.library.ui.picker.ImagePickerAlbumsActivity;
import com.lib.filebrowserlib.utils.CommonUtils;
import com.lib.filebrowserlibrary.data.enums.TypeFileBrowser;
import com.lib.filebrowserlibrary.data.model.FileBrowserBundle;
import com.lib.filebrowserlibrary.ui.browser.MainBrowserActivity;

public class MainActivityJava extends AppCompatActivity implements View.OnClickListener {

    private Button boton;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);
        boton = findViewById(R.id.boton);
        boton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (data!=null&&data.getExtras()!=null)
                data.getExtras().getString("FilePathKey");
        }
    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        FileBrowserBundle fileBrowserBundle = new FileBrowserBundle();
        fileBrowserBundle.setIsSingle(true);
        fileBrowserBundle.setTypeFile(TypeFileBrowser.IMAGE);
        bundle.putString("fileBrowser", CommonUtils.toJson(fileBrowserBundle));
        Intent intent = new Intent(this , MainBrowserActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }
}