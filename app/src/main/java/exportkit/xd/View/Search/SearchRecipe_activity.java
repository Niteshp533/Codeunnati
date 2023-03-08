package exportkit.xd.View.Search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import exportkit.xd.View.Profile.IProfile;
import exportkit.xd.View.Profile.Profile_activity;
import exportkit.xd.View.Recipe.RecipeDetails_activity;

public class SearchRecipe_activity extends Activity implements IProfile, IAppViews {
    UserController userController;
    RecipeController recipeController;

    RecyclerView recycleSearchList;
    Vector<Integer> recipesIdList= new Vector<>();
    List<String>  recipeNameList = new ArrayList<>();
    List<String> recipeImageList = new ArrayList<>();
    Adapter adapter;

    EditText recipeName;
    Button done , recipesButton , usersButton ;
    ImageButton back, HomeButton;
    private CircularImageView ProfileIcon;
    int RecipeId ;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_recipe);

        userController = new UserController(this);
        recipeController = new RecipeController(this);
        Context cntx= this;

        //find views
        recycleSearchList = findViewById(R.id.list);
        recipeName = (EditText) findViewById(R.id.search);
        done = (Button) findViewById(R.id.ellipse_ek22);
        back = (ImageButton) findViewById(R.id.backk);
        HomeButton = (ImageButton) findViewById(R.id.home1);
        ProfileIcon = findViewById(R.id.profile1);
        recipesButton = (Button) findViewById(R.id.recipes);
        usersButton = (Button) findViewById(R.id.users) ;

        //display IProfile icon
        //get logged user
        SessionManager session = new SessionManager(this);
        long loggedUser= session.getUserFromSession();
        User user= userController.getUser((int)loggedUser);
        if(user.getAvatar() != null) {
            ProfileIcon.setImageURI(Uri.parse(user.getAvatar()));
        }

        // buttons Functions
        done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                String txtName = recipeName.getText().toString().replaceAll("\\s",""); //remove all spaces
                List<Recipe> recipeInfo = recipeController.SearchRecipe(txtName); //just rectuen id
                if (recipeInfo.isEmpty()) {
                    onError("Recipe Name Doesn't exist");
                }
                else
                {
                    //Recyclerview as GridView for recipes
                    recycleSearchList.setVisibility(View.VISIBLE);
                    for(int i=0; i<recipeInfo.size();i++){
                        Recipe recipe= recipeController.getRecipe(recipeInfo.get(i).getId());
                        recipesIdList.add(recipe.getId());
                        recipeNameList.add(recipe.getName());
                        recipeImageList.add(recipe.getImage());
                    }
                    adapter = new Adapter(cntx, recipesIdList, recipeNameList, recipeImageList);
                    //RecipeId= recipeInfo.get(0).getId();
                    onSuccess("");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Homepage_activity.class);
                startActivity(nextScreen);
            }
        });
        HomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Homepage_activity.class);
                startActivity(nextScreen);
            }
        });
        usersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), SearchUser_activity.class);
                startActivity(nextScreen);
                Toast.makeText(getApplication(),"Now You are On Search User Mode",Toast.LENGTH_LONG).show();

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
    public void onSuccess(String message) {
        /*
        Intent nextScreen = new Intent(getApplicationContext(), RecipeDetails_activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", RecipeId);
        bundle.putString("IProfile","myProfile");
        nextScreen.putExtras(bundle);
        startActivity(nextScreen);*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        recycleSearchList.setLayoutManager(gridLayoutManager);
        recycleSearchList.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void viewRecipeDetails(int id) {
        Intent nextScreen = new Intent(getApplicationContext(), RecipeDetails_activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("IProfile","searchRecipe");
        nextScreen.putExtras(bundle);
        startActivity(nextScreen);

    }
}
