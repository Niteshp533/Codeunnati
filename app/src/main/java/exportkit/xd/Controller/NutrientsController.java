package exportkit.xd.Controller;

import android.content.Context;

import java.text.DecimalFormat;
import java.util.ArrayList;

import exportkit.xd.DB.NutrientsDBController;
import exportkit.xd.Model.NutrientsFactsRecord;
import exportkit.xd.View.Recipe.Ingredient;

public class NutrientsController {
    NutrientsDBController db;

    public NutrientsController(Context cntx) {
        db= new NutrientsDBController(cntx);
    }

    public NutrientsFactsRecord calculateNutrients(ArrayList<Ingredient> ingredients){
        NutrientsFactsRecord recipeFacts= new NutrientsFactsRecord();

        //1. get nutrition info foreach ingredient
        ArrayList<String>nameList= new ArrayList<>();
        for(int i=0; i<ingredients.size(); i++) { nameList.add(ingredients.get(i).name); }
        ArrayList<NutrientsFactsRecord> listInfo= db.getNutrientsInfo(nameList);

        //2. loop on list and start to calculate -> summation
        for(int i=0; i<listInfo.size(); i++){
            //1. get recipe amount for current ingredient
            double amount= ingredients.get(indexOf(ingredients, listInfo.get(i).getFoodName())).amount;

            //2. read facts for current ingredient
            NutrientsFactsRecord record= listInfo.get(i);

            //3. based on amount and facts start to calculate -> amount*factAmount/servingSize
            recipeFacts.setCalories(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getCalories()+(amount*record.getCalories()/db.file.servingSize))
            ));//calories
            recipeFacts.setFats(Double.parseDouble(new DecimalFormat("#.0#").format(
                    recipeFacts.getFats()+(amount*record.getFats()/db.file.servingSize))
            )); //fats
            recipeFacts.setCarbs(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getCarbs()+(amount*record.getCarbs()/db.file.servingSize))
            )); //carbs
            recipeFacts.setProtein(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getProtein()+(amount*record.getProtein()/db.file.servingSize))
            )); //protein
            recipeFacts.setSaFats(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getSaFats()+(amount*record.getSaFats()/db.file.servingSize))
            )); //satFat
            recipeFacts.setSugars(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getSugars()+(amount*record.getSugars()/db.file.servingSize))
            )); //sugars
            recipeFacts.setCholesterol(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getCholesterol()+(amount*record.getCholesterol()/db.file.servingSize))
            )); //Cholesterol
            recipeFacts.setCalcium(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getCalcium()+(amount*record.getCalcium()/db.file.servingSize))
            )); //Calcium
            recipeFacts.setVitamin_A(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getVitamin_A()+(amount*record.getVitamin_A()/db.file.servingSize))
            )); //vitamin A
            recipeFacts.setVitamin_C(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getVitamin_C()+(amount*record.getVitamin_C()/db.file.servingSize))
            )); //vitamin C
            recipeFacts.setVitamin_B6(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getVitamin_B6()+(amount*record.getVitamin_B6()/db.file.servingSize))
            )); //vitamin B6
            recipeFacts.setVitamin_B12(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getVitamin_B12()+(amount*record.getVitamin_B12()/db.file.servingSize))
            )); //vitamin B12
            recipeFacts.setVitamin_D(Double.parseDouble( new DecimalFormat("#.0#").format(
                    recipeFacts.getVitamin_D()+(amount*record.getVitamin_D()/db.file.servingSize))
            )); //Vitamin D
        }
/*
        //2. loop on map and start to calculate based on amount
        for(Map.Entry<String, ArrayList<Ingredient>> idx: ingredientsInfo.entrySet()){
            //1. get recipe amount for current ingredient
            double amount= ingredients.get(indexOf(ingredients, idx.getKey())).amount;
            //2. read facts for current ingredient
            ArrayList<Ingredient> info= idx.getValue();
            //3. based on amount and facts start to calculate
            for(int i=0; i<info.size(); i++){
                double temp= amount * info.get(i).amount / db.servingSize;
                int index= indexOf(facts, info.get(i).name); //which fact, carbs? fats? ans so on
                Ingredient item= facts.get(index);
                item.amount+= temp; //contain facts for whole recipe not foreach ingredient
                facts.set(index, item);
            }
        }
        */

        /*
        System.out.println("--------------------------------------------------------");
        for(int i=0; i<facts.size(); i++)
            System.out.println(facts.get(i).name+" "+facts.get(i).amount);
        */

        return recipeFacts;
    }

    public String trackMacros(String queryStr, ArrayList<String> list){ //list contain ingredients name
        String Tips="";
        //get query input, what user want to track
        String[] query= String.valueOf(queryStr).split("_"); //[0]:increase/decrease
        //read info for each ingredient
        ArrayList<NutrientsFactsRecord> listInfo= db.getNutrientsInfo(list); //contain facts foreach ingredient
        //select which one to track
        String ingredient1="", ingredient2="";
        double target1=0, target2=0;
        for(int i=0; i<listInfo.size(); i++){
            NutrientsFactsRecord ingr= listInfo.get(i);

            //gat value
            double value=0;
            if(query[1].equals("Fats")){ value= ingr.getFats();}
            else if (query[1].equals("Carbs")){ value= ingr.getCarbs();}
            else if((query[1].equals("Protein"))){ value= ingr.getProtein();}

            //search on two ingredients have highest value for this facts
            if(value>target1) {
                //shift target1 to target22
                if(target1>target2) {
                    target2 = target1;
                    ingredient2 = ingredient1;
                }
                target1 = value;
                ingredient1= ingr.getFoodName();

            }else if(value>target2) {
                target2 = value;
                ingredient2 = ingr.getFoodName();
            }
        }

        //to track
        Tips="1. 100g of "+ingredient1+" has "+target1+"g "+query[1]+",\n";
        if(target2!=0)
            Tips+="\n2. 100g of "+ingredient2+" has "+target2+"g "+query[1]+",\n";

        if(query[0].equals("INCREASE")){
            Tips+= "\nSo you can Add more of it to the recipe\n";
        }
        else if(query[0].equals("DECREASE")) {
            Tips+= "\nYou should reduce amount of it in the recipe\n";
        }

        return Tips;
    }

    //----------------------------------------------------------------------------------------------
    private int indexOf(ArrayList<Ingredient> list, String name){
        int idx=-1;
        for (int i=0; i<list.size(); i++){
            if(list.get(i).name.toLowerCase().replaceAll("\\s","").equals(name)){
                idx=i;
                break;
            }
        }

        return idx;
    }
}