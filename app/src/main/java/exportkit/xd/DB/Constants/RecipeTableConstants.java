package exportkit.xd.DB.Constants;

public class RecipeTableConstants {
    // Recipe table name
    public static final String DB_Table = "Recipe" ;
    // Recipe Table Columns names
    public static final String DB_col_ID = "ID" ;
    public static final String DB_col_IMAGE = "IMAGE" ;
    public static final String DB_col_NAME = "NAME" ;
    public static final String DB_col_DESCRIPTION = "DESCRIPTION" ;
    public static final String DB_col_INGREDIENTS = "INGREDIENTS" ;
    public static final String DB_col_NUTRIENTSID = "NUTRIENTSID" ;
    public static final String DB_col_USERID = "USERID" ;

    public String CREATE_RECIPE_TABLE = "CREATE TABLE "
            + DB_Table + "("
            + DB_col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DB_col_IMAGE + " TEXT, "
            + DB_col_NAME + " TEXT, "
            + DB_col_DESCRIPTION + " TEXT, "
            + DB_col_INGREDIENTS + " TEXT, "
            + DB_col_NUTRIENTSID+ " INTEGER, "
            + DB_col_USERID + " INTEGER)";

    // drop table sql query
    public String DROP_RECIPE_TABLE = "DROP TABLE IF EXISTS " + DB_Table;
}
