package la.com.github.bureau.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collection;

import la.com.github.bureau.R;
import la.com.github.bureau.adapter.UserAdsAdapter;
import la.com.github.bureau.pojo.Ad;
import la.com.github.bureau.pojo.User;

public class ProfileFragment extends Fragment {

    private ImageView userImage;
    private TextView userName;
    private TextView userLocation;
    private TextView userRegistered;
    private TextView userCountReturning;
    private TextView userCountReturned;
    private RecyclerView ads;
    private UserAdsAdapter userAdsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_profile, container, false);
        init(view);
        loadUserInfo();
        initAds(view);
        loadAds();
        return view;
    }

    private void init(View view) {
        userImage = view.findViewById(R.id.user_image_view);
        userName = view.findViewById(R.id.user_name_text_view);
        userLocation = view.findViewById(R.id.user_location_text_view);
        userRegistered = view.findViewById(R.id.user_account_registered_date_text_view);
        userCountReturning = view.findViewById(R.id.user_count_returning_text_view);
        userCountReturned = view.findViewById(R.id.user_count_returned_text_view);
    }


    private Collection<Ad> getAds() {
        return Arrays.asList(
                new Ad("Rat", "https://pixy.org/src/469/4694649.jpg"),
                new Ad("Cat", "https://www.zastavki.com/pictures/originals/2013/Animals___Cats_Cat_wearing_a_tie_043904_.jpg"),
                new Ad("Bag", "https://i1.rozetka.ua/goods/5870363/29809007_images_5870363904.jpg"),
                new Ad("Компьютер Новый Core i7 GTX1060 RAM 8Gb 1060x2080", "https://www.mitso.by/web/uploads/ckeditor/1508770194.jpg"),
                new Ad("iPhone", "https://avatars.mds.yandex.net/get-pdb/163339/258f0324-90f3-41b6-b1d0-aaee79827d1a/s1200?webp=false"),
                new Ad("Паспорт", "https://www.wanista.com/wp-content/uploads/2014/04/IMG_2586-581x683.jpg")
        );
    }

    private void loadAds() {
        Collection<Ad> adList = getAds();
        userAdsAdapter.setItems(adList);
    }

    private void initAds(View view) {
        ads = view.findViewById(R.id.user_ads_recycler_view);
        ads.setLayoutManager(new LinearLayoutManager(getContext()));

        userAdsAdapter = new UserAdsAdapter();
        ads.setAdapter(userAdsAdapter);
    }


    private void displayUserInfo(User user) {
        userName.setText(user.getName());
    }

   /* private User getUserId() {
        return new User(11, "https://pp.userapi.com/c840239/v840239928/29ac0/cvAXTu9-7XM.jpg",
                "Александр Альбертович", "Москва, Коптево",
                "23 сентября 2018", 204, 100);
    }*/

    private void loadUserInfo() {
        //User user = getUserId();
       // displayUserInfo(user);
    }
}
