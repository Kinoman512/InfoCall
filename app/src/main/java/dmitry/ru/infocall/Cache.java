package dmitry.ru.infocall;

import android.content.Context;
import android.util.Log;

import dmitry.ru.infocall.utils.Setting;

/**
 * Created by Dmitry on 20.06.2016.
 */
public class Cache  {

    private final String Tag = "MyCacheTag";
    private final String id;


    Context context ;


    public Cache(String id,Context context){
        this.id = id;
        this.context = context;

    }


    public void put(String key, String val){

        Setting.setString(key + "_" + id,val);
//        long now = new Date().getTime();
//        if(now - date > delta || val == null){
//            Setting.setLong(file, now);
//            save(null);
//            date = now;
//        }
//        List<LinkedHashMap<T, K>> d = read().data;
//
//        LinkedHashMap<T,K>  map = new LinkedHashMap<T,K>();
//
//        if(d == null){
//            d = new ArrayList<LinkedHashMap<T, K>>();
//        }
//        map.put(key,val);
//        d.add(map);
//        Data data = new Data();
//        data.data = d;
//        save(data);

    };

    public String  get(String key){

        Log.d(Tag, key);

        return Setting.getString(key + "_" + id);
//        long now = new Date().getTime();
//        if(now - date > delta ){
//            Setting.setLong(file, now);
//            save(null);
//            date = now;
//            return  null;
//        }
//
//
//        List<LinkedHashMap<T, K>> data = read().data;
//        if (data == null){
//            return null;
//        }
//        for( LinkedHashMap<T, K> e: data){
//
//            if( e.keySet().contains(key)){
//                return e.get(key);
//            }
//
//        }

//        return null;
    };




//    boolean save ( Data<List<LinkedHashMap<T, K>>> data) {
//
//
//        String path =UtilFile.getPath(context) + file;
//        FileOutputStream fos = null;
//        try {
//
//
//            fos = new FileOutputStream(path);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//
//
//            oos.writeObject(data);
//            oos.flush();
//            oos.close();
//            return true;
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

//    Data<List<LinkedHashMap<T, K>>> read() {
//        String path = UtilFile.getPath(context) + file;
//        Data<List<LinkedHashMap<T, K>>> data = new Data<>();
//        FileInputStream fis = null;
//        ObjectInputStream oin = null;
//
//        try {
//            fis = new FileInputStream(path);
//            oin = new ObjectInputStream(fis);
//
//            data = ( Data<List<LinkedHashMap<T, K>>>) oin.readObject();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (StreamCorruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                if(fis !=null)
//                     fis.close();
//                if(oin !=null)
//                     oin.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(data == null) data =  new Data<List<LinkedHashMap<T, K>>>();
//        return data;
//    }
}
