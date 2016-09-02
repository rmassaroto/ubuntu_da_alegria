package br.com.horizonnew.ubuntudaalegria.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.image.PicassoSingleton;
import br.com.horizonnew.ubuntudaalegria.model.Post;

/**
 * Created by renan on 30/08/16.
 */
public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.BaseViewHolder> {

    private Context mContext;
    private ArrayList<Post> mPosts;

    public FeedListAdapter(@NonNull Context context) {
        mContext = context;
        mPosts = new ArrayList<>();
    }

    public FeedListAdapter(@NonNull Context context, @NonNull ArrayList<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @Override
    public int getItemViewType(int position) {
        return mPosts.get(position).getType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        BaseViewHolder viewHolder;

        switch (viewType) {
            case Post.TYPE_TEXT:
                viewHolder = new TextViewHolder(layoutInflater.inflate(R.layout.list_feed_image_item_1, parent, false));
                break;

            case Post.TYPE_IMAGE:
                viewHolder = new ImageViewHolder(layoutInflater.inflate(R.layout.list_feed_image_item_1, parent, false));
                break;

            case Post.TYPE_VIDEO:
                viewHolder = new ImageViewHolder(layoutInflater.inflate(R.layout.list_feed_image_item_1, parent, false));
                break;

            default:
                viewHolder = null;
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.clearData();

        Post post = mPosts.get(position);

        switch (getItemViewType(position)) {
            case Post.TYPE_IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

                if (post.getUser() != null) {
                    if (post.getUser().getPictureUrl() != null && !post.getUser().getPictureUrl().trim().isEmpty()) {
                        PicassoSingleton.getInstance(mContext)
                                .load(post.getUser().getPictureUrl())
                                .fit()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(imageViewHolder.userProfileImageView);
                    }

                    imageViewHolder.userNameImageView.setText(post.getUser().getName());
                }

                if (post.getUrl() != null && !post.getUrl().trim().isEmpty()) {
                    PicassoSingleton.getInstance(mContext)
                            .load(post.getUrl())
                            .fit()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.drawable.image_splash)
                            .into(imageViewHolder.postImageView);
                }

                imageViewHolder.postTitleImageView.setText(post.getTitle());
                imageViewHolder.postDescriptionImageView.setText(post.getText());

                if (post.isCampaign())
                    imageViewHolder.campaignTextView.setVisibility(View.VISIBLE);

                break;

            case Post.TYPE_VIDEO:
                ImageViewHolder videoViewHolder = (ImageViewHolder) holder;

                if (post.getUser() != null) {
                    if (post.getUser().getPictureUrl() != null && !post.getUser().getPictureUrl().trim().isEmpty()) {
                        PicassoSingleton.getInstance(mContext)
                                .load(post.getUser().getPictureUrl())
                                .fit()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(videoViewHolder.userProfileImageView);
                    }

                    videoViewHolder.userNameImageView.setText(post.getUser().getName());
                }

                if (post.getUrl() != null && !post.getUrl().trim().isEmpty()) {

                    PicassoSingleton.getInstance(mContext)
                            .load(getYoutubeThumbnailUrl(post))
                            .fit()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.drawable.image_splash)
                            .into(videoViewHolder.postImageView);
                }

                videoViewHolder.postTitleImageView.setText(post.getTitle());
                videoViewHolder.postDescriptionImageView.setText(post.getText());

                if (post.isCampaign())
                    videoViewHolder.campaignTextView.setVisibility(View.VISIBLE);

                break;
        }
    }

    @NonNull
    private String getYoutubeThumbnailUrl(Post post) {
        String result =  post.getUrl().replace("http://www.youtube.com/watch?v=", "");
        result =  result.replace("https://www.youtube.com/watch?v=", "");
        result =  result.replace("https://youtu.be/", "");
        result =  "http://img.youtube.com/vi/" +  result + "/hqdefault.jpg";

        return result;
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void setDataSet(@NonNull ArrayList<Post> posts) {
        notifyItemRangeRemoved(0, mPosts.size());
        mPosts = posts;
        notifyItemRangeInserted(0, mPosts.size());
    }

    public class ImageViewHolder extends BaseViewHolder implements View.OnClickListener {

        ImageView userProfileImageView, postImageView;
        TextView userNameImageView, postTitleImageView, postDescriptionImageView, campaignTextView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            userProfileImageView = (ImageView) itemView.findViewById(R.id.image_1);
            postImageView = (ImageView) itemView.findViewById(R.id.activity_create_post_image_view);

            userNameImageView = (TextView) itemView.findViewById(R.id.text_1);
            postTitleImageView = (TextView) itemView.findViewById(R.id.text_2);
            postDescriptionImageView = (TextView) itemView.findViewById(R.id.text_3);
            campaignTextView = (TextView) itemView.findViewById(R.id.text_4);

            itemView.setOnClickListener(this);
        }

        public void clearData() {
            userProfileImageView.setImageBitmap(null);
            postImageView.setImageBitmap(null);

            userNameImageView.setText(null);
            postTitleImageView.setText(null);
            postDescriptionImageView.setText(null);

            campaignTextView.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            if (mPosts.get(getAdapterPosition()).getType() == Post.TYPE_VIDEO) {
                mContext.startActivity(
                        new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(mPosts.get(getAdapterPosition()).getUrl())
                        )
                );

            }
        }
    }

    public class TextViewHolder extends BaseViewHolder {

        public TextViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public void clearData() {}
    }
}
