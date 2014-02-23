package collectivedatabase;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Andrei-Madalin on 16/02/14.
 */
public class CollectiveController {
    static ClassLoader classLoader = CollectiveController.class.getClassLoader();

    public static Object first(Class<?> Klass) {
        String className = Klass.getSimpleName().toLowerCase() + "s";

        String query = "SHOW KEYS FROM " + className + " WHERE Key_name = 'PRIMARY'";
        ArrayList<HashMap<String, Object>> primaryKeys = executeQuery(query, null);

        int primaryKeysLength = primaryKeys.size();
        String queryOrder = "";
        for (int i = 0; i < primaryKeysLength; ++i) {
            queryOrder += primaryKeys.get(i).get("COLUMN_NAME") + " ";
            if (i < primaryKeysLength - 1)
                queryOrder += "AND ";
        }

        query = "SELECT * FROM " + className + " ORDER BY " + queryOrder + " ASC LIMIT 1";
        return getFirstResult(query, null);
    }

    public static Object last(Class<?> Klass) {
        String className = Klass.getSimpleName().toLowerCase() + "s";
        String query = "SHOW KEYS FROM " + className + " WHERE Key_name = 'PRIMARY'";
        ArrayList<HashMap<String, Object>> primaryKeys = executeQuery(query, null);

        int primaryKeysLength = primaryKeys.size();
        String queryOrder = "";
        for (int i = 0; i < primaryKeysLength; ++i) {
            queryOrder += primaryKeys.get(i).get("COLUMN_NAME") + " ";
            if (i < primaryKeysLength - 1)
                queryOrder += "AND ";
        }

        query = "SELECT * FROM " + className + " ORDER BY " + queryOrder + " DESC LIMIT 1";
        return getFirstResult(query, null);
    }

    public static Object all(Class<?> Klass) {
        String className = Klass.getSimpleName().toLowerCase() + "s";
        String query = "SELECT * FROM " + className;

        ArrayList<HashMap<String, Object>> result = executeQuery(query, null);
        Object obj = new CollectiveCollection(Klass, result);
        if (((CollectiveCollection) obj).length() > 0)
            return obj;
        else
            return new CollectiveCollection(Klass);
    }

    public static Object find(Class<?> Klass, Object args) {
        String className = Klass.getSimpleName().toLowerCase() + "s";

        Object[] argsArray;
        argsArray = args.getClass().isArray() ? (Object[]) args : new Object[]{args};

        String query = "SHOW KEYS FROM " + className + " WHERE Key_name = 'PRIMARY'";
        ArrayList<HashMap<String, Object>> primaryKeys = executeQuery(query, null);

        int primaryKeysLength = primaryKeys.size();
        int argsLength = argsArray.length;
        if (primaryKeysLength != argsLength) {
            CollectiveDatabase.DBLogger.log(Level.WARNING, "Table " + className + " has " + primaryKeysLength + " primary key(s) but you are calling the method find with " + argsLength + " args .");
            return new CollectiveModel();
        }

        query = "SELECT * FROM " + className + " WHERE ";
        for (int i = 0; i < primaryKeysLength; ++i) {
            query += primaryKeys.get(i).get("COLUMN_NAME") + " = ? ";
            if (i < primaryKeysLength - 1)
                query += "AND ";
        }

        return getFirstResult(query, argsArray);
    }

    public static Object find_by(Class<?> Klass, Object args) {

        return null;
    }

    private static Object getFirstResult(String query, Object[] args) {
        ArrayList<HashMap<String, Object>> result = executeQuery(query, args);
        Object obj;
        if (result.size() > 0) {
            obj = new CollectiveModel(result.get(0));
        } else {
            obj = new CollectiveModel();
            CollectiveDatabase.DBLogger.log(Level.WARNING, "The object does not exist");
        }
        return obj;
    }

    private static ArrayList<HashMap<String, Object>> executeQuery(String query, Object[] objects) {

        ResultSet result;
        ArrayList<HashMap<String, Object>> vals = new ArrayList<>();

        PreparedStatement preparedStatement;
        try {
            preparedStatement = CollectiveDatabase.CONNECTION.prepareStatement(query);
            if (objects != null) {
                for (int i = 0; i < objects.length; i++) {
                    switch (objects[i].getClass().getSimpleName()) {
                        case "String":
                            preparedStatement.setString(i + 1, ((String) objects[i]).toLowerCase());
                            break;
                        case "Integer":
                            preparedStatement.setInt(i + 1, ((Integer) objects[i]));
                            break;
                    }

                }
            }

            CollectiveDatabase.DBLogger.log(Level.INFO, "Executing query " + preparedStatement);
            result = preparedStatement.executeQuery();

            ResultSetMetaData metaData = result.getMetaData();
            int columns = metaData.getColumnCount();
            try {
                while (result.next()) {
                    HashMap<String, Object> row = new HashMap<>(columns);
                    for (int i = 1; i <= columns; ++i) {
                        row.put(metaData.getColumnName(i), result.getObject(i));
                    }
                    vals.add(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            CollectiveDatabase.DBLogger.log(Level.SEVERE, e.getMessage(), e);
        }

        return vals;
    }

    /**
     * Example:
     * Class[] paramsTypes = new Class[]{String.class, Object[].class};
     * Object[] paramsValues = new Object[]{query, objects};
     * String methodName = "executeQuery";
     * invokeMethodOfClass(Klass, methodName, paramsTypes, paramsValues);
     *
     * @param Klass
     * @param methodName
     * @param paramsTypes
     * @param paramsValues
     * @return
     */
    public static Object invokeMethodOfClass(Class<?> Klass, String methodName, Class[] paramsTypes, Object[] paramsValues) {
        Object returnValue = null;
        try {
            returnValue = Class.forName(Klass.getName()).getMethod(methodName, paramsTypes).invoke(Klass.newInstance(), paramsValues);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException | InstantiationException e) {
            CollectiveDatabase.DBLogger.log(Level.SEVERE, e.getMessage(), e);
        }
        return returnValue;
    }
}
