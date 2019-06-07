package la.com.github.bureau.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import la.com.github.bureau.R;
import la.com.github.bureau.pojo.Category;
import la.com.github.bureau.pojo.CategoryBase;

public class DialogCategoryFragment extends DialogFragment {


    public static final String EXTRA_ID_CATEGORY = "la.com.github.bureau.category";
    private RecyclerView mRecyclerView;
    private CategoryAdapter mCategoryAdapter;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fr_dialog_category, null);
        mRecyclerView = view.findViewById(R.id.dialog_category_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.choice_category)
                .setView(view)
                .create();
    }

    private void sendResult(int resultCode, int idCategory) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID_CATEGORY, idCategory);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void updateUI() {
        CategoryBase categoryBase = CategoryBase.getInstance();
        List<Category> categories = categoryBase.getCategories();
        if (getTargetRequestCode()==FilterFragment.REQUEST_CODE_CATEGORY){
            categories.add(new Category(R.string.all_category, R.drawable.ic_all_category));
        }

        if (mCategoryAdapter == null) {
            mCategoryAdapter = new CategoryAdapter(categories);
            mRecyclerView.setAdapter(mCategoryAdapter);
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Category mCategory;
        private ImageView mCategoryImageView;
        private TextView mCategoryTextView;

        public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_dialog_category, parent, false));
            itemView.setOnClickListener(this);
            mCategoryImageView = itemView.findViewById(R.id.category_image_view);
            mCategoryTextView = itemView.findViewById(R.id.category_text_view);
        }

        @Override
        public void onClick(View v) {
            int idCategory = mRecyclerView.getChildAdapterPosition(v);
            sendResult(Activity.RESULT_OK, idCategory);
            DialogCategoryFragment.this.dismiss();
        }

        public void bind(Category category) {
            mCategory = category;
            mCategoryTextView.setText(getString(mCategory.getCategoryResourceText()));
            mCategoryImageView.setImageResource(mCategory.getCategoryResourceImage());
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<Category> mCategories;

        public CategoryAdapter(List<Category> categories) {
            mCategories = categories;
        }


        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CategoryHolder categoryHolder, int position) {
            Category category = mCategories.get(position);
            categoryHolder.bind(category);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }
    }
}
