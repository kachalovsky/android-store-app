package fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.image_fragment;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.BaseInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.interfaces.IChainParent;

import static android.app.Activity.RESULT_OK;

/**
 * Created by andre on 04.10.2017.
 */

public class ImageInputFragment<Type extends IImageInputItem> extends BaseInputFragment {
    private static int RESULT_LOAD_IMAGE = 1;

    public static <Type extends IImageInputItem> Fragment newInstance(IChainParent<Type> delegate) {
        ImageInputFragment<Type> fragment = new ImageInputFragment();
        fragment.setDelegate(delegate);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_item, null);
        ImageView imgView = (ImageView) view.findViewById(R.id.img_select);

        String imgBase64 = ((IImageInputItem)getDelegate().passData()).getImagePath();
        if (imgBase64 != null) {
            byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgView.setImageBitmap(decodedByte);
        }
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        return   view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            if (picturePath != null) {
                IImageInputItem item = (IImageInputItem) getDelegate().passData();
                item.setImagePath(encodedImage);
                getDelegate().dataDidChange(item);
                ImageView imageView = (ImageView) getView().findViewById(R.id.img_select);
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedByte);
            }
        }
    }
}
