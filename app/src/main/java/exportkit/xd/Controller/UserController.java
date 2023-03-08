package exportkit.xd.Controller;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

import exportkit.xd.DB.AppDBController;
import exportkit.xd.DB.SessionManager;
import exportkit.xd.Model.User;
import exportkit.xd.View.IAppViews;

public class UserController{

    AppDBController db;
    SessionManager session;
    IAppViews view;

    public UserController(IAppViews view) {
        this.view = view;
        db = new AppDBController((Context) this.view);
    }

    public void openSession(long id) {
        session= new SessionManager((Context) this.view);
        session.createLoginSession(id);
    }

    public void signUp(User user){
        long id = db.Register(user);

        if(id>0) {
            openSession(id);
            view.onSuccess("Registration Successfully");
        }
        else
            view.onError("email or username exists, enter new one!!!!");
    }

    public void login(String email, String password) {
        boolean i = db.loginValidation(email,password);

        if(i){
            openSession(db.getUserID(email));
            view.onSuccess("Login Successfully");
        }
        else
            view.onError("Try Again !!!!");
    }

    public void logout() {
        if(session.checkLogin())
            session.logoutUserFromSession();
    }
    public User getUser(int id){
        return db.getUser(id);
    }

    public void EditProfile(User user){

        Boolean data = db.editUser(user);
        if(data)
            view.onSuccess("Edit Operation is Successfully");
        else
            view.onError("username Exists, enter new one!!!!");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<User> SearchUser(String username) {
         return db.searchUser(username);
    }

}


