package exportkit.xd.DB;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import exportkit.xd.DB.Constants.RecipeNutrientsTableConstants;
import exportkit.xd.Model.NutrientsFactsRecord;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class NutrientsDBController {

    Context context;
    public RecipeNutrientsTableConstants file = new RecipeNutrientsTableConstants();

    public NutrientsDBController(Context cntx) {
        this.context= cntx;
    }

    private Sheet openExcelSheet(){
        Sheet sheet = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStreams = assetManager.open(file.sheetName);
            Workbook workbook = Workbook.getWorkbook(inputStreams);
            sheet = workbook.getSheet(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        return sheet;
    }

    public ArrayList<NutrientsFactsRecord> getNutrientsInfo(ArrayList<String> ingredients)
    {
        ArrayList<NutrientsFactsRecord> list= new ArrayList<>();

        //open excel sheet
        Sheet sheet= openExcelSheet();

        //get header titles-> first row in sheet
        Cell[] headerRow= sheet.getRow(0);

        //loop on ingredients List
        for(int k=0; k<ingredients.size(); k++) { //loop on my ingredients list
            //convert all letters to lowercase and remove all spaces -> excel format
            String name= (ingredients.get(k)).toLowerCase().replaceAll("\\s","");
            Cell cellFood = sheet.findCell(name); //search on ingredient
            int row = cellFood.getRow(); //get number of row
            Cell[] factsRow = sheet.getRow(row); //read all columns
            list.add(storeFacts(headerRow, factsRow)); //create instance of NutrientsFactsRecord
        }

        return list;
    }

    private NutrientsFactsRecord storeFacts(Cell[] headerRow, Cell[] factsRow){
        NutrientsFactsRecord facts= new NutrientsFactsRecord();
        for(int i=0; i<headerRow.length; i++){
           if(!factsRow[i].getContents().equals("null")) {

               if (headerRow[i].getContents().trim().equals(file.col_FoodName)) {
                   facts.setFoodName(factsRow[i].getContents().trim());

               } else if (headerRow[i].getContents().trim().equals(file.col_Category)) {
                   facts.setCategory(factsRow[i].getContents().trim());

               } else if (headerRow[i].getContents().trim().equals(file.col_Calories)) {
                   facts.setCalories(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Fats)) {
                   facts.setFats(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_SaFats)) {
                   facts.setSaFats(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Protein)) {
                   facts.setProtein(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Carbs)) {
                   facts.setCarbs(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Sugars)) {
                   facts.setSugars(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Cholesterol)) {
                   facts.setCholesterol(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Calcium)) {
                   facts.setCalcium(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Vitamin_A)) {
                   facts.setVitamin_A(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Vitamin_C)) {
                   facts.setVitamin_C(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Vitamin_B6)) {
                   facts.setVitamin_B6(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Vitamin_B12)) {
                   facts.setVitamin_B12(Double.parseDouble(factsRow[i].getContents().trim()));

               } else if (headerRow[i].getContents().trim().equals(file.col_Vitamin_D)) {
                   facts.setVitamin_D(Double.parseDouble(factsRow[i].getContents().trim()));
               }
           }
        }

        return facts;
    }
}
