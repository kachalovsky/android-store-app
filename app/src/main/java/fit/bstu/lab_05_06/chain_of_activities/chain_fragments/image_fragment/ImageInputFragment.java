package fit.bstu.lab_05_06.chain_of_activities.chain_fragments.image_fragment;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.chain_of_activities.chain_fragments.BaseInputFragment;
import fit.bstu.lab_05_06.chain_of_activities.interfaces.IChainItem;
import fit.bstu.lab_05_06.chain_of_activities.interfaces.IChainParent;

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
        String imgPath = ((IImageInputItem)getDelegate().passData()).getImagePath();
        if (imgPath != null) {
            imgView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
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
            IImageInputItem item = (IImageInputItem) getDelegate().passData();
            item.setImagePath(picturePath);
            getDelegate().dataDidChange(item);
            ImageView imageView = (ImageView) getView().findViewById(R.id.img_select);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
