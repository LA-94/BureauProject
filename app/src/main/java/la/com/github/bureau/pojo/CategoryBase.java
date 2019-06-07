package la.com.github.bureau.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import la.com.github.bureau.R;

public class CategoryBase {
    private List<Category> mCategories;
    private static CategoryBase sCategoryBase;


    private CategoryBase() {
        mCategories = new ArrayList<>(Arrays.asList(
                new Category(R.string.keys, R.drawable.ic_category_key),
                new Category(R.string.wallets, R.drawable.ic_category_wallet),
                new Category(R.string.bank_card, R.drawable.ic_category_bank_card),
                new Category(R.string.documents, R.drawable.ic_category_documents),
                new Category(R.string.books, R.drawable.ic_category_books),
                new Category(R.string.electronics, R.drawable.ic_category_electronics),
                new Category(R.string.bags, R.drawable.ic_category_bag),
                new Category(R.string.clothes, R.drawable.ic_category_clothes),
                new Category(R.string.accessories, R.drawable.ic_category_accessory),
                new Category(R.string.animals, R.drawable.ic_category_animals),
                new Category(R.string.baby_stuff, R.drawable.ic_category_baby_stuff),
                new Category(R.string.musical_instruments, R.drawable.ic_category_musical_instruments),
                new Category(R.string.other, R.drawable.ic_category_other)
        ));
    }

    public static CategoryBase getInstance() {
        if (sCategoryBase == null) {
            return new CategoryBase();
        } else {
            return sCategoryBase;
        }
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public int getCategoryTexResourceById(int index) {
        return mCategories.get(index).getCategoryResourceText();
    }

    public int getCategoryImageResourceById(int index) {
        return mCategories.get(index).getCategoryResourceImage();
    }
}
