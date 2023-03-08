package exportkit.xd.View.Recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import exportkit.xd.Controller.CameraController;
import exportkit.xd.Controller.NutrientsController;
import exportkit.xd.Controller.RecipeController;
import exportkit.xd.DB.SessionManager;
import exportkit.xd.Model.NutrientsFactsRecord;
import exportkit.xd.Model.Recipe;
import exportkit.xd.R;
import exportkit.xd.View.Scanner.Camera;
import exportkit.xd.View.Homepage_activity;
import exportkit.xd.View.IAppViews;
import exportkit.xd.View.Profile.Profile_activity;

public class AddRecipe_activity extends Camera implements IAppViews {
    //dynamic view
    LinearLayout ingredients_layoutList;
    Button dynamicAddBtn;

    //variables
    ArrayList<Ingredient> ingredientsList = new ArrayList<>();

    private TextView name, description;
    private ImageButton saveBtn ;
    private Button cancelBtn;

    RecipeController recipeController;
    NutrientsController nutrientsController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        recipeController = new RecipeController(this);
        cameraController = new CameraController(this);
        nutrientsController= new NutrientsController(this);

        //dynamic view
        dynamicAddBtn = findViewById(R.id.addIngredientBtn);
        ingredients_layoutList = findViewById(R.id.dyanamicLinearLayout);

        //get logged user
        SessionManager session= new SessionManager(this);
        int loggedUser = (int) session.getUserFromSession();

        //find views
        uploadedImage= findViewById(R.id.uploadImage);
        name= findViewById(R.id.enter_food_name);
        description= findViewById(R.id.enter_description);
        saveBtn = findViewById(R.id.done);
        cancelBtn= findViewById(R.id.cancel);

        uploadedImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //show image pick dialog
                cameraController.imagePickDialog();
            }
        });

        dynamicAddBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addView();
            };
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String recipeName= name.getText().toString().trim(),
                        recipeDescription= ""+description.getText().toString().trim();

                if (recipeName.equalsIgnoreCase(""))//|| recipeIngredients.equals(""))
                    Toast.makeText(getApplication(),"you should fill food name field",Toast.LENGTH_LONG).show();
                else
                {
                    if(readDynamicView()){
                        //read ingredientsList as a one string
                        String all_ingredients="";
                        for(int i=0; i<ingredientsList.size(); i++) {
                            String name= ingredientsList.get(i).name;
                            String amount= String.valueOf(ingredientsList.get(i).amount);
                            String record= String.valueOf(i+1)+") "+amount+" Grams of "+name+"\n";
                            all_ingredients+=record;
                        }
                        //calculate Nutrients Facts for the recipe then insert it
                        NutrientsFactsRecord facts= nutrientsController.calculateNutrients(ingredientsList);
                        long nutrientsID= recipeController.addRecipeNutrients(facts);

                        //save recipe with all information in DB
                        Recipe recipe= new Recipe(loggedUser);
                        recipe.setImage(""+ cameraController.imageUri);
                        recipe.setName(recipeName);
                        recipe.setDescription(recipeDescription);
                        recipe.setIngredients(all_ingredients);
                        recipe.setNutrientsID((int)nutrientsID);
                        recipeController.addRecipe(recipe);
                    }
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Homepage_activity.class);
                startActivity(nextScreen);
            }
        });
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

    //----------------------------------------------------------------------------------------------

    private void addView() {
         View view = getLayoutInflater().inflate(R.layout.hidden, null, false);

         ImageView imageClose = (ImageView)view.findViewById(R.id.image_remove);
         imageClose.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   removeView(view);
               }
         });

         ingredients_layoutList.addView(view);
    }

    private void removeView(View view){
            ingredients_layoutList.removeView(view);
    }

    private boolean readDynamicView() {
        ingredientsList.clear();
        boolean result = true;

        for(int i=0;i<ingredients_layoutList.getChildCount();i++){

            View view = ingredients_layoutList.getChildAt(i);

            EditText TextName= (EditText)view.findViewById(R.id.name),
                    TextAmount= (EditText)view.findViewById(R.id.amount);

            Ingredient ingredient = new Ingredient();

            if(!TextName.getText().toString().equals("") && !TextAmount.getText().toString().equals("")){
                ingredient.name= TextName.getText().toString().trim();
                ingredient.amount= Double.parseDouble(TextAmount.getText().toString().trim());
            }
            else{
                result = false;
                break;
            }
            ingredientsList.add(ingredient);
        }

        if(ingredientsList.size()==0){
            result = false;
            Toast.makeText(this, "Add Ingredient First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }
}
