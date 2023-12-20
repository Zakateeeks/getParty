/**
 * Класс отвечает за исключение, вызыввемое при некорректном вводе данных
 *
 * @author Комогоров Виктор
 */
public class InvalidDatabaseEntryException extends Exception {
    public InvalidDatabaseEntryException (String message){
        super(message);
    }
}
