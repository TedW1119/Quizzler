package controllers

import services.NoteService
import util.DataModels.Note

class NoteController {
    private val noteService: NoteService = NoteService()

    fun getNote(id: String): Note? {
        return noteService.getNote(id)
    }

    fun createNote(note: Note) {
        noteService.createNote(note)
    }

    fun deleteNote(id: String): Boolean {
        return noteService.deleteNote(id)
    }
}
