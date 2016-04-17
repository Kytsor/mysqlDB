/**
 * Created by Kvazar on 14.03.2016.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDB {
    // Connection - основний клас для роботи з JDBC. Забезбечує зв'язок програми
    // з базою даних та надає усі необхідні методи для роботи з базою.
    private Connection connection;
    // PreparedStatement - звертання до бази даних у вигляді запиту, у який
    // можна передавати вхідні параметри.
    private PreparedStatement ps;

    // Оскільки Connection є потоком, то при завершенні роботи його варто
    // закривати.
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Для створення об'єкту Connection необхідно передати адресу бази даних
    // (url), логін та пароль користувача сервера бази даних.
    public PersonDB(String url, String user, String password) {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("exception");
        }
    }

    // JDBC забезпечує "спілкування" з базою даних завдями Statement`ам.
    // Statement та PreparedStatement використовують SQL запити,
    // CallableStatement викликає збережені в БД процедури.
    public void createTablePerson() {
        try {
            ps = connection
                    .prepareStatement("create table if not exists person (id int not null auto_increment, name varchar(45), age int, primary key(id))");
            ps.execute(); // метод execute() передає написаний запит у базу
            // даних. Без нього звернення не відбудеться.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Головною перевагою PreparedStatement є можливість вводити вхідні
    // параметри в запит.
    public Person insertPersonInTable(String name, int age) {
        Person person = new Person(name, age);
        try {
            // Вхідні параметри позначаються як ?
            ps = connection
                    .prepareStatement("insert into person(name, age) values (?, ?)");
            // Завдяки методам set<тип даних> передаються вхідні параметри у
            // запит.
            // Ці методи мають два вхідні параметри: номер знаку питання у
            // запиті(відлік з 1, а не з 0!) та власне значення, яке потрібно
            // передати.
            ps.setInt(2, age);
            ps.setString(1, name);
            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return person;
    }

    public List<Person> findAllPersons() {
        List<Person> persons = new ArrayList<Person>();
        ResultSet results = null;
        try {
            ps = connection.prepareStatement("select * from person");
            // Метод executeQuery повертає результат виконаного запиту у викляді
            // ResultSet.
            // ResultSet - це об'єкт у вигляді ітератора, у який записується
            // результат select запиту. Рядки результуючої таблиці є елементами
            // цього ітератора.
            results = ps.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            while (results.next()) {
                Person p;
                // Завдяки методам get<тип даних> отримуються значення, записані
                // у поточний рядок, відповідного до вказаного стовпця.
                // Вказаний стовпець можна передати параметром у цей метод або
                // назвою, або його порядковим номером у результуючій таблиці.
                p = new Person(results.getString("name"), results.getInt("age"));
                p.setId(results.getInt("id"));
                persons.add(p);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return persons;
    }

    public List<Person> findByAgeBetween(int from, int until) {
        List<Person> persons = new ArrayList<Person>();
        ResultSet results = null;

        if (from < 0 || until < 0) {
            from = Math.abs(from);
            until = Math.abs(until);
        }
        if (from > until) {
            int temp = until;
            until = from;
            from = temp;
        }

        try {
            ps = connection
                    .prepareStatement("select * from person where age between ? and ?");
            ps.setInt(1, from);
            ps.setInt(2, until);
            results = ps.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            while (results.next()) {
                Person p;
                p = new Person(results.getString("name"), results.getInt("age"));
                p.setId(results.getInt("id"));
                persons.add(p);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return persons;
    }
}
