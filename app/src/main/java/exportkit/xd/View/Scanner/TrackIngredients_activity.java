package exportkit.xd.View.Scanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;

import java.util.ArrayList;

import exportkit.xd.Controller.NutrientsController;
import exportkit.xd.Controller.UserController;
import exportkit.xd.DB.Constants.RecipeNutrientsTableConstants;
import exportkit.xd.DB.SessionManager;
import exportkit.xd.Model.NutrientsFactsRecord;
import exportkit.xd.Model.User;
import exportkit.xd.R;
import exportkit.xd.View.Adapter_ingr;
import exportkit.xd.View.Homepage_activity;
import exportkit.xd.View.IAppViews;
import exportkit.xd.View.Profile.Profile_activity;
import exportkit.xd.View.Recipe.Ingredient;
import exportkit.xd.View.Search.SearchUser_activity;

public class TrackIngredients_activity extends Activity implements IAppViews {

    String[] ingredients;

    RecyclerView recycleList;
    CircularImageView  ProfileIcon;
    ImageButton HomeButton;
    Button SearchButton;

    Adapter_ingr adapter;
    UserController userController;
    NutrientsController nutrientsController;
    RecipeNutrientsTableConstants cnst= new RecipeNutrientsTableConstants();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients);

        //retrieve ingredients list
        String list= getIntent().getExtras().getString("list"); //[]
        list=list.substring(1,list.length()-1);

        //split ingredients
        ingredients= list.split(",");

        userController = new UserController(this);
        nutrientsController = new NutrientsController(this);

        // finds views
        recycleList = findViewById(R.id.ingredient_);
        SearchButton = (Button) findViewById(R.id.search1);
        HomeButton = (ImageButton) findViewById(R.id.home1);
        ProfileIcon = findViewById(R.id.profile1);

        // get logged user
        SessionManager session = new SessionManager(this);
        long loggedUser= session.getUserFromSession();
        User user= userController.getUser((int)loggedUser);

        //display IProfile info
        if(user.getAvatar() != null) {
            ProfileIcon.setImageURI(Uri.parse(user.getAvatar()));
        }

        //Recyclerview as GridView for recipes
        adapter = new Adapter_ingr(this, ingredients);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        recycleList.setLayoutManager(gridLayoutManager);
        recycleList.setAdapter(adapter);


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

    public void viewFacts(String s) {
        ArrayList<Ingredient> list= new ArrayList<>();
        Ingredient ingrd= new Ingredient();
        ingrd.name=s; ingrd.amount=cnst.servingSize;
        list.add(ingrd);
        NutrientsFactsRecord facts= nutrientsController.calculateNutrients(list);

        //convert it to String
        String str="100g of "+s+":\n\n" ;
        str+= cnst.col_Calories+": "+facts.getCalories()+"\n";
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

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onError(String message) {

    }
}
