package la.com.github.bureau.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import la.com.github.bureau.R;
import la.com.github.bureau.ac.DisconnectionActivity;
import la.com.github.bureau.activity.AdPagerActivity;
import la.com.github.bureau.activity.FilterActivity;
import la.com.github.bureau.activity.SingleFragmentActivity;
import la.com.github.bureau.pojo.Ad;
import la.com.github.bureau.pojo.BulletinBoard;
import la.com.github.bureau.pojo.CategoryBase;
import la.com.github.bureau.pojo.Filter;

public class AdListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mAdRecyclerView;
    private AdAdapter mAdAdapter;
    private ImageButton mFilterBtn;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static Filter sFilterForSearch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sFilterForSearch = new Filter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_ad_list, container, false);
        mAdRecyclerView = v.findViewById(R.id.ads_recycler_view);
        mAdRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProgressBar = v.findViewById(R.id.progress_bar);
        mFilterBtn = v.findViewById(R.id.filter_btn);
        mFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FilterActivity.getIntent(getContext()));
                getActivity().overridePendingTransition(R.anim.translate, R.anim.translate);
            }
        });
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return v;
    }


    private void updateUI() {
        BulletinBoard board = BulletinBoard.get(getActivity());
        List<Ad> ads = board.getAds();
        if (mAdAdapter == null) {
            mAdAdapter = new AdAdapter(ads);
            mAdRecyclerView.setAdapter(mAdAdapter);
        } else {
            mAdAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        getAds();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public static Filter getFilterForSearch() {
        return sFilterForSearch;
    }

    public static void setFilterForSearch(Filter newFilter) {
        sFilterForSearch.changeFilter(newFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        getAds();
    }


    private void getAds() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("ads");
        Query capitalCities;
        if (sFilterForSearch.getCategoryIndex() < CategoryBase.getInstance().getCategories().size()) {

            capitalCities = reference.whereEqualTo("category", sFilterForSearch.getCategoryIndex());
        } else {
            capitalCities = reference.whereLessThanOrEqualTo("category", sFilterForSearch.getCategoryIndex() + 1);
        }
        capitalCities
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                                List<Ad> ads = new ArrayList<>();
                                for (QueryDocumentSnapshot s : task.getResult()) {
                                    Ad ad = s.toObject(Ad.class);
                                    if (ad.getPlacementDate() >= sFilterForSearch.getPlacementDateSearch() && !ad.isReturn()) {
                                        ads.add(ad);
                                    }
                                }
                                ads = Filter.getSortedAds(ads, sFilterForSearch.getSortByIndex());
                                BulletinBoard.get(getActivity()).addAds(ads);
                            } else {
                                BulletinBoard.get(getActivity()).clearBulletinBoard();
                            }
                            updateUI();
                        }
                        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    }

                });
    }

    private class AdHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Ad mAd;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mIsReturnedTextView;
        private ImageView mAdImageView;

        public AdHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_ad, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.ad_title_text_view);
            mDateTextView = itemView.findViewById(R.id.ad_found_date_text_view);
            mAdImageView = itemView.findViewById(R.id.ad_image_view);
            mIsReturnedTextView = itemView.findViewById(R.id.is_returned_text_view);
        }

        @Override
        public void onClick(View v) {
            if (SingleFragmentActivity.hasConnection(v.getContext())) {
                Intent intent = AdPagerActivity.getIntent(getContext(), mAd.getId());
                startActivity(intent);
            } else {
                startActivity(DisconnectionActivity.getIntent(v.getContext()));
            }
            if (getActivity() != null)
                getActivity().overridePendingTransition(R.anim.translate, R.anim.translate);
        }

        public void bind(Ad ad) {
            mAd = ad;
            Glide.with(mAdImageView.getContext())
                    .load(mAd.getPhotoUrl())
                    .into(mAdImageView);
            mTitleTextView.setText(mAd.getTitle());
            mDateTextView.setText(Ad.dateFormatted(mAd.getFoundDate()));
            mIsReturnedTextView.setVisibility(mAd.isReturn() ? View.VISIBLE : View.GONE);
        }
    }

    private class AdAdapter extends RecyclerView.Adapter<AdHolder> {
        private List<Ad> mAds;

        public AdAdapter(List<Ad> ads) {
            mAds = ads;
        }


        @Override
        public AdHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new AdHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AdHolder adHolder, int position) {
            Ad ad = mAds.get(position);
            adHolder.bind(ad);
        }

        @Override
        public int getItemCount() {
            return mAds.size();
        }
    }


}
