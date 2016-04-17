/**
 * Created by Kvazar on 14.03.2016.
 */
public class Main {

    // JDBC є "мостом" між Java програмою та базою даних. Завдяки йому забезпечується потік даних з програми у базу та навпаки.
    public static void main(String[] args) {
        PersonDB db = new PersonDB("jdbc:mysql://localhost/logos", "root",
                "123456");
//		db.createTablePerson();
//		db.insertPersonInTable("Taras", 31);

//		for (Person person : db.findAllPersons()) {
//			System.out.println(person);
//		}

        for (Person person : db.findByAgeBetween(25, 15)) {
            System.out.println(person);
        }
        db.closeConnection();
    }
}