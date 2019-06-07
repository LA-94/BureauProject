package la.com.github.bureau.pojo;

public class ValidationModule {

    public static final int LENGTH_ERROR = 1;
    public static final int LETTER_ERROR = 2;
    public static final int SUCCESS = 2;
    private static boolean focusMoved = false;


    public static int validLogin(String login) {

            int sizeLogin = login.length();

            for (int i = 0; i < sizeLogin; i++) {
                char symbol = login.charAt(i);
                if (!(3 < sizeLogin && sizeLogin < 32) && focusMoved) {
                    return LENGTH_ERROR;
                }
                if (!(Character.isLetterOrDigit(symbol) || (symbol == 45)
                        || (symbol == 46) || (symbol == 95))) {
                    return LETTER_ERROR;
                }
            }

        focusMoved = !focusMoved;
        return SUCCESS;
    }
}
