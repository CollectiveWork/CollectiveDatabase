package testpackages.controllers;

import collectivedatabase.CollectiveCollection;
import collectivedatabase.CollectiveController;
import collectivedatabase.CollectiveModel;
import testpackages.models.User;

/**
 * Created by Andrei-Madalin on 16/02/14.
 */
public class Users extends CollectiveController {
    public static User first(){
        return new User((CollectiveModel)CollectiveController.first(User.class));
    }

    public static User last(){
        return new User((CollectiveModel)CollectiveController.last(User.class));
    }

    public static User find(Object... args){
        return new User((CollectiveModel)CollectiveController.find(User.class, args));
    }

    public static CollectiveCollection<User> find_by(Object... args){
        return (CollectiveCollection<User>)CollectiveController.find_by(User.class, args);
    }

    public static CollectiveCollection<User> all(){
        return (CollectiveCollection<User>)CollectiveController.all(User.class);
    }

}
