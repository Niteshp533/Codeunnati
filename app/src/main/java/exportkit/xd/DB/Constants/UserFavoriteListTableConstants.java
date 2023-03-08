package exportkit.xd.DB.Constants;

public class UserFavoriteListTableConstants {
    //Table name
    public static final String DB_Table = "UserFavoriteList" ;
    //Table Columns names
    public static final String DB_col_ID = "ID" ;
    public static final String DB_col_UserID = "USERID" ;
    public static final String DB_col_RecipeID = "RECIPEID" ;

    public String CREATE_TABLE = "CREATE TABLE "
            + DB_Table + "("
            + DB_col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DB_col_UserID + " INTEGER, "
            + DB_col_RecipeID + " INTEGER " +")";

    // drop table sql query
    public String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_Table;
}
