package testpackages;

import collectivedatabase.CollectiveCollection;
import collectivedatabase.CollectiveDatabase;
import testpackages.models.User;
import testpackages.controllers.Users;

/**
 * Created by Andrei-Madalin on 11/02/14.
 */
public class TestCollectiveConnection {
    static void setConnectionEnvironmentVariables(){
        CollectiveDatabase.setLogger();
        CollectiveDatabase.setConnection();
    }

    public static void main(String[] args){
        setConnectionEnvironmentVariables();

        User user = Users.first();
        CollectiveCollection<User> users = Users.all();

        System.out.println(user.get("name"));
        System.out.println(user.get("age"));

        System.out.println(users.first().get("name"));
        System.out.println(users.last().get("age"));

        System.out.println(Users.find(1));

        Users.find_by();
    }
}
