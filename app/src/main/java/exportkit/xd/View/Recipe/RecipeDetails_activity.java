package exportkit.xd.View.Recipe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;

import java.util.Vector;

import exportkit.xd.Controller.RecipeController;
import exportkit.xd.Controller.UserController;
import exportkit.xd.DB.Constants.RecipeNutrientsTableConstants;
import exportkit.xd.DB.SessionManager;
import exportkit.xd.Model.NutrientsFactsRecord;
import exportkit.xd.Model.Recipe;
import exportkit.xd.Model.User;
import exportkit.xd.R;
import exportkit.xd.View.Homepage_activity;
import exportkit.xd.View.IAppViews;
import exportkit.xd.View.Profile.Profile_activity;
import exportkit.xd.View.Profile.UserProfile_activity;
import exportkit.xd.View.Search.SearchRecipe_activity;
import exportkit.xd.View.Search.SearchUser_activity;

public class RecipeDetails_activity extends Activity implements IAppViews {
    RecipeController recipeController;
    UserController userController;
    ImageView image;
    TextView name, description, ingredient;
    private ImageButton editButton, HomeButton, backButton, favButton;
    private Button SearchButton, MacroTracker, nutrientsTable;
    private CircularImageView ProfileIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipe_details);

        recipeController = new RecipeController(this);
        userController = new UserController(this);

        // get logged user
        SessionManager session = new SessionManager(this);
        long loggedUser= session.getUserFromSession();
        User user= userController.getUser((int)loggedUser);

        //retrieve recipe id
        int recipeId= getIntent().getExtras().getInt("id");
        String PROFILE_KEY= getIntent().getExtras().getString("IProfile");

        // finds views
        image= findViewById(R.id.food_picture);
        name= findViewById(R.id.foodName);
        description= findViewById(R.id.getDescription);
        ingredient= findViewById(R.id.ingredients);
        nutrientsTable= findViewById(R.id.displayNutrients);

        backButton = findViewById(R.id.back);
        editButton= findViewById(R.id.edit);
        favButton= findViewById(R.id.favorite);
        MacroTracker= findViewById(R.id.macroTrackerTap);
        SearchButton= findViewById(R.id.ellipse_ek22);
        HomeButton= findViewById(R.id.home_ek11);
        ProfileIcon= findViewById(R.id.profile1);

        //display IProfile icon
        if(user.getAvatar() != null) {
            ProfileIcon.setImageURI(Uri.parse(user.getAvatar()));
        }
        //if current logged user IProfile -> already show delete icon
        //if another user IProfile -> is it in my favList -> show favButton; else show star icon to can add it to favList
        if(!PROFILE_KEY.equals("myProfile")){
            Vector<Integer> favList=  recipeController.viewFavList((int)loggedUser);
            if(favList.contains(recipeId)){ //already added into favList
                editButton.setVisibility(View.GONE);
                favButton.setVisibility(View.VISIBLE);
            }else
                editButton.setImageResource(R.drawable.star_1);
        }
        if(PROFILE_KEY.equals("searchRecipe"))
            editButton.setVisibility(View.GONE); //no delete no favList

        //get recipe info from db
        Recipe recipe= recipeController.getRecipe(recipeId);

        //display info
        if(recipe.getImage().equals("null")) {
            image.setImageResource(R.drawable.recipeimage);
        }else {
            image.setImageURI(Uri.parse(recipe.getImage()));
        }
        name.setText(recipe.getName());
        description.setText(recipe.getDescription());
        ingredient.setText(recipe.getIngredients());

        //buttons
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(PROFILE_KEY.equals("myProfile")) //delete recipe
                    recipeController.deleteRecipe(recipeId);

                else {//add to favoriteList
                    long id= recipeController.addToFavList((int) loggedUser, recipeId);
                    if(id>0) {
                        editButton.setVisibility(View.GONE);
                        favButton.setVisibility(View.VISIBLE);
                    }else
                        onError("FAILED!!");
                }
            }
        });
        favButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) { //remove from favorite List
                recipeController.unFavRecipe((int) loggedUser,recipeId);
                favButton.setVisibility(View.GONE);
                editButton.setImageResource(R.drawable.star_1);
                editButton.setVisibility(View.VISIBLE);
            }
        });
        nutrientsTable.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //get facts information
                NutrientsFactsRecord facts= recipeController.getRecipeNutrients(recipe.getNutrientsID());
                //convert it to String
                RecipeNutrientsTableConstants cnst= new RecipeNutrientsTableConstants();
                String str="";
                str= cnst.col_Calories+": "+facts.getCalories()+"\n";
                str+= "\n"+cnst.col_Protein+": "+facts.getProtein()+"g\n";
                str+= "\n"+cnst.col_Carbs+": "+facts.getCarbs()+"g\n";
                if(facts.getSugars()!=0) str+= "\t\t\t"+cnst.col_Sugars+": "+facts.getSugars()+"g\n";
                str+= "\n"+cnst.col_Fats+": "+facts.getFats()+"g\n";
                if(facts.getSaFats()!=0) str+= "\t\t\t"+cnst.col_SaFats+": "+facts.getSaFats()+"g\n";
                if(facts.getCholesterol()!=0) str+= "\n"+cnst.col_Cholesterol+": "+facts.getCholesterol()+"mg\n";
                if(facts.getCalcium()!=0) str+= "\n"+cnst.col_Calcium+": "+facts.getCalcium()+"%\n";
                if(facts.getVitamin_A()!=0) str+= "\n"+cnst.col_Vitamin_A+": "+facts.getVitamin_A()+"%\n";
                if(facts.getVitamin_C()!=0) str+= "\n"+cnst.col_Vitamin_C+": "+facts.getVitamin_C()+"%\n";
                if(facts.getVitamin_D()!=0) str+= "\n"+cnst.col_Vitamin_D+": "+facts.getVitamin_D()+"%\n";
                if(facts.getVitamin_B6()!=0) str+= "\n"+cnst.col_Vitamin_B6+": "+facts.getVitamin_B6()+"%\n";
                if(facts.getVitamin_B12()!=0) str+= "\n"+cnst.col_Vitamin_B12+": "+facts.getVitamin_B12()+"%";

                display("Nutrition Facts", str);
            }
        });
        MacroTracker.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) { //remove from favorite List
                Intent nextScreen = new Intent(getApplicationContext(), MacroTracker_activity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("recipeID",recipeId);
                bundle.putString("IProfile", PROFILE_KEY);
                if(!PROFILE_KEY.equals("myProfile"))
                    bundle.putInt("userId",getIntent().getExtras().getInt("userId"));
                bundle.putString("image",recipe.getImage());
                nextScreen.putExtras(bundle);
                startActivity(nextScreen);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen;
                if(PROFILE_KEY.equals("myProfile"))
                    nextScreen= new Intent(getApplicationContext(), Profile_activity.class);
                else if(PROFILE_KEY.equals("searchRecipe"))
                    nextScreen= new Intent(getApplicationContext(), SearchRecipe_activity.class);
                else {
                    nextScreen = new Intent(getApplicationContext(), UserProfile_activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",getIntent().getExtras().getInt("userId"));
                    nextScreen.putExtras(bundle);
                }
                startActivity(nextScreen);
            }
        });
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


    private void display(String title, String msg){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.create().show();
    }
    //----------------------------------------------------------------------------------------------
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
