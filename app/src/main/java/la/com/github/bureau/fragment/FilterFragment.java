package la.com.github.bureau.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import la.com.github.bureau.R;
import la.com.github.bureau.activity.FilterActivity;
import la.com.github.bureau.activity.SearchByPublishedActivity;
import la.com.github.bureau.activity.SearchByRadiusActivity;
import la.com.github.bureau.activity.SortActivity;
import la.com.github.bureau.pojo.CategoryBase;
import la.com.github.bureau.pojo.Filter;

public class FilterFragment extends Fragment {


    public static final String EXTRA_SORT_BY_RADIUS_CHECKED = "la.com.github.bureau.sort_by_radius_checked";
    public static final String EXTRA_SORT_BY_PUBLISHED_CHECKED = "la.com.github.bureau.sort_by_published_checked";
    public static final String EXTRA_SORT_CHECKED = "la.com.github.bureau.sort";
    public static final int REQUEST_CODE_CATEGORY = 0;
    private static final String DIALOG_CATEGORY = "la.com.github.bureau.fr_dialog_category_from_filter";


    private ImageButton mBtnCheck;
    private ImageButton mBtnRefresh;
    private RelativeLayout mRLRadiusSearch;
    private RelativeLayout mRLPublished;
    private RelativeLayout mRLSort;
    private RelativeLayout mRLCategory;

    private TextView mRadiusSearchTextView;
    private TextView mPublishedTextView;
    private TextView mSortByTextView;
    private TextView mCategoryTextView;
    private ImageView mCategoryImageView;


    public static Filter mCreateFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_filter, container, false);
        init(view);
        addOnClickListener();
        displayFilter();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        displayFilter();
    }


    public void init(View view) {
        mBtnCheck = view.findViewById(R.id.check_btn);
        mBtnRefresh = view.findViewById(R.id.refresh_btn);
        mRLRadiusSearch = view.findViewById(R.id.radius_search_RL);
        mRLPublished = view.findViewById(R.id.published_RL);
        mRLSort = view.findViewById(R.id.sort_RL);
        mRLCategory = view.findViewById(R.id.category_relative_layout);

        mCategoryTextView = view.findViewById(R.id.info_category_tv);
        mRadiusSearchTextView = view.findViewById(R.id.info_radius_search_tv);
        mCategoryImageView = view.findViewById(R.id.category_image);
        mPublishedTextView = view.findViewById(R.id.info_published_tv);

        mSortByTextView = view.findViewById(R.id.sort_by_tv);

        mCreateFilter = new Filter();
        mCreateFilter.changeFilter(AdListFragment.getFilterForSearch());
    }

    private void displayFilter() {
        mRadiusSearchTextView.setText(getString(mCreateFilter.getRadiusDistanceStringResource()));
        mPublishedTextView.setText(getString(mCreateFilter.getPublishedStringResource()));
        mSortByTextView.setText(getString(mCreateFilter.getSortByStringResource()));
        displayCategory();
    }

    private void addOnClickListener() {

        mRLCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DialogCategoryFragment dialog = new DialogCategoryFragment();
                dialog.setTargetFragment(FilterFragment.this, REQUEST_CODE_CATEGORY);
                dialog.show(manager, DIALOG_CATEGORY);
            }
        });

        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdListFragment.setFilterForSearch(mCreateFilter);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.translate, R.anim.translate);
            }
        });
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCreateFilter.setIndexes(0);
                displayFilter();
            }
        });

        mRLRadiusSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchByRadiusActivity.getIntent(getContext()));
                getActivity().overridePendingTransition(R.anim.translate, R.anim.translate);
            }
        });
        mRLPublished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchByPublishedActivity.getIntent(getContext()));
                getActivity().overridePendingTransition(R.anim.translate, R.anim.translate);
            }
        });
        mRLSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SortActivity.getIntent(getContext()));
                getActivity().overridePendingTransition(R.anim.translate, R.anim.translate);
            }
        });
    }


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, FilterActivity.class);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CATEGORY) {
            int idCategory = data.getIntExtra(DialogCategoryFragment.EXTRA_ID_CATEGORY, 0);
            mCreateFilter.setCategoryIndex(idCategory);
            displayCategory();
        }
    }

    private void displayCategory(){
        if (mCreateFilter.getCategoryIndex() < CategoryBase.getInstance().getCategories().size()) {
            CategoryBase categoryBase = CategoryBase.getInstance();
            mCategoryTextView.setText(getString(categoryBase.getCategoryTexResourceById(mCreateFilter.getCategoryIndex())));
            mCategoryImageView.setImageResource(categoryBase.getCategoryImageResourceById(mCreateFilter.getCategoryIndex()));
        } else{
            mCategoryTextView.setText(getString(R.string.all_category));
            mCategoryImageView.setImageResource(R.drawable.ic_all_category);
        }
    }
}
