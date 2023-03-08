package exportkit.xd.View.Profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import exportkit.xd.Controller.RecipeController;
import exportkit.xd.Controller.UserController;
import exportkit.xd.DB.SessionManager;
import exportkit.xd.Model.Recipe;
import exportkit.xd.Model.User;
import exportkit.xd.R;
import exportkit.xd.View.Adapter;
import exportkit.xd.View.Homepage_activity;
import exportkit.xd.View.IAppViews;
import exportkit.xd.View.Recipe.RecipeDetails_activity;
import exportkit.xd.View.Search.SearchUser_activity;

public class UserProfile_activity extends Activity implements IProfile, IAppViews {
    private CircularImageView uploadedImage, ProfileIcon;
    private TextView name , username ;
    private ImageButton HomeButton, editButton , logoutBtn ;
    private Button FavButton , SearchButton;

    int userID;
    UserController userController;
    RecipeController recipeController;

    RecyclerView recycleRecipeList;
    List<String>  recipeNameList = new ArrayList<>();
    List<String> recipeImageList = new ArrayList<>();
    Adapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        userController = new UserController(this);
        recipeController = new RecipeController(this);

        // finds views
        recycleRecipeList = findViewById(R.id.recipeList);
        uploadedImage = findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        username = (TextView) findViewById(R.id.__tayshelby_ek2) ;
        FavButton = (Button) findViewById(R.id.FavoriteButton) ;
        editButton = (ImageButton) findViewById(R.id.edit11) ;
        logoutBtn= (ImageButton) findViewById(R.id._list_1) ;

        SearchButton = (Button) findViewById(R.id.ellipse_ek22);
        HomeButton = (ImageButton) findViewById(R.id.home_ek11);
        ProfileIcon = findViewById(R.id.profile1);

        //hide unuseful buttons
        FavButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        logoutBtn.setVisibility(View.GONE);
        findViewById(R.id.logout22).setVisibility(View.GONE);

        // get logged user
        SessionManager session = new SessionManager(this);
        long loggedUser= session.getUserFromSession();
        User user= userController.getUser((int)loggedUser);
        if(user.getAvatar() != null) {
            ProfileIcon.setImageURI(Uri.parse(user.getAvatar()));
        }

        //retrieve user id
        userID= getIntent().getExtras().getInt("id");
        user= userController.getUser(userID);

        //display IProfile info
        if(user.getAvatar() != null) {
            uploadedImage.setImageURI(Uri.parse(user.getAvatar()));
        }
        name.setText(user.getName());
        username.setText(user.getUsername());

        //Recyclerview as GridView for recipes
        //get user recipe from db
        Vector<Integer> recipesIdList= recipeController.viewRecipeList(userID);
        for(int i=0; i<recipesIdList.size();i++){
            Recipe recipe= recipeController.getRecipe(recipesIdList.get(i));

            recipeNameList.add(recipe.getName());
            recipeImageList.add(recipe.getImage());
        }

        adapter = new Adapter(this, recipesIdList, recipeNameList, recipeImageList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        recycleRecipeList.setLayoutManager(gridLayoutManager);
        recycleRecipeList.setAdapter(adapter);

        // buttons functions
        HomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Homepage_activity.class);
                startActivity(nextScreen);
            }
        });
        SearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), SearchUser_activity.class);
                startActivity(nextScreen);

            }
        });
        ProfileIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Profile_activity.class);
                startActivity(nextScreen);
            }
        });

    }

    @Override
    public void viewRecipeDetails(int id) {
        Intent nextScreen = new Intent(getApplicationContext(), RecipeDetails_activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("IProfile","searchProfile");
        bundle.putInt("userId", userID);
        nextScreen.putExtras(bundle);
        startActivity(nextScreen);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(getApplication(),message,Toast.LENGTH_LONG).show();
        Intent nextScreen = new Intent(getApplicationContext(), Profile_activity.class);
        startActivity(nextScreen);
    }
    @Override
    public void onError(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

}
