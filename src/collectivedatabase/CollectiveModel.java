package collectivedatabase;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Andrei-Madalin on 16/02/14.
 */
public class CollectiveModel {
    protected HashMap<String, Object> values = new HashMap<>();

    public CollectiveModel() {

    }

    public CollectiveModel(HashMap<String, Object> vals) {
        values = vals;
    }

    public CollectiveModel(CollectiveModel collectiveModel) {
        values = collectiveModel.getAllValues();
    }

    public Object save() {
        return null;
    }

    public Object get(String column) {
        Object val = values.get(column);

        if (values.keySet().size() > 0) {
            if (!values.containsKey(column))
                CollectiveDatabase.DBLogger.log(Level.WARNING, "The column " + column + " does not exist!");
        } else {
            CollectiveDatabase.DBLogger.log(Level.WARNING, "The object does not exist!");
        }


        return val;
    }

    @Override
    public String toString() {
        String callerClass = this.getClass().getSimpleName();
        return callerClass + "{" +
                "values=>" + values +
                '}';
    }

    public HashMap<String, Object> getAllValues() {
        return values;
    }

//
//    public void executeNonQueryPreparedStatement(PreparedStatement preparedStatement) {
//        try {
//            preparedStatement.executeUpdate();
//            preparedStatement.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
