package la.com.github.bureau.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Ad {

    private String ID;
    private UUID mId;
    private String mTitle;
    private String mPhotoUrl;
    private long mPlacementDate;
    private long mFoundDate;
    private boolean mIsReturn;
    private String mUserId;
    private String mDetails;
    private int mCategory;
    //private BigDecimal mLatitude;
    // private BigDecimal mLongitude;


    public Ad(String title, String photoUrl) {
        init(title);
        mPhotoUrl = photoUrl;
    }


    public Ad() {
        init("");
        mPhotoUrl = "";

    }

    private void init(String title) {
        mTitle = title;
        mPlacementDate = new Date().getTime();
        mFoundDate = new Date().getTime();
        mId = UUID.randomUUID();
        mIsReturn = false;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public long getPlacementDate() {
        return mPlacementDate;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public void setPlacementDate(long placementDate) {
        mPlacementDate = placementDate;
    }

    public UUID getId() {
        return mId;
    }

    public boolean isReturn() {
        return mIsReturn;
    }

    public void setReturn(boolean aReturn) {
        mIsReturn = aReturn;
    }

    public long getFoundDate() {
        return mFoundDate;
    }

    public void setFoundDate(long foundDate) {
        mFoundDate = foundDate;
    }

    public static String dateFormatted(long dateF) {
        String rawDate = new Date(dateF).toString();
        final String AD_RESPONSE_FORMAT_DATE = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        final String YEAR_MONTH_DAY_FORMAT_DATE = "d MMM yyyy";
        SimpleDateFormat responseDateFormat = new SimpleDateFormat(AD_RESPONSE_FORMAT_DATE, Locale.ROOT);
        SimpleDateFormat displayedDateFormat = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT_DATE, Locale.getDefault());

        try {
            Date date = responseDateFormat.parse(rawDate);
            return displayedDateFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setUserId() {
        mUserId = User.getInstance().getUID();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Ad ad = (Ad) obj;
        return (this.mPlacementDate == ad.mPlacementDate
                && this.mTitle.equals(ad.mTitle)
                && this.mPhotoUrl.equals(ad.mPhotoUrl)
                && this.mFoundDate == ad.mFoundDate
                && this.mIsReturn == ad.mIsReturn
                && this.mUserId.equals(ad.mUserId)
                && this.mDetails.equals(ad.mDetails)
                && this.mCategory == ad.mCategory);
    }


    public String getAdID() {
        return ID;
    }

    public void setAdID(String ID) {
        this.ID = ID;
    }
}
