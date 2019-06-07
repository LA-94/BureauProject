package la.com.github.bureau.pojo;

public class UserForAds {
    private String UID;
    private String name;
    private String phoneNumber;

    public void setAll(UserForAds userForAds){
        this.UID = userForAds.getUID();
        this.name = userForAds.getName();
        this.phoneNumber = userForAds.getPhoneNumber();
    }

    public UserForAds() {

    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserForAds user = (UserForAds) o;

        if (!UID.equals(user.UID)) return false;
        if (!name.equals(user.name)) return false;
        return phoneNumber.equals(user.phoneNumber);
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
