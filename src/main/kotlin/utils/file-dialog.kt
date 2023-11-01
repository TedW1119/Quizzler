package utils

import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

/*
** File Dialog Class for Opening Files
 */
class FileDialog {
    // getFileToOpen() gets the correct file based on Mac or Windows
    fun getFileToOpen(): File? {
        val osName = System.getProperty("os.name").lowercase(Locale.getDefault())

        return when {
            osName.contains("win") -> {
                openFileExplorerWindows()
            }
            osName.contains("mac") -> {
                openFinderMac()
            }
            else -> {
                openFileExplorerFallback()
            }
        }
    }

    // openFileExplorerWindows() opens file explorer
    private fun openFileExplorerWindows(): File? {
        val frame = Frame()
        val fileDialog = FileDialog(frame, "Open File", FileDialog.LOAD)
        fileDialog.file = null // Clear the default file name
        fileDialog.isMultipleMode = false // Allow selecting only one file
        fileDialog.setFile("*.pdf;*.pptx") // Filter for PDF and PPTX files only
        fileDialog.isVisible = true
        val selectedFile = fileDialog.file
        return if (selectedFile != null) File(fileDialog.directory, selectedFile) else null
    }

    // openFinderMac() opens finder
    private fun openFinderMac(): File? {
        val fileChooser = JFileChooser()
        val filter = FileNameExtensionFilter(
            "PDF and PPTX Files",
            "pdf", "pptx") // Filter for PDF and PPTX files only
        fileChooser.fileFilter = filter
        val result = fileChooser.showOpenDialog(null)
        return if (result == JFileChooser.APPROVE_OPTION) fileChooser.selectedFile else null
    }

    // Linux?
    private fun openFileExplorerFallback(): File? {
        // Implement a fallback mechanism for unsupported platforms here
        // For example, you could show a custom file picker dialog
        return null
    }
}