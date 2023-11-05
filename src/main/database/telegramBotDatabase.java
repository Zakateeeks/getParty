import java.sql.*;

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
    public void createTable(Connection conn, String tableName, String tableStructure) {
        String query = "CREATE TABLE ? (?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, tableName);
            preparedStatement.setString(2, tableStructure);

            preparedStatement.executeUpdate();
            System.out.println(tableName + " table created");
        } catch (SQLException e) {
            System.out.println("Create Error: " + e.getMessage());
        }
    }

    public void insertRow(Connection conn, String tableName, String field1, String field2, String value1, String value2 ) {
        PreparedStatement preparedStatement = null;
        try {
            String query = "insert into " + tableName + "("+field1+","+field2+") values (?, ?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, value1);
            preparedStatement.setString(2, value2);
            preparedStatement.executeUpdate();
            System.out.println("Row created");
        } catch (SQLException e) {
            System.out.println("Row create Error: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(Connection conn, String tableName, String column, String value, String chatID) {
        PreparedStatement preparedStatement = null;
        try {
            String query = "update " + tableName + " set " + column + " = ? where chatid = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, chatID);
            preparedStatement.executeUpdate();
            System.out.println("Update OK");
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public String searchByChatID(Connection conn, String tableName, String chatID, String searchProduct) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String query = "SELECT " + searchProduct + " FROM " + tableName + " WHERE chatid = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, chatID);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getString(searchProduct);
            } else {
                System.out.println(":(");
                return "11";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
