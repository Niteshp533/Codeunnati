package exportkit.xd.DB.Constants;

public class RecipeNutrientsTableConstants {
    //sheet name
    public static final String sheetName= "NutritionFacts.xls";
    //standard measure= 100g, unit: gram
    public final int servingSize=100;
    //Table name
    public static final String DB_Table = "RecipeNutrientsFacts" ;
    //Table Columns names
    public static final String DB_col_ID = "ID" ,
            col_FoodName= "FoodName",
            col_Category= "Category",
            col_Cholesterol ="Cholesterol",
            col_Calcium ="Calcium",
            col_Vitamin_A ="Vitamin_A",
            col_Vitamin_C ="Vitamin_C",
            col_Vitamin_B12 ="Vitamin_B12",
            col_Vitamin_B6 ="Vitamin_B6",
            col_Vitamin_D ="Vitamin_D",
            col_Calories ="Calories",
            col_Fats ="Fats",
            col_SaFats ="SaFats",
            col_Protein ="Protein",
            col_Carbs ="Carbs",
            col_Sugars ="Sugars";

    public String CREATE_TABLE = "CREATE TABLE "
            + DB_Table + "("
            + DB_col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + col_Calories + " INTEGER, "
            + col_Protein + " INTEGER, "
            + col_Carbs + " INTEGER, "
            + col_Fats + " INTEGER, "
            + col_SaFats + " INTEGER, "
            + col_Sugars + " INTEGER, "
            + col_Cholesterol + " INTEGER, "
            + col_Calcium + " INTEGER, "
            + col_Vitamin_A + " INTEGER, "
            + col_Vitamin_C + " INTEGER, "
            + col_Vitamin_B6 + " INTEGER, "
            + col_Vitamin_B12 + " INTEGER, "
            + col_Vitamin_D + " INTEGER)";

    // drop table sql query
    public String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_Table;
}
