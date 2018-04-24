package wolverine.noteit.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note_editor.*
import wolverine.noteit.NoteItApplication
import wolverine.noteit.R
import wolverine.noteit.model.Note

class NoteEditorActivity : AppCompatActivity() {
    var note: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)
        getIntentData();
        setData()

    }

    private fun setData() {
        edt_note_editor.setText(note?.text)
    }

    private fun getIntentData() {
        val intent = getIntent();
        note = intent.getSerializableExtra("Note") as Note?
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == R.id.action_done) {
                if (note == null) {
                    note = Note(edt_note_editor.text.toString())
                } else {
                    note?.text = edt_note_editor.text.toString();
                }
                var id = (application as NoteItApplication).boxStore?.boxFor(Note::class.java)?.put(note);
                Toast.makeText(this, "Successfully Save...!", Toast.LENGTH_SHORT);
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
