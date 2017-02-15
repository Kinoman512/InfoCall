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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dmitry.ru.infocall.utils.Setting;

/**
 * Created by Dmitry on 20.06.2016.
 */
public class Cache<T,K> implements Serializable  {

    public class Data<M>   implements Serializable {
        public M data;
    }


    long date;
    long delta = 259200000;
    String file;

    public Cache(String id){
        file ="/cache4" + id ;

       long d = Setting.getLong(file);

        if(d == 0){
            d = new Date().getTime();
           Setting.setLong(file, d);
        }
        date = d;

        String path = MainActivity.filedir + file;
        File file = new File(path);
        File f2 = new File(MainActivity.filedir);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (!f2.exists())
            file.mkdirs();

    }


    public void put(T key, K val){
        long now = new Date().getTime();
        if(now - date > delta || val == null){
            Setting.setLong(file, now);
            save(null);
            date = now;
        }
        List<LinkedHashMap<T, K>> d = read().data;

        LinkedHashMap<T,K>  map = new LinkedHashMap<T,K>();

        if(d == null){
            d = new ArrayList<LinkedHashMap<T, K>>();
        }
        map.put(key,val);
        d.add(map);
        Data data = new Data();
        data.data = d;
        save(data);

    };

    public K get(T key){
        long now = new Date().getTime();
        if(now - date > delta ){
            Setting.setLong(file, now);
            save(null);
            date = now;
            return  null;
        }


        List<LinkedHashMap<T, K>> data = read().data;
        if (data == null){
            return null;
        }
        for( LinkedHashMap<T, K> e: data){

            if( e.keySet().contains(key)){
                return e.get(key);
            }

        }

        return null;
    };




    boolean save ( Data<List<LinkedHashMap<T, K>>> data) {


        String path = MainActivity.filedir + file;
        FileOutputStream fos = null;
        try {


            fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);


            oos.writeObject(data);
            oos.flush();
            oos.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    Data<List<LinkedHashMap<T, K>>> read() {
        String path = MainActivity.filedir + file;
        Data<List<LinkedHashMap<T, K>>> data = new Data<>();
        FileInputStream fis = null;
        ObjectInputStream oin = null;

        try {
            fis = new FileInputStream(path);
            oin = new ObjectInputStream(fis);

            data = ( Data<List<LinkedHashMap<T, K>>>) oin.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fis !=null)
                     fis.close();
                if(oin !=null)
                     oin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(data == null) data =  new Data<List<LinkedHashMap<T, K>>>();
        return data;
    }
}
