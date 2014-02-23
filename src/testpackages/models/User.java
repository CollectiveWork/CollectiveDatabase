package testpackages.models;

import collectivedatabase.CollectiveModel;
import java.util.HashMap;

/**
 * Created by Andrei-Madalin on 16/02/14.
 */
public class User extends CollectiveModel {
    public User(CollectiveModel collectiveModel){
        values = collectiveModel.getAllValues();
    }

    public User(HashMap<String, Object> vals){
        values = vals;
    }
}
