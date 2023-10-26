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

    public void createTableUsers(Connection conn){
        Statement statement;
        try{
            String query = "create table users (empid SERIAL,name varchar(200),role varchar(200),description varchar(200),chatID varchar(200),primary key(empid));";
            statement= conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("table create");
        }catch(Exception e){
            System.out.println("Create Error");
        }
    }

    public void createTableEvent(Connection conn){
        Statement statement;
        try{
            String query = "create table event (empid SERIAL,organizer varchar(200),eventname varchar(200),eventdesc varchar(200),chatID varchar(200),eventref varchar(200),eventdate varchar(200),id varchar(200),primary key(empid));";
            statement= conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("table create");
        }catch(Exception e){
            System.out.println("Create Error");
        }
    }

    public void insertRowUsers(Connection conn,String tableName, String role, String chatID){
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

    public void insertRowEvent(Connection conn,String tableName, String organizer, String chatID){
        Statement statement;
        try{
            String query = String.format("insert into %s(organizer,chatid) values('%s','%s')",tableName,organizer,chatID);
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
            System.out.println("update ok");
        }catch(Exception e){
            System.out.println("update Error");
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
