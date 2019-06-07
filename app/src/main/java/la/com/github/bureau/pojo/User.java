package la.com.github.bureau.pojo;

public class User extends UserForAds {
    private static User sUser;

    public User() {
        super();
        sUser = this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        //User user = (User) o;
        return super.equals(o);
    }

    public static User getInstance() {
        if (sUser == null) {
            sUser = new User();
        }
        return sUser;
    }
}
