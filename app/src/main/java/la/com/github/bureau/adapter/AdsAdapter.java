package la.com.github.bureau.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import la.com.github.bureau.R;
import la.com.github.bureau.pojo.Ad;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.AdsViewHolder> {

    private OnAdClickListener onAdClickListener;
    private List<Ad> adList = new ArrayList<>();

    public AdsAdapter(OnAdClickListener onAdClickListener) {
        this.onAdClickListener = onAdClickListener;
    }

    class AdsViewHolder extends RecyclerView.ViewHolder {

        private ImageView adPhoto;
        private TextView adTitleTv;
        private TextView foundTextTv;
        private TextView foundDateTv;

        public AdsViewHolder(View itemView) {
            super(itemView);
            init();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ad ad = adList.get(getLayoutPosition());
                    onAdClickListener.onAdClick(ad);
                }
            });
        }

        private void init() {
            adPhoto = itemView.findViewById(R.id.ad_image_view);
            adTitleTv = itemView.findViewById(R.id.ad_title_text_view);
            foundTextTv = itemView.findViewById(R.id.ad_text_found_text_view);
            foundDateTv = itemView.findViewById(R.id.ad_found_date_text_view);
        }

        private void bind(Ad ad) {
            adTitleTv.setText(ad.getTitle());
            foundTextTv.setText(R.string.found);
            String date = Ad.dateFormatted(ad.getPlacementDate());
            foundDateTv.setText(date);
            Picasso.with(itemView.getContext()).load(ad.getPhotoUrl()).into(adPhoto);
        }
    }

    @Override
    public AdsViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ad, parent, false);
        return new AdsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdsViewHolder holder, int position) {
        holder.bind(adList.get(position));
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public void setAds(Collection<Ad> collection) {
        adList.addAll(collection);
        notifyDataSetChanged();
    }

    public void clearAds() {
        adList.clear();
        notifyDataSetChanged();
    }

    public interface OnAdClickListener {
        void onAdClick(Ad ad);
    }

}

