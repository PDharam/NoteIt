package wolverine.noteit.view_holder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.row_note_new.view.*
import wolverine.noteit.R
import wolverine.noteit.adapter.NoteAdapter
import wolverine.noteit.model.Note

/**
 * Created by Dell on 03-Apr-18.
 */
class NoteViewHolder(val noteAdapter: NoteAdapter?, itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        if (v?.id == R.id.tv_note_title) {
            noteAdapter?.OnClickListner?.editNote(adapterPosition);
        }
    }

    public fun bind(note: Note) {
        itemView.tv_note_title.text = note.text;
        itemView.tv_note_title.setOnClickListener(this)
    }
}