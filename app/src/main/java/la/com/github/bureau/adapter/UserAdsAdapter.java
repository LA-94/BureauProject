package la.com.github.bureau.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import la.com.github.bureau.R;
import la.com.github.bureau.pojo.Ad;

public class UserAdsAdapter extends RecyclerView.Adapter<UserAdsAdapter.AdViewHolder>{
    private final String AD_RESPONSE_FORMAT_DATE = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    private final String YEAR_MONTH_DAY_FORMAT_DATE = "d MMM yyyy";
    private List<Ad> adList = new ArrayList<>();

    class AdViewHolder extends RecyclerView.ViewHolder{

        private ImageView adImage;
        private TextView adTitle;
        private TextView adTextFound;
        private TextView adFoundDate;

        public AdViewHolder(View itemView){
            super(itemView);
            init(itemView);
        }

        private void init(View itemView){
            adImage = itemView.findViewById(R.id.ad_image_view);
            adTitle = itemView.findViewById(R.id.ad_title_text_view);
            adTextFound = itemView.findViewById(R.id.ad_text_found_text_view);
            adFoundDate = itemView.findViewById(R.id.ad_found_date_text_view);
        }

        public void bind(Ad ad){
            adTitle.setText(ad.getTitle());
            adTextFound.setText(R.string.found);
            String dateFormatted = Ad.dateFormatted(ad.getPlacementDate());
            adFoundDate.setText(dateFormatted);
            Picasso.with(itemView.getContext()).load(ad.getPhotoUrl()).into(adImage);

       }


    }

    public void setItems(Collection<Ad> ads){
        adList.addAll(ads);
        notifyDataSetChanged();
    }

    public void clearItems(){
        adList.clear();
        notifyDataSetChanged();
    }

    @Override
    public AdViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ad, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdViewHolder holder, int position){
        holder.bind(adList.get(position));
    }



    @Override
    public int getItemCount(){
        return adList.size();
    }


}
