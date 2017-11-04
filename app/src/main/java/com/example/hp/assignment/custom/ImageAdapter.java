package com.example.hp.assignment.custom;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.hp.assignment.R;
import com.example.hp.assignment.model.Image;

import java.util.List;

/**
 * Created by hp on 10/23/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Image> images;

    public ImageAdapter(List<Image> images) {
        this.images = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(viewType, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_picture;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Image image=images.get(position);
        byte[] img=image.getmImage();
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
    }

    @Override
    public int getItemCount() {
        return null!=images ? images.size() : 0;
    }


    class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageButton imageButton;
        public ImageViewHolder(View itemView) {
            super(itemView);
            itemView=itemView.findViewById(R.id.iv_image);
            imageButton=itemView.findViewById(R.id.ibtn_cancel);
        }
    }
}
