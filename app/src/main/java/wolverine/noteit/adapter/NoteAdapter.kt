package wolverine.noteit.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import wolverine.noteit.R
import wolverine.noteit.interfaces.OnClickListner
import wolverine.noteit.model.Note
import wolverine.noteit.view_holder.NoteViewHolder

/**
 * Created by Dell on 03-Apr-18.
 */
class NoteAdapter(val context: Context, private var noteList: ArrayList<Note>) : RecyclerView.Adapter<NoteViewHolder>() {
    var OnClickListner: OnClickListner? = null

    override fun onBindViewHolder(holder: NoteViewHolder?, position: Int) {
        if (holder != null) {
            holder.bind(noteList.get(position))
        }

    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NoteViewHolder {
        return NoteViewHolder(this, LayoutInflater.from(context).inflate(R.layout.row_note_new, parent, false))
    }

    fun setNoteList(noteList: ArrayList<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        noteList.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    fun restoreItem(note: Note, position: Int) {
        noteList.add(position, note)
        // notify item added by position
        notifyItemInserted(position)
    }
}