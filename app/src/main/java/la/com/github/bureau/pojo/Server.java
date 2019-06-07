package la.com.github.bureau.pojo;

import java.util.ArrayList;

public class Server {


    private static ArrayList<String> passwordDB = new ArrayList<>();
    private static ArrayList<String> loginDB = new ArrayList<>();
    private static ArrayList<String> nameDB = new ArrayList<>();
         static{
                passwordDB.add("1234");
                loginDB.add("qwert");
                nameDB.add("Alex");
            }


    public static boolean hasUser(String login, String password) {

           int hasPassword = passwordDB.indexOf(password);
           int hasLogin = loginDB.indexOf(login);

        return (hasLogin != -1 && hasPassword != -1);
    }


    public static boolean hasUser(String login){
        int hasLogin = loginDB.indexOf(login);
        return (hasLogin != -1);
    }

    public static boolean addData(String userName, String login, String password) {
        int isAdded = (int) (Math.random() * 2);
        if(isAdded == 1){
            nameDB.add(userName);
            passwordDB.add(password);
            loginDB.add(login);
            return  true;
        }
        return  false;
    }
}
