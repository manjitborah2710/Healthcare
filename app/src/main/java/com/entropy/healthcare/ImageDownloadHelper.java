package com.entropy.healthcare;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ImageDownloadHelper extends AsyncTask<String,Void,RequestCreator>{

    @Override
    protected RequestCreator doInBackground(String... strings) {
        RequestCreator rc=Picasso.get().load(strings[0]).resize(250,250);
        return rc;
    }
}
