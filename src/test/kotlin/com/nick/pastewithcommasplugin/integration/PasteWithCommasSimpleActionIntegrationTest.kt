package com.nick.pastewithcommasplugin.integration

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.nick.pastewithcommasplugin.PasteWithCommasSimpleAction
import java.awt.datatransfer.StringSelection

class PasteWithCommasSimpleActionIntegrationTest : BasePlatformTestCase() {

    private fun setClipboard(content: String) {
        val clipboard = CopyPasteManager.getInstance()
        clipboard.setContents(StringSelection(content))
    }

    private fun createActionEvent(editor: Editor): AnActionEvent {
        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.EDITOR, editor)
            .add(CommonDataKeys.PROJECT, project)
            .build()
        
        return AnActionEvent.createFromDataContext("test", null, dataContext)
    }

    fun testSimplePasteWithNumbers() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("1\n2\n3")
        
        val action = PasteWithCommasSimpleAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Simple action should not add quotes, even for numbers
        myFixture.checkResult("1,\n2,\n3")
    }

    fun testSimplePasteWithText() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("apple\nbanana\ncherry")
        
        val action = PasteWithCommasSimpleAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Simple action should not add quotes, even for text
        myFixture.checkResult("apple,\nbanana,\ncherry")
    }

    fun testSimplePasteWithMixedContent() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("1\ntext\n3.14")
        
        val action = PasteWithCommasSimpleAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Simple action never adds quotes
        myFixture.checkResult("1,\ntext,\n3.14")
    }

    fun testSimplePasteInMiddleOfText() {
        val file = myFixture.configureByText("test.txt", "start<caret>end")
        val editor = myFixture.editor
        
        setClipboard("x\ny\nz")
        
        val action = PasteWithCommasSimpleAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        myFixture.checkResult("startx,\ny,\nzend")
    }

    fun testSimplePasteWithExistingCommas() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("item1,\nitem2\nitem3")
        
        val action = PasteWithCommasSimpleAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Should add commas even if they already exist
        myFixture.checkResult("item1,,\nitem2,\nitem3")
    }
}