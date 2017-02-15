

package dmitry.ru.infocall.utils.contact;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.renderscript.Element;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dmitry.ru.myapplication.R;

/**
 * Created by Dmitry on 17.02.2016.
 */
public class ContactUtil {

    public static List<LinkedHashMap<String, String>> getContactCursor(Context con,Uri from, List<String> colums, String selection, String[] selectionArgs) {

        List<LinkedHashMap<String, String>> res = new ArrayList<>();


        String[] projection = new String[colums.size()];
        colums.toArray(projection); // fill the array


        Log.d("ContactUtil", "requsired cols are  " + projection.toString());

        Cursor cursor = con.getContentResolver().query(from , projection, selection, selectionArgs, null);


        Log.d("ContactUtil", " The cursor have  " + cursor.getCount() + " row");
        if (cursor.getCount() > 0) {
    /* Найдено */
            while(cursor.moveToNext()){
                LinkedHashMap<String, String> answers = new LinkedHashMap<>();
                int count = cursor.getColumnCount();


                for (int i = 0; i < count; i++) {

                    String key = colums.get(i);
                    Log.d("ContactUtil", key + i);
                    int key2 = cursor.getColumnIndex(key);
                    String value = cursor.getString(key2);
                    answers.put(key, value);

                }
                res.add(answers);
            }
        } else {
    /* Не найдено */
            LinkedHashMap<String, String> answers = new LinkedHashMap<>();
            res.add(answers);
            Log.d("ContactUtil", "I  dont have the contact ");
        }
        cursor.close();
        return res;
    }
    public static List<LinkedHashMap<String, String>> getCallLog(Context con) {
        List<String> cols = new ArrayList<>();
        Uri from = android.provider.CallLog.Calls.CONTENT_URI;

        cols.add(android.provider.CallLog.Calls.NUMBER);
        //cols.add(android.provider.CallLog.Calls.DATE);


        String selection =  null;
        String[] selectionArgs = null;

        List<LinkedHashMap<String, String>> list = getContactCursor(con, from, cols, selection, selectionArgs);
        return list;

    }
    public static LinkedHashMap<String, String> getContactInfo(Context con, String phone) {
        List<String> cols = new ArrayList<>();
        Uri from = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //cols.add(ContactsContract.CommonDataKinds.Phone._ID);
        cols.add(ContactsContract.CommonDataKinds.Note.NOTE);
        cols.add(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
       // cols.add(ContactsContract.CommonDataKinds.Phone.NUMBER);

//        cols.add( ContactsContract.CommonDataKinds.StructuredPostal.CITY);
//        cols.add(ContactsContract.CommonDataKinds.Organization.COMPANY);
//        cols.add(ContactsContract.CommonDataKinds.Organization.TITLE);

        Map<String, String> search = new LinkedHashMap<>();
        search.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);

        String selection = "PHONE_NUMBERS_EQUAL(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ",?)";
        String[] selectionArgs = new String[]{phone};
        LinkedHashMap<String, String> answers = getContactCursor(con,from, cols, selection, selectionArgs).get(0);
        Log.d("ContactUtil ", " the info about user " + answers.toString());
        return answers;

    }

    public static void addContact(Context con, Map<String, String> map, String phone, Bitmap photo) {
        Log.d("ContactUtil", "Add new  contact " + map);
        if (map == null) {
            Log.d("ContactUtil", "The contacs map  is null!");
            return;
        }
        Map<String, String> temp = new HashMap<>();
        for( String e: map.keySet()){
            String[] arr = e.split(":");
            if(arr.length  >1){
                temp.put(arr[0], map.get(e));
            }else{
                temp.put(e,map.get(e));
            }
        }
        map = temp;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.
                newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, map.get("name"))
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, map.get("name"))
                        //.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "lastName")
                .build());

        ops.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());


        if (!"".equals(map.get("phone"))) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, map.get("phone"))
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }


        if (!"".equals(map.get("birthday")) && map.get("birthday") != null ) {

            long timeStamp  = Long.valueOf(map.get("birthday"));
            Date date = new Date(timeStamp * 1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String formattedDate = sdf.format(date);

            Log.d("ContactUtil", "Add new  contact with bithday  " + formattedDate + " " + (map.get("birthday")));
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                    .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, formattedDate)
                    .build());

        }


        //adress
        if ((!"".equals(map.get("city")) && !"".equals(map.get("country")))) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                            //.withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, map.get("company"))
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, map.get("city"))
                            //.withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, map.get("company"))
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, map.get("country"))
                    .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, 1093)
                    .build());
        }

        //organization
        if ((!"".equals(map.get("company")) && !"".equals(map.get("organization")))) {

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, map.get("company"))
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, map.get("organization"))
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        //photo
        if (photo != null) {
            try {
                Log.d("ContactUtil", "with image ");


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 75, stream);


                // Adding insert operation to operations list
                // to insert Photo in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                        .build());

                try {
                    stream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        try {
            Log.d("ContactUtil", " Test " + con.getContentResolver().toString());

            ContentResolver a = con.getContentResolver();
            ContentProviderResult[] results = a.applyBatch(ContactsContract.AUTHORITY, ops);
    //        Toast.makeText(con, "Contact is successfully added", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bitmap retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        if (contactId == null) {
            return null;
        }

        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_callscreen);

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }

            assert inputStream != null;
            if (inputStream != null)
                inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }


}
