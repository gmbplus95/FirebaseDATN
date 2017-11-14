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
 * Created by Admin on 06-09-2017.
 */

public class ImageListAdapter extends ArrayAdapter<ImageUpload> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;

    public ImageListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ImageUpload> objects) {
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
        TextView txtName=v.findViewById(R.id.imgName);
        ImageView img =v.findViewById(R.id.img_View);
        TextView txtDesc=v.findViewById(R.id.imgDesc);
        TextView txtEmail=v.findViewById(R.id.imgEmail);
        txtName.setText("Tên ảnh: "+ listImage.get(position).getName());
        txtDesc.setText("Mô tả: " +listImage.get(position).getDescription());
        txtEmail.setText("Upload By: "+ listImage.get(position).getEmail());
        Glide.with(context)
                .load(listImage.get(position)
                        .getUrl())
                .into(img);
        return v;
    }

}
