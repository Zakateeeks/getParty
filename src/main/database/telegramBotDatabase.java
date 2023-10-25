import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class telegramBotDatabase {
    public Connection connectToDatabase(String dbname, String user, String pass) {
        Connection conn=null;
        try{
            Class.forName("org.postgresql.Driver");
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname, user, pass);
            if(conn!=null){
                System.out.println("Connection Established");
            }
            else{
                System.out.println("Connection Failed");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }

    public void createTable(Connection conn,String tableName){
        Statement statement;
        try{
            String query = "create table "+tableName+"(empid SERIAL,name varchar(200),role varchar(200),description varchar(200),chatID varchar(200),primary key(empid));";
            statement= conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("table create");
        }catch(Exception e){
            System.out.println("Create Error");
        }
    }

    public void insertRow(Connection conn,String tableName, String role, String chatID){
        Statement statement;
        try{
            String query = String.format("insert into %s(role,chatid) values('%s','%s')",tableName,role,chatID);
            statement= conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row create");
        }catch(Exception e){
            System.out.println("Row create Error");
        }
    }

    public void update(Connection conn,String tableName, String column, String value,String chatID){
        Statement statement;
        try{
            String query = String.format("update %s set %s='%s' where chatid='%s'",tableName,column,value,chatID);
            statement= conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row create");
        }catch(Exception e){
            System.out.println("Row create Error");
        }
    }
    public String searchByChatID(Connection conn,String tableName, String chatID, String searchProduct){
        Statement statement;
        ResultSet rs=null;
        try{
            String query = String.format("select * from %s where chatid= '%s' ", tableName, chatID);
            statement= conn.createStatement();
            rs = statement.executeQuery(query);

            if (rs.next()) {
                return rs.getString(searchProduct);
            }else {
                System.out.println("Пиздец");
                return "11";
            }
        }catch(Exception e){
            return "";
        }
    }


}
