package la.com.github.bureau.pojo;

public class Category {
    private int mCategoryResourceText;
    private int mCategoryResourceImage;

    public Category(int categoryResourceText, int categoryResourceImage) {
        mCategoryResourceText = categoryResourceText;
        mCategoryResourceImage = categoryResourceImage;
    }

    public int getCategoryResourceText() {
        return mCategoryResourceText;
    }

    public int getCategoryResourceImage() {
        return mCategoryResourceImage;
    }
}
