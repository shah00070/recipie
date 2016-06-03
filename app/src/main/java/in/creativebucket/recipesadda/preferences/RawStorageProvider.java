package in.creativebucket.recipesadda.preferences;


import android.content.Context;
import android.content.SharedPreferences;

public class RawStorageProvider {
    public static final String PREFS_NAME = "ReceipesAddaPref";
    public static RawStorageProvider instance = null;
    private Context context;

    public RawStorageProvider(Context _context) {
        context = _context;
    }

    public static RawStorageProvider getInstance(Context context) {
        if (instance == null)
            instance = new RawStorageProvider(context);
        return instance;
    }

    public void dumpDataToStorage(String storageCallName, boolean dataToSave) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = storage.edit();
        editor.putBoolean(storageCallName, dataToSave);
        editor.commit();
    }

    public boolean isThisValueSet(String storageCallName) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        boolean data = storage.getBoolean(storageCallName, false);
        return data;
    }

    public boolean isThisValueSet(String storageCallName, boolean initialValue) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        boolean data = storage.getBoolean(storageCallName, initialValue);
        return data;
    }

    public void dumpDataToStorage(String storageCallName, String dataToSave) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = storage.edit();
        editor.putString(storageCallName, dataToSave);
        editor.commit();
    }

    public String getDataFromStorage(String storageCallName) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        String data = storage.getString(storageCallName, null);
        return data;
    }

    public String getDataFromStorage(String storageCallName, String returnValue) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        String data = storage.getString(storageCallName, returnValue);
        return data;
    }

    public void dumpDataToStorage(String storageCallName, int dataToSave) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = storage.edit();
        editor.putInt(storageCallName, dataToSave);
        editor.commit();
    }

    public int getNumberFromStorage(String storageCallName) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        int data = storage.getInt(storageCallName, 1);
        return data;
    }

    public int getNumberFromStorage(String storageCallName, int defaultValue) {
        SharedPreferences storage = context.getSharedPreferences(PREFS_NAME, 0);
        int data = storage.getInt(storageCallName, defaultValue);
        return data;
    }
}
