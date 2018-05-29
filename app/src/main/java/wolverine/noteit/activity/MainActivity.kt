package wolverine.noteit.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.android.synthetic.main.activity_main.*
import wolverine.noteit.NoteItApplication
import wolverine.noteit.R
import wolverine.noteit.adapter.NoteAdapter
import wolverine.noteit.interfaces.OnClickListner
import wolverine.noteit.model.Note
import wolverine.noteit.utils.RecyclerItemTouchHelper
import wolverine.noteit.view_holder.NoteViewHolder


class MainActivity : AppCompatActivity(), OnClickListner, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    var noteList = ArrayList<Note>()
    val NOTE_REQUEST_CODE: Int = 101
    val EDIT_NOTE_REQUEST_CODE: Int = 201
    var noteAdapter: NoteAdapter? = null
    var boxStore: BoxStore? = null
    var noteBox: Box<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boxStore = (application as NoteItApplication).boxStore
        noteBox = boxStore?.boxFor(Note::class.java)

        val recordsize = noteBox?.query()?.build()?.find()?.size

        if (recordsize != null && recordsize > 0) {
            noteList = noteBox?.query()?.build()?.find() as ArrayList<Note>
        }


        rv_note_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_note_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        noteAdapter = NoteAdapter(this, noteList)
        noteAdapter?.OnClickListner = this
        rv_note_list.adapter = noteAdapter

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_note_list)

        img_btn_add_note.setOnClickListener({
            startActivityForResult(Intent(this, NoteEditorActivity::class.java), NOTE_REQUEST_CODE)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == NOTE_REQUEST_CODE || requestCode == EDIT_NOTE_REQUEST_CODE) {
                noteList.clear()
                noteList = noteBox?.query()?.build()?.find() as ArrayList<Note>
                noteAdapter?.setNoteList(noteList)
            }
        }

    }

    override fun editNote(position: Int) {
        val intent = Intent(this, NoteEditorActivity::class.java)
        intent.putExtra("Note", noteList[position])
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE)
    }

    var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // Row is swiped from recycler view
            // remove it from adapter
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            // view the background view
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is NoteViewHolder) {
            // get the removed item name to display it in snack bar
            val name = noteList.get(viewHolder.adapterPosition).text

            // backup of removed item for undo purpose
            val deletedItem = noteList.get(viewHolder.adapterPosition)
            val deletedIndex = viewHolder.adapterPosition

            // remove the item from recycler view
            noteAdapter?.removeItem(viewHolder.adapterPosition)
            //remove the note object from objectbox db
            noteBox?.remove(deletedItem)

            // showing snack bar with Undo option
            val snackbar = Snackbar
                    .make(rl_main, name + " removed from cart!", Snackbar.LENGTH_LONG)
            snackbar.setAction("UNDO", {
                //undo the deleted note, insert in db
                noteBox?.put(deletedItem)
                // undo is selected, restore the deleted item
                noteAdapter?.restoreItem(deletedItem, deletedIndex)
            })
            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()
        }

    }

}
