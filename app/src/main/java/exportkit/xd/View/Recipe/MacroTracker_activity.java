package exportkit.xd.View.Recipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;

import java.util.ArrayList;

import exportkit.xd.Controller.NutrientsController;
import exportkit.xd.Controller.RecipeController;
import exportkit.xd.Controller.UserController;
import exportkit.xd.DB.SessionManager;
import exportkit.xd.Model.NutrientsFactsRecord;
import exportkit.xd.Model.Recipe;
import exportkit.xd.Model.User;
import exportkit.xd.R;
import exportkit.xd.View.Homepage_activity;
import exportkit.xd.View.IAppViews;
import exportkit.xd.View.Profile.Profile_activity;
import exportkit.xd.View.Scanner.Camera;
import exportkit.xd.View.Search.SearchUser_activity;

public class MacroTracker_activity extends Camera implements IAppViews {

    private CircularImageView ProfileButton;
    private ImageView photo;
    private Button SearchButton, recipeDetailsButton,
            increaseFats, increaseCarbs, increaseProteins,
            decreaseFats, decreaseCarbs, decreaseProteins;
    private ImageButton HomeButton;
    private TextView fats, carbs, protein;

    UserController userController;
    RecipeController recipeController;
    NutrientsController nutrientsController;

    int recipeId;

    public enum QUERY {
        INCREASE_Fats, INCREASE_Carbs, INCREASE_Protein,
        DECREASE_Fats, DECREASE_Carbs, DECREASE_Protein
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macro_tracker);

        userController= new UserController(this);
        recipeController= new RecipeController(this);
        nutrientsController= new NutrientsController(this);

        //get logged user
        SessionManager session= new SessionManager(this);
        long loggedUserID= session.getUserFromSession();
        User user= userController.getUser((int)loggedUserID);

        //retrieve recipe id
        recipeId= getIntent().getExtras().getInt("recipeID");
        String PROFILE_KEY= getIntent().getExtras().getString("IProfile");
        String image= getIntent().getExtras().getString("image");

        //find views
        photo= findViewById(R.id.recipeImage);
        fats= findViewById(R.id.fatsMeasure);
        carbs= findViewById(R.id.carbsMeasure);
        protein= findViewById(R.id.proteinsMeasure);

        increaseFats= findViewById(R.id.increaseFats);
        increaseCarbs= findViewById(R.id.increaseCarbs);
        increaseProteins= findViewById(R.id.increaseProteins);

        decreaseFats= findViewById(R.id.decreaseFats);
        decreaseCarbs= findViewById(R.id.decreaseCarbs);
        decreaseProteins= findViewById(R.id.decreaseProteins);

        recipeDetailsButton= findViewById(R.id.recipeDetailsTap);
        ProfileButton = findViewById(R.id.profile1);
        SearchButton = (Button) findViewById(R.id.search1);
        HomeButton =  findViewById(R.id.home1);


        //set profile icon for logged user
        if(user.getAvatar() != null){
            ProfileButton.setImageURI(Uri.parse(user.getAvatar()));
        }
        if(image.equals("null")) {
            photo.setImageResource(R.drawable.recipeimage);
        }else {
            photo.setImageURI(Uri.parse(image));
        }

        //get recipe nutrients details
        NutrientsFactsRecord facts= recipeController.getRecipeNutrients(recipeId);
        fats.setText(String.valueOf(facts.getFats())+"g");
        carbs.setText(String.valueOf(facts.getCarbs())+"g");
        protein.setText(String.valueOf(facts.getProtein())+"g");

        increaseFats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackMacros(QUERY.INCREASE_Fats);
            }
        });
        decreaseFats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackMacros(QUERY.DECREASE_Fats);
            }
        });
        increaseCarbs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackMacros(QUERY.INCREASE_Carbs);
            }
        });
        decreaseCarbs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackMacros(QUERY.DECREASE_Carbs);
            }
        });
        increaseProteins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackMacros(QUERY.INCREASE_Protein);
            }
        });
        decreaseProteins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackMacros(QUERY.DECREASE_Protein);
            }
        });
        recipeDetailsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), RecipeDetails_activity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", recipeId);
                bundle.putString("IProfile",PROFILE_KEY);
                if(!PROFILE_KEY.equals("myProfile"))
                    bundle.putInt("userId",getIntent().getExtras().getInt("userId"));
                nextScreen.putExtras(bundle);
                startActivity(nextScreen);
            }
        });
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Profile_activity.class);
                startActivity(nextScreen);
            }
        });
        SearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), SearchUser_activity.class);
                startActivity(nextScreen);
            }
        });
        HomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Homepage_activity.class);
                startActivity(nextScreen);

            }
        });
    }

    private void trackMacros(QUERY key){
        //track nutrients for each ingredient
        Recipe recipe= recipeController.getRecipe(recipeId);
        //split ingredients, each ingredient in one index in vector
        ArrayList<String> ingredientsList= readRecipeIngredients(recipe.getIngredients());
        String msg= nutrientsController.trackMacros(String.valueOf(key), readRecipeIngredients(recipe.getIngredients()));
        //display msg
        display(msg);
    }

    private ArrayList<String> readRecipeIngredients(String recipeIngredients){
        ArrayList<String> ingredients= new ArrayList<>();
        String txtIngredient="";

        for(int i=0; i<recipeIngredients.length(); i++){
            if(recipeIngredients.charAt(i)!='\n'){
                txtIngredient+= recipeIngredients.charAt(i);
            }
            else{
                //generate name and amount from txtIngredient-> ex: 1) 50.0 Grams of Ice milk
                String[] split= txtIngredient.split("of");
                ingredients.add(split[1].toLowerCase().replaceAll("\\s",""));

                txtIngredient="";
            }
        }
        return ingredients;
    }

    private void display(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Tips");
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
    public void onSuccess(String message) { }

    @Override
    public void onError(String message) { }
}
