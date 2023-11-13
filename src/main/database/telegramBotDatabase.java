import java.sql.*;

/**
 * Класс отвечает за работу с базой данных
 *
 * @author Цымбал Александр
 */
public class telegramBotDatabase {

    /**
     * Коннект с базой данных
     */
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

    /**
     * Создает новую таблицу в базе данных с указанным именем.
     *
     * @param tableName Имя таблицы, которую нужно создать.
     * @param tableStructure Струкутра таблицы, задающая поля и их размеры
     */
    public void createTable(Connection conn, String tableName, String tableStructure) {
        String query = "CREATE TABLE "+tableName+" ("+tableStructure+")";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            ps.close();
            System.out.println(tableName + " table created");
        } catch (SQLException e) {
            System.out.println("Create Error: " + e.getMessage());
        }
    }

    /**
     * Создает строку в таблице с указанным именем.
     *
     * @param tableName Имя таблицы, в которую нужно добавить строку.
     * @param field1 Имя поля в которые нужно добавить информаиию.
     * @param value1 Информация которую нуцжно добавить в поле.
     */
    public void insertRow(Connection conn, String tableName, String field1, String field2, String value1, String value2 ) {
        PreparedStatement preparedStatement = null;
        dataVerification verification = new dataVerification();
        try {
            verification.verify(field1,value1);
            verification.verify(field2,value2);

            String query = "insert into " + tableName + "("+field1+","+field2+") values (?, ?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, value1);
            preparedStatement.setString(2, value2);
            preparedStatement.executeUpdate();
            System.out.println("Row created");
        }catch (InvalidDatabaseEntryException e){
            System.out.println(e.getMessage());
        }
        catch (SQLException e) {
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

    /**
     * Добавляет инофрмацию в указаное поле с заданным chatid
     *
     * @param tableName Имя таблицы, в которую нужно добавить информацию.
     * @param column Поле в которое нужно добавить информацию.
     * @param value Значение, которое будет добавлено в данное поле
     */
    public void update(Connection conn, String tableName, String column, String value, String chatID) throws InvalidDatabaseEntryException {
        PreparedStatement preparedStatement = null;
        dataVerification verification = new dataVerification();
        try {
            verification.verify(column,value);
            String query;
            if (tableName.equals("users")) {
                query = "update " + tableName + " set " + column + " = ? where chatid = ?";
            } else {
                query = "update " + tableName + " set " + column + " = ? where chatid = ? and empid = (select max(empid) from " + tableName + " where chatid = ?)";
            }
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, chatID);
            preparedStatement.setString(3, chatID);
            preparedStatement.executeUpdate();
            System.out.println("Update OK");
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        } catch (InvalidDatabaseEntryException e){
            System.out.println(e.getMessage());
            throw new InvalidDatabaseEntryException("Данные не корректны");
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

    /**
     * Поиск информации в указанном поле с заданным chatid
     *
     * @param tableName Имя таблицы, в которую нужно добавить информацию.
     * @param searchProduct Поле, в котором ищется инофрмация.
     */
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

    public String[] viewRow(Connection conn, String tableName, int empid) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM " + tableName + " WHERE empid = CAST(? AS INTEGER)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, Integer.toString(empid));
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)};
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[]{};
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
        return null;
    }

    public int countRow(Connection conn, String tableName){
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        try {
            String query = "SELECT COUNT(*) FROM "+tableName;
            preparedStatement = conn.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
