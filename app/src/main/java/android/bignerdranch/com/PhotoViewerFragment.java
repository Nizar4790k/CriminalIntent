package android.bignerdranch.com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.File;


public class PhotoViewerFragment extends DialogFragment {

    private static final String FILE_PHOTO="android.bignerdranch.com.FilePhoto";

    private File mPhotoFile;
    private ImageView mPhotoView;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.photo_viewer_dialog_fragment,null);
      mPhotoView = view.findViewById(R.id.imageViewDialog);

      if (mPhotoFile==null){
          mPhotoFile = (File) savedInstanceState.getSerializable(FILE_PHOTO);
      }


      updatePhotoView();


        return new AlertDialog.Builder(getActivity()).setView(view)
                .create();
    }

    public void setPhoto(File file){
        mPhotoFile = file;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FILE_PHOTO,mPhotoFile);
    }
}
