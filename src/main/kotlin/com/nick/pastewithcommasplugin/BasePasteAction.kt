package com.nick.pastewithcommasplugin

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ide.CopyPasteManager
import java.awt.datatransfer.DataFlavor

abstract class BasePasteAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        
        val clipboardContent = getClipboardContent() ?: return
        val processedContent = processClipboardContent(clipboardContent)
        
        WriteCommandAction.runWriteCommandAction(project) {
            val document = editor.document
            val caretModel = editor.caretModel
            val offset = caretModel.offset
            
            document.insertString(offset, processedContent)
            caretModel.moveToOffset(offset + processedContent.length)
        }
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val project = e.project
        e.presentation.isEnabledAndVisible = editor != null && project != null && hasClipboardContent()
    }

    protected fun getClipboardContent(): String? {
        return try {
            val clipboard = CopyPasteManager.getInstance()
            clipboard.getContents<String>(DataFlavor.stringFlavor)
        } catch (e: Exception) {
            null
        }
    }

    protected fun hasClipboardContent(): Boolean {
        return getClipboardContent() != null
    }

    protected fun isNumeric(str: String): Boolean {
        if (str.isEmpty()) return false
        return try {
            str.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    protected fun processLinesWithCommas(
        input: String,
        lineProcessor: (String, Boolean) -> String
    ): String {
        if (input.isBlank()) return input
        
        val lines = input.split('\n')
        
        // If all lines are empty, don't add commas
        if (lines.all { it.isEmpty() }) {
            return input
        }
        
        return lines.mapIndexed { index, line ->
            val processedLine = lineProcessor(line, index == lines.lastIndex)
            if (index == lines.lastIndex) {
                processedLine
            } else {
                "$processedLine,"
            }
        }.joinToString("\n")
    }

    abstract fun processClipboardContent(input: String): String
}