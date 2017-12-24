package com.example.admin.firebasedemo;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Admin on 09-12-2017.
 */

public class My_Image_List_Adapter extends ArrayAdapter<ImageUpload> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;

    public My_Image_List_Adapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        listImage=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View v=inflater.inflate(resource,null);
        TextView txtName=v.findViewById(R.id.imgName1);
        ImageView img1 =v.findViewById(R.id.img_View1);
        TextView txtDesc=v.findViewById(R.id.imgDesc1);
        txtName.setText("Tên ảnh: "+ listImage.get(position).getName());
        txtDesc.setText("Mô tả: " +listImage.get(position).getDescription());
        Glide.with(context)
                .load(listImage.get(position)
                        .getUrl())
                .into(img1);
        return v;
    }

}
