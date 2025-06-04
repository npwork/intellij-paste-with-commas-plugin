package com.nick.pastewithcommasplugin.integration

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.nick.pastewithcommasplugin.PasteWithCommasAndDoubleQuotesAction
import com.nick.pastewithcommasplugin.PasteWithCommasAndQuotesAction
import java.awt.datatransfer.StringSelection

class PasteWithQuotesActionsIntegrationTest : BasePlatformTestCase() {

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

    fun testSingleQuotesActionWithNumbers() {
        val file = myFixture.configureByText("test.sql", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("1\n2\n3")
        
        val action = PasteWithCommasAndQuotesAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Single quotes action should always add quotes
        myFixture.checkResult("'1',\n'2',\n'3'")
    }

    fun testSingleQuotesActionWithText() {
        val file = myFixture.configureByText("test.sql", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("firstName\nlastName\nemail")
        
        val action = PasteWithCommasAndQuotesAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        myFixture.checkResult("'firstName',\n'lastName',\n'email'")
    }

    fun testDoubleQuotesActionWithNumbers() {
        val file = myFixture.configureByText("test.json", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("1\n2\n3")
        
        val action = PasteWithCommasAndDoubleQuotesAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        // Double quotes action should always add quotes
        myFixture.checkResult("\"1\",\n\"2\",\n\"3\"")
    }

    fun testDoubleQuotesActionWithText() {
        val file = myFixture.configureByText("test.json", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("id\nname\ncreated_at")
        
        val action = PasteWithCommasAndDoubleQuotesAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        myFixture.checkResult("\"id\",\n\"name\",\n\"created_at\"")
    }

    fun testQuotesWithExistingQuotes() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("it's\ndon't\nwon't")
        
        val singleQuotesAction = PasteWithCommasAndQuotesAction()
        val actionEvent = createActionEvent(editor)
        singleQuotesAction.actionPerformed(actionEvent)
        
        // Should wrap existing quotes in additional quotes
        myFixture.checkResult("'it's',\n'don't',\n'won't'")
    }

    fun testDoubleQuotesWithExistingQuotes() {
        val file = myFixture.configureByText("test.txt", "start<caret>end")
        val editor = myFixture.editor
        
        setClipboard("say \"hello\"\nprint \"world\"")
        
        val doubleQuotesAction = PasteWithCommasAndDoubleQuotesAction()
        val actionEvent = createActionEvent(editor)
        doubleQuotesAction.actionPerformed(actionEvent)
        
        // Should wrap existing double quotes in additional quotes
        myFixture.checkResult("start\"say \"hello\"\",\n\"print \"world\"\"end")
    }

    fun testQuotesActionsWithEmptyLines() {
        val file = myFixture.configureByText("test.txt", "<caret>")
        val editor = myFixture.editor
        
        setClipboard("line1\n\nline3")
        
        // Test single quotes
        val singleQuotesAction = PasteWithCommasAndQuotesAction()
        val actionEvent = createActionEvent(editor)
        singleQuotesAction.actionPerformed(actionEvent)
        
        myFixture.checkResult("'line1',\n'',\n'line3'")
    }

    fun testQuotesActionsCaretMovement() {
        val file = myFixture.configureByText("test.txt", "prefix<caret>suffix")
        val editor = myFixture.editor
        val initialOffset = editor.caretModel.offset
        
        setClipboard("a\nb")
        
        val action = PasteWithCommasAndDoubleQuotesAction()
        val actionEvent = createActionEvent(editor)
        action.actionPerformed(actionEvent)
        
        myFixture.checkResult("prefix\"a\",\n\"b\"suffix")
        
        // Verify caret moved to end of inserted content
        val expectedOffset = initialOffset + "\"a\",\n\"b\"".length
        assertEquals(expectedOffset, editor.caretModel.offset)
    }
}