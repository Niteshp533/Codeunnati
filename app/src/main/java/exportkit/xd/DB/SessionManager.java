package exportkit.xd.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN= "IsLoggedIn";
    private static final String KEY_USERID= "userID";

    public SessionManager(Context context) {
        context = context;
        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(long id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putLong(KEY_USERID, id);

        editor.commit();
    }

    public long getUserFromSession() {
        long userID= userSession.getLong(KEY_USERID,0);
        return userID;
    }

    public boolean checkLogin(){
        if(userSession.getBoolean(IS_LOGIN, true)){
            return true;
        }
        else
            return false;

    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }

}
