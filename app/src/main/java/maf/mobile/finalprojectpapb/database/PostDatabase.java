//package maf.mobile.finalprojectpapb.database;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import maf.mobile.finalprojectpapb.model.Post;
//
//@Database(entities =  {Post.class}, version = 1)
//public abstract class PostDatabase extends RoomDatabase {
//    private  static final String DBNAME = "otolink";
//    public static PostDatabase instance;
//    public static PostDatabase getDb(Context context){
//        if (PostDatabase.instance == null) {
//            PostDatabase.instance =
//                    Room.databaseBuilder(context.getApplicationContext(),
//                            PostDatabase.class, PostDatabase.DBNAME).build();
//        }
//        return PostDatabase.instance;
//    }
//
//    public abstract PostDAO postDao();
//
//}
