package dmitry.ru.infocall;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dmitry on 20.06.2016.
 */
public class FileSave {
    static String file = "/bd5";

    static public void delete(Context c, String phone) {
        List<LinkedHashMap<String, String>> list = FileSave.read(c);
        for (Map<String, String> e2 : list) {
            if (e2.get("number").equals(phone)) {
                list.remove(e2);
                break;
            }
        }

        FileSave.save(c, list);


    }

    static public boolean save(Context c, List<LinkedHashMap<String, String>> list) {
        String path = c.getFilesDir().getPath().toString() + file;
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();

            fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            MyFile mf = new MyFile();
            mf.list = list;
            oos.writeObject(mf);
            oos.flush();
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    static public List<LinkedHashMap<String, String>> read(Context c) {
        String path = c.getFilesDir().getPath().toString() + file;
        FileInputStream fis = null;
        MyFile mf = new MyFile();
        try {
            fis = new FileInputStream(path);
            ObjectInputStream oin = new ObjectInputStream(fis);
            mf = (MyFile) oin.readObject();
        } catch (FileNotFoundException e) {
          //  e.printStackTrace();
        } catch (StreamCorruptedException e) {
          //  e.printStackTrace();
        } catch (IOException e) {
         //   e.printStackTrace();
        } catch (ClassNotFoundException e) {
          //  e.printStackTrace();
        }


        return mf.list;
    }


    public static class MyFile implements Serializable {
        public List<LinkedHashMap<String, String>> list = new ArrayList<>();


    }
}
