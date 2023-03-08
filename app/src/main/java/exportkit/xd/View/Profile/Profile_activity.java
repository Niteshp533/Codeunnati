package exportkit.xd.View.Profile;

import android.annotation.SuppressLint;
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
import exportkit.xd.View.Register.Login_activity;
import exportkit.xd.View.Search.SearchUser_activity;

public class Profile_activity extends Activity implements IProfile, IAppViews {
    private CircularImageView uploadedImage, ProfileIcon;
    private TextView name , username ;
    private ImageButton HomeButton, editButton , logoutBtn ;
    private Button FavButton, SearchButton, RecipeButton;

    UserController userController;
    RecipeController recipeController;

    RecyclerView recycleRecipeList, recycleFavList;
    List<String>  recipeNameList = new ArrayList<>();
    List<String> recipeImageList = new ArrayList<>();
    exportkit.xd.View.Adapter Adapter;

    String KEY_VALUE="myProfile";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        userController = new UserController(this);
        recipeController = new RecipeController(this);

        // finds views
        recycleRecipeList = findViewById(R.id.recipeList);
        recycleFavList= findViewById(R.id.favList);
        uploadedImage = findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        username = (TextView) findViewById(R.id.__tayshelby_ek2) ;
        FavButton = (Button) findViewById(R.id.FavoriteButton) ;
        editButton = (ImageButton) findViewById(R.id.edit11) ;
        logoutBtn= (ImageButton) findViewById(R.id._list_1) ;
        RecipeButton= (Button)findViewById(R.id.recipes);

        SearchButton = (Button) findViewById(R.id.ellipse_ek22);
        HomeButton = (ImageButton) findViewById(R.id.home_ek11);
        ProfileIcon = findViewById(R.id.profile1);

        // get logged user
        SessionManager session = new SessionManager(this);
        long loggedUser= session.getUserFromSession();
        User user= userController.getUser((int)loggedUser);

        //display IProfile info
        if(user.getAvatar() != null) {
            uploadedImage.setImageURI(Uri.parse(user.getAvatar()));
            ProfileIcon.setImageURI(Uri.parse(user.getAvatar()));
        }
        name.setText(user.getName());
        username.setText(user.getUsername());

        //Recyclerview as GridView for recipes
        //get user recipe from db
        recycleRecipeList.setVisibility(View.VISIBLE);
        Vector<Integer> recipesIdList= recipeController.viewRecipeList((int) loggedUser);
        for(int i=0; i<recipesIdList.size();i++){
            Recipe recipe= recipeController.getRecipe(recipesIdList.get(i));
            recipeNameList.add(recipe.getName());
            recipeImageList.add(recipe.getImage());
        }
        viewDynamic(recycleRecipeList ,recipesIdList);

        // buttons functions
        HomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Homepage_activity.class);
                startActivity(nextScreen);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                session.logoutUserFromSession();
                Intent nextScreen = new Intent(getApplicationContext(), Login_activity.class);
                startActivity(nextScreen);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), EditProfile_activity.class);
                startActivity(nextScreen);
            }
        });
        RecipeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Profile_activity.class);
                startActivity(nextScreen);
            }
        });
        FavButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                RecipeButton.setTextColor(R.color.choice);
                FavButton.setTextColor(R.color.select);
                recycleRecipeList.setVisibility(View.GONE);
                recycleFavList.setVisibility(View.VISIBLE);
                KEY_VALUE="userProfile";

                //Recyclerview as GridView for recipes
                //get user recipe from db
                Vector<Integer> favRecipesIdList= recipeController.viewFavList((int) loggedUser);
                //System.out.println(loggedUser+"------------------------------------------------------------------------------"+favRecipesIdList);
                for(int i=0; i<favRecipesIdList.size();i++){
                    Recipe fav= recipeController.getRecipe(favRecipesIdList.get(i));
                    recipeNameList.add(fav.getName());
                    recipeImageList.add(fav.getImage());
                    viewDynamic(recycleFavList, favRecipesIdList);
                }
            }
        });
        SearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), SearchUser_activity.class);
                startActivity(nextScreen);
                Toast.makeText(getApplication(),"Now You are On Search User Mode",Toast.LENGTH_LONG).show();


            }
        });

    }

    private void viewDynamic(RecyclerView recycle, List<Integer> recipesIdList){
        Adapter = new Adapter(this, recipesIdList, recipeNameList, recipeImageList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2, GridLayoutManager.VERTICAL,false);
        recycle.setLayoutManager(gridLayoutManager);
        recycle.setAdapter(Adapter);
    }

    @Override
    public void viewRecipeDetails(int id) {
        Intent nextScreen = new Intent(getApplicationContext(), RecipeDetails_activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("IProfile",KEY_VALUE);
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
