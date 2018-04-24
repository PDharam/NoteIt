package wolverine.noteit

import android.app.Application
import android.content.Context
import android.util.Log
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import wolverine.noteit.model.MyObjectBox


/**
 * Created by Dell on 05-Apr-18.
 */

class NoteItApplication : Application() {

    lateinit var boxStore: BoxStore

    override fun onCreate() {
        super.onCreate()
        // do this once, for example in your Application class
        boxStore = MyObjectBox.builder().androidContext(this).build();

    }


}
