package exportkit.xd.DB.Constants;

public class UserTableConstants {
    // User table name
    public static final String DB_User_Table = "user" ;
    // User Table Columns names
    public static final String DB_col_ID = "ID" ;
    public static final String DB_col_name = "FullName" ;
    public static final String DB_col_username = "UserName" ;
    public static final String DB_col_email = "Email" ;
    public static final String DB_col_password = "Password" ;
    public static final String DB_col_gender = "gender" ;
    public static final String DB_col_phoneNumber = "PhoneNumber" ;
    public static final String DB_col_IMAGE = "IMAGE" ;

    // create table sql query
    public String CREATE_USER_TABLE = "CREATE TABLE "
            + DB_User_Table + "("
            + DB_col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DB_col_name + " TEXT, "
            + DB_col_username + " TEXT UNIQUE, "
            + DB_col_email + " TEXT UNIQUE, "
            + DB_col_password + " TEXT, "
            + DB_col_gender + " TEXT, "
            + DB_col_phoneNumber + " INTEGER, "
            + DB_col_IMAGE + " TEXT "
            +")";
    // drop table sql query
    public String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + DB_User_Table;
}
