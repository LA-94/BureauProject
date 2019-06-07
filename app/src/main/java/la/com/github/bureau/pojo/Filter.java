package la.com.github.bureau.pojo;


import android.util.Log;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import la.com.github.bureau.R;

public class Filter {
    private int mCategoryIndex = CategoryBase.getInstance().getCategories().size();
    private int mLatitudeIndex;
    private int mLongitudeIndex;
    private int mRadiusDistanceIndex;
    private int mPublishedIndex;
    private int mSortByIndex;


    public int getRadiusDistanceStringResource() {
        switch (mRadiusDistanceIndex) {
            case 0:
                return R.string.radius_20km;
            case 1:
                return R.string.radius_15km;
            case 2:
                return R.string.radius_10km;
            case 3:
                return R.string.radius_5km;
            case 4:
                return R.string.radius_3km;
            case 5:
                return R.string.radius_1km;
            case 6:
                return R.string.radius_500m;
        }
        return 0;
    }

    public int getPublishedStringResource() {
        switch (mPublishedIndex) {
            case 0:
                return R.string.published_for_all_time;
            case 1:
                return R.string.published_in_7_days;
            case 2:
                return R.string.published_in_3_days;
            case 3:
                return R.string.published_in_24_hours;
            case 4:
                return R.string.published_in_12_hours;
            case 5:
                return R.string.published_in_1_hour;
        }
        return 0;
    }

    public long getPlacementDateSearch() {
        long timeNowadays = new Date().getTime();
        switch (mPublishedIndex) {
            case 0:
                return 0;
            case 1:
                return timeNowadays - 1000 * 60 * 60 * 24 * 7;
            case 2:
                return timeNowadays - 1000 * 60 * 60 * 24 * 3;
            case 3:
                return timeNowadays - 1000 * 60 * 60 * 24;
            case 4:
                return timeNowadays - 1000 * 60 * 60 * 12;
            case 5:
                return timeNowadays - 1000 * 60 * 60;
        }
        return 0;
    }


    public int getSortByStringResource() {
        switch (mSortByIndex) {
            case 0:
                return R.string.sort_by_publication_date;
            case 1:
                return R.string.sort_by_date_of_discovery;
            case 2:
                return R.string.sort_by_distance;
        }
        return 0;
    }

    public Filter() {
        mCategoryIndex = CategoryBase.getInstance().getCategories().size();
    }

    public void setIndexes(int startIndex) {
        setCategoryIndex(CategoryBase.getInstance().getCategories().size());
        setLatitudeIndex(startIndex);
        setLongitudeIndex(startIndex);
        setRadiusDistanceIndex(startIndex);
        setPublishedIndex(startIndex);
        setSortByIndex(startIndex);
    }

    public void changeFilter(Filter newFilter) {
        mCategoryIndex = newFilter.getCategoryIndex();
        mLatitudeIndex = newFilter.getLatitudeIndex();
        mLongitudeIndex = newFilter.getLongitudeIndex();
        mRadiusDistanceIndex = newFilter.getRadiusDistanceIndex();
        mPublishedIndex = newFilter.getPublishedIndex();
        mSortByIndex = newFilter.getSortByIndex();
    }

    public int getCategoryIndex() {
        return mCategoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        mCategoryIndex = categoryIndex;
    }

    public int getLatitudeIndex() {
        return mLatitudeIndex;
    }

    public void setLatitudeIndex(int latitudeIndex) {
        mLatitudeIndex = latitudeIndex;
    }

    public int getLongitudeIndex() {
        return mLongitudeIndex;
    }

    public void setLongitudeIndex(int longitudeIndex) {
        mLongitudeIndex = longitudeIndex;
    }

    public int getRadiusDistanceIndex() {
        return mRadiusDistanceIndex;
    }

    public void setRadiusDistanceIndex(int radiusDistanceIndex) {
        mRadiusDistanceIndex = radiusDistanceIndex;
    }

    public int getPublishedIndex() {
        return mPublishedIndex;
    }

    public void setPublishedIndex(int publishedIndex) {
        mPublishedIndex = publishedIndex;
    }

    public int getSortByIndex() {
        return mSortByIndex;
    }

    public void setSortByIndex(int sortByIndex) {
        mSortByIndex = sortByIndex;
    }


    private static void quickSort(Ad[] array, int low, int high, int sort) {
        if (array.length == 0)
            return;//завершить выполнение если длина массива равна 0

        if (low >= high)
            return;//завершить выполнение если уже нечего делить

        // выбрать опорный элемент
        int middle = low + (high - low) / 2;
        long opora = getSortBy(array[middle], sort);

        // разделить на подмассивы, который больше и меньше опорного элемента
        int i = low, j = high;
        while (i <= j) {
            while (getSortBy(array[i], sort) < opora) {
                i++;
            }

            while (getSortBy(array[j], sort) > opora) {
                j--;
            }

            if (i <= j) {//меняем местами
                Ad temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        // вызов рекурсии для сортировки левой и правой части
        if (low < j)
            quickSort(array, low, j, sort);

        if (high > i)
            quickSort(array, i, high, sort);
    }

    public static List<Ad> getSortedAds(List<Ad> ads, int sortBy) {

        Ad[] array = ads.toArray(new Ad[ads.size()]);
        int low = 0;
        int high = ads.size() - 1;

        quickSort(array, low, high, sortBy);
        ads = Arrays.asList(array);
        return ads;
    }

    private static long getSortBy(Ad ad, int sort) {
        switch (sort) {
            case 0:
                return ad.getPlacementDate();
        }
        return ad.getFoundDate();
    }


}


