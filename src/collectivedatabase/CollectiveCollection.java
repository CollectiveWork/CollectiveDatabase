package collectivedatabase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Andrei-Madalin on 17/02/14.
 */
public class CollectiveCollection<T> {
    ArrayList<T> values = new ArrayList<>();

    public CollectiveCollection(Class<T> Klass){
        try {
            values.add(Klass.getConstructor(new Class[]{HashMap.class}).newInstance(new HashMap<String, Object>()));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            CollectiveDatabase.DBLogger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public CollectiveCollection(Class<T> Klass, ArrayList<HashMap<String, Object>> vals) {
        for (HashMap<String, Object> val : vals) {
            try {
                values.add(Klass.getConstructor(new Class[]{HashMap.class}).newInstance(val));
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                CollectiveDatabase.DBLogger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    public int length(){
        return values.size();
    }

    public T first() {
        return values.get(0);
    }

    public T last() {
        return values.get(values.size() - 1);
    }

    // TODO iterator
}
