package wolverine.noteit.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable

/**
 * Created by Dell on 03-Apr-18.
 */
@Entity
class Note : Serializable {
    @Id
    var id: Long = 0
    var text: String = ""

    constructor(text: String) {
        this.text = text
    }

    constructor()
}