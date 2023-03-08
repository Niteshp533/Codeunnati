package exportkit.xd.Model;

//Excel Sheet
public class NutrientsFactsRecord {
    String FoodName="", Category="";
    double Cholesterol=0, Calcium=0, Vitamin_A=0, Vitamin_C=0,
            Vitamin_B12=0, Vitamin_B6=0, Vitamin_D=0,
            Calories=0, Fats=0, SaFats=0, Protein=0, Carbs=0, Sugars=0;

    public NutrientsFactsRecord(){}

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public double getCholesterol() {
        return Cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        Cholesterol = cholesterol;
    }

    public double getCalcium() {
        return Calcium;
    }

    public void setCalcium(double calcium) {
        Calcium = calcium;
    }

    public double getVitamin_A() {
        return Vitamin_A;
    }

    public void setVitamin_A(double vitamin_A) {
        Vitamin_A = vitamin_A;
    }

    public double getVitamin_C() {
        return Vitamin_C;
    }

    public void setVitamin_C(double vitamin_C) {
        Vitamin_C = vitamin_C;
    }

    public double getVitamin_B12() {
        return Vitamin_B12;
    }

    public void setVitamin_B12(double vitamin_B12) {
        Vitamin_B12 = vitamin_B12;
    }

    public double getVitamin_B6() {
        return Vitamin_B6;
    }

    public void setVitamin_B6(double vitamin_B6) {
        Vitamin_B6 = vitamin_B6;
    }

    public double getVitamin_D() {
        return Vitamin_D;
    }

    public void setVitamin_D(double vitamin_D) {
        Vitamin_D = vitamin_D;
    }

    public double getCalories() {
        return Calories;
    }

    public void setCalories(double calories) {
        Calories = calories;
    }

    public double getFats() {
        return Fats;
    }

    public void setFats(double fats) {
        Fats = fats;
    }

    public double getSaFats() {
        return SaFats;
    }

    public void setSaFats(double saFats) {
        SaFats = saFats;
    }

    public double getProtein() {
        return Protein;
    }

    public void setProtein(double protein) {
        Protein = protein;
    }

    public double getCarbs() {
        return Carbs;
    }

    public void setCarbs(double carbs) {
        Carbs = carbs;
    }

    public double getSugars() {
        return Sugars;
    }

    public void setSugars(double sugars) {
        Sugars = sugars;
    }
}
