package com.seyfullahpolat.rahatlaticisesler.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.seyfullahpolat.rahatlaticisesler.R;
import com.seyfullahpolat.rahatlaticisesler.item.CategoryItem;

import java.util.ArrayList;

import jp.wasabeef.fresco.processors.BlurPostprocessor;
/**
 * Created by seyfullahpolat on 15/07/2018.
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.Myholder> {
    private CategoryListAdapter.ClickListener clickListener = null;
    private boolean isPlaying = false;
    AnimationDrawable rocketAnimation;

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private ArrayList<CategoryItem> categoryItems;
    private Context context;

    public class Myholder extends RecyclerView.ViewHolder {
        public TextView categoryName;
        public SimpleDraweeView categoryIcon;


        public Myholder(View view) {
            super(view);

            categoryName = (TextView) view.findViewById(R.id.category_name);
            categoryIcon = (SimpleDraweeView) view.findViewById(R.id.category_icon);

        }
    }


    public CategoryListAdapter(ArrayList<CategoryItem> categoryItems, Context context) {
        this.categoryItems = categoryItems;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(final Myholder holder, final int position) {
        final CategoryItem resultsItem = categoryItems.get(position);
        holder.categoryName.setText(resultsItem.getC_name());

        Uri uri = Uri.parse(resultsItem.getC_image_path());
        holder.categoryIcon.setScaleType(ImageView.ScaleType.FIT_END);
        //holder.categoryIcon.setImageURI(uri);
        Postprocessor postprocessor = new BlurPostprocessor(context,20);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(postprocessor)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(holder.categoryIcon.getController())
                .build();

        holder.categoryIcon.setController(controller);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClickDetail(view, position);
                }
            }
        });

    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_row, parent, false);
        return new Myholder(itemView);
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public interface ClickListener {
        void onClickDetail(View view, int position);

    }
}