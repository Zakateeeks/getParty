import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Класс отвечает за проверку данных для базы данных
 *
 * @author Комогоров Виктор
 */
public class dataVerification {
    public Map<String,String>  literal=new HashMap<>();

    public dataVerification(){
        literal.put("name","[^'\";]+");
        literal.put("role",".+");
        literal.put("countevent",".+");
        literal.put("description","[^'\";]+");
        literal.put("chatid","\\d+");
        literal.put("organizer","[^'\";]+");
        literal.put("eventname","[^'\";]+");
        literal.put("eventdesc","[^'\";]+");
        literal.put("eventref","[t]\\.[m][e]\\/\\+\\w+");
        literal.put("eventdate","([0-3]?[0-9])\\.([0-1]?[0-9])\\.([0-9]{4})\\s([0-2]?[0-9])\\:([0-6]?[0-9])");


    }

    /**
     * Проверка данных для БД с помощью шаблонов
     *
     *@param column Поле в которое планируется добавить информацию.
     *
     * @param value Значение, которое планируется добавить в данное поле.
     */
    public void verify(String column, String value) throws InvalidDatabaseEntryException{
        if (!(Pattern.matches(literal.get(column),value))){
            throw new InvalidDatabaseEntryException("Данные не корректны");
        }
    }
}
