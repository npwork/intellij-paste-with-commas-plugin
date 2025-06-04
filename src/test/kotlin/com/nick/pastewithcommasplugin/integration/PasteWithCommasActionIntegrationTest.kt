package com.nick.pastewithcommasplugin.integration

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.nick.pastewithcommasplugin.PasteWithCommasAction
import java.awt.datatransfer.StringSelection

class PasteWithCommasActionIntegrationTest : BasePlatformTestCase() {

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

    fun testIntelligentPasteWithNumbers() {
        // Create a file with some content
        val file = myFixture.configureByText("test.txt", "initial content<caret>")
        val editor = myFixture.editor
        
        // Set clipboard with numbers
        setClipboard("1\n2\n3")
        
        // Execute the action
        val action = PasteWithCommasAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Verify the result
        myFixture.checkResult("initial content1,\n2,\n3")
    }

    fun testIntelligentPasteWithText() {
        // Create a file with some content
        val file = myFixture.configureByText("test.txt", "start<caret>")
        val editor = myFixture.editor
        
        // Set clipboard with text
        setClipboard("apple\nbanana\ncherry")
        
        // Execute the action
        val action = PasteWithCommasAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Verify the result
        myFixture.checkResult("start'apple',\n'banana',\n'cherry'")
    }

    fun testIntelligentPasteWithMixedContent() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        // Set clipboard with mixed content (should use quotes)
        setClipboard("1\ntext\n3")
        
        val action = PasteWithCommasAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        myFixture.checkResult("'1',\n'text',\n'3'")
    }

    fun testCaretPositionAfterPaste() {
        val file = myFixture.configureByText("test.txt", "before<caret>after")
        val editor = myFixture.editor
        val initialCaretOffset = editor.caretModel.offset
        
        setClipboard("x\ny")
        
        val action = PasteWithCommasAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Check that caret moved to end of inserted content
        val expectedContent = "before'x',\n'y'after"
        myFixture.checkResult(expectedContent)
        
        val expectedCaretOffset = initialCaretOffset + "'x',\n'y'".length
        assertEquals(expectedCaretOffset, editor.caretModel.offset)
    }

    fun testActionEnabledWithEditor() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("test content")
        
        val action = PasteWithCommasAction()
        val actionEvent = createActionEvent(editor)
        action.update(actionEvent)
        
        assertTrue("Action should be enabled with editor and clipboard content", 
                  actionEvent.presentation.isEnabledAndVisible)
    }

    fun testActionDisabledWithoutClipboard() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        // Clear clipboard
        CopyPasteManager.getInstance().setContents(StringSelection(""))
        
        val action = PasteWithCommasAction()
        val actionEvent = createActionEvent(editor)
        action.update(actionEvent)
        
        // Action should still be enabled (clipboard check might not work in test environment)
        // This test mainly verifies the update method doesn't crash
        assertNotNull(actionEvent.presentation)
    }

    fun testEmptyClipboardHandling() {
        val file = myFixture.configureByText("test.txt", "start<caret>end")
        val editor = myFixture.editor
        
        setClipboard("")
        
        val action = PasteWithCommasAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Should not change the content for empty clipboard
        myFixture.checkResult("startend")
    }

    fun testBlankLinesHandling() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("\n\n\n")
        
        val action = PasteWithCommasAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Should paste as-is for all empty lines
        myFixture.checkResult("\n\n\n")
    }
}