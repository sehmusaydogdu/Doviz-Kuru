package com.dovizkuru;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dovizkuru.KurBilgileri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 23.12.2017.
 */

public class Database extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=1;//Database Version
    private static final String DATABASE_NAME="DOVİZKURLAR";//Database Name
    private static final String TABLE_NAME="DOVİZ";//Table Name

    private static String ID="_id";
    private static String DOLAR_ALIS="dolar_alis";
    private static String DOLAR_SATIS="dolar_satis";
    private static String EURO_ALIS="euro_alis";
    private static String EURO_SATIS="euro_satis";

    public Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DOLAR_ALIS+" TEXT, "
                +DOLAR_SATIS+" TEXT, "
                +EURO_ALIS+" TEXT, "
                +EURO_SATIS+" TEXT "+")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void dovizKuruEkle(KurBilgileri kurlar){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DOLAR_ALIS,kurlar.getDolarAlis());
        values.put(DOLAR_SATIS,kurlar.getDolarSatis());
        values.put(EURO_ALIS,kurlar.getEuroAlis());
        values.put(EURO_SATIS,kurlar.getEuroSatis());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public List<KurBilgileri> getAllList(){
        List<KurBilgileri> kurBilgileriList=new ArrayList<>();
        String selectQuery="SELECT dolar_alis,dolar_satis,euro_alis,euro_satis FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();


        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                KurBilgileri kur=new KurBilgileri();
                kur.setDolarAlis(cursor.getString(0));
                kur.setDolarSatis(cursor.getString(1));
                kur.setEuroAlis(cursor.getString(2));
                kur.setEuroSatis(cursor.getString(3));
                kurBilgileriList.add(kur);
            }
            while (cursor.moveToNext());
        }
        return kurBilgileriList;
    }
}
