package com.nick.pastewithcommasplugin.integration

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.nick.pastewithcommasplugin.BasePasteAction
import com.nick.pastewithcommasplugin.PasteWithCommasAction
import com.nick.pastewithcommasplugin.PasteWithCommasAndDoubleQuotesAction
import com.nick.pastewithcommasplugin.PasteWithCommasAndQuotesAction
import com.nick.pastewithcommasplugin.PasteWithCommasSimpleAction

class MenuIntegrationTest : BasePlatformTestCase() {

    fun testMainActionIsRegistered() {
        val actionManager = ActionManager.getInstance()
        val action = actionManager.getAction("PasteWithCommas")

        assertNotNull("PasteWithCommas action should be registered", action)
        assertTrue("Action should be instance of PasteWithCommasAction",
                  action is PasteWithCommasAction
        )
    }

    fun testSimpleActionIsRegistered() {
        val actionManager = ActionManager.getInstance()
        val action = actionManager.getAction("PasteWithCommasSimple")

        assertNotNull("PasteWithCommasSimple action should be registered", action)
        assertTrue("Action should be instance of PasteWithCommasSimpleAction",
                  action is PasteWithCommasSimpleAction
        )
    }

    fun testQuotesActionIsRegistered() {
        val actionManager = ActionManager.getInstance()
        val action = actionManager.getAction("PasteWithCommasAndQuotes")

        assertNotNull("PasteWithCommasAndQuotes action should be registered", action)
        assertTrue("Action should be instance of PasteWithCommasAndQuotesAction",
                  action is PasteWithCommasAndQuotesAction
        )
    }

    fun testDoubleQuotesActionIsRegistered() {
        val actionManager = ActionManager.getInstance()
        val action = actionManager.getAction("PasteWithCommasAndDoubleQuotes")

        assertNotNull("PasteWithCommasAndDoubleQuotes action should be registered", action)
        assertTrue("Action should be instance of PasteWithCommasAndDoubleQuotesAction",
                  action is PasteWithCommasAndDoubleQuotesAction
        )
    }

    fun testSubMenuGroupIsRegistered() {
        val actionManager = ActionManager.getInstance()
        val group = actionManager.getAction("PasteWithCommasAdditional")

        assertNotNull("PasteWithCommasAdditional group should be registered", group)
        assertTrue("Should be an ActionGroup", group is ActionGroup)

        val actionGroup = group as ActionGroup
        assertTrue("Group should be a popup", actionGroup.isPopup)
    }

    fun testMainActionProperties() {
        val actionManager = ActionManager.getInstance()
        val action = actionManager.getAction("PasteWithCommas")
        assertNotNull(action)

        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()

        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        action.update(event)

        val presentation = event.presentation
        assertEquals("Paste with Commas", presentation.text)
        assertEquals("Intelligent paste: uses commas for numbers, single quotes + commas for text",
                    presentation.description)
        assertNotNull("Icon should be set", presentation.icon)
    }

    fun testSubMenuActionProperties() {
        val actionManager = ActionManager.getInstance()

        // Test simple action properties
        val simpleAction = actionManager.getAction("PasteWithCommasSimple")
        assertNotNull(simpleAction)

        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()

        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        simpleAction.update(event)

        val presentation = event.presentation
        assertEquals("Paste with Commas (simple)", presentation.text)
        assertTrue("Description should mention no quotes",
                  presentation.description?.contains("no quotes") == true)
    }

    fun testQuotesActionProperties() {
        val actionManager = ActionManager.getInstance()
        val quotesAction = actionManager.getAction("PasteWithCommasAndQuotes")
        assertNotNull(quotesAction)

        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()

        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        quotesAction.update(event)

        val presentation = event.presentation
        assertEquals("Paste with Commas + '", presentation.text)
        assertTrue("Description should mention single quotes",
                  presentation.description?.contains("single quotes") == true)
        assertNotNull("Icon should be set", presentation.icon)
    }

    fun testDoubleQuotesActionProperties() {
        val actionManager = ActionManager.getInstance()
        val doubleQuotesAction = actionManager.getAction("PasteWithCommasAndDoubleQuotes")
        assertNotNull(doubleQuotesAction)

        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()

        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        doubleQuotesAction.update(event)

        val presentation = event.presentation
        assertEquals("Paste with Commas + \"", presentation.text)
        assertTrue("Description should mention double quotes",
                  presentation.description?.contains("double quotes") == true)
        assertNotNull("Icon should be set", presentation.icon)
    }

    fun testSubMenuGroupChildren() {
        val actionManager = ActionManager.getInstance()
        val group = actionManager.getAction("PasteWithCommasAdditional") as ActionGroup

        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()

        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        val children = group.getChildren(event)

        assertEquals("Submenu should have 3 children", 3, children.size)

        // Verify the order and types of children
        assertTrue("First child should be simple action",
                  children[0] is PasteWithCommasSimpleAction
        )
        assertTrue("Second child should be single quotes action",
                  children[1] is PasteWithCommasAndQuotesAction
        )
        assertTrue("Third child should be double quotes action",
                  children[2] is PasteWithCommasAndDoubleQuotesAction
        )
    }

    fun testEditorPopupMenuGroupExists() {
        val actionManager = ActionManager.getInstance()
        val editorPopupGroup = actionManager.getAction("EditorPopupMenu")

        assertNotNull("EditorPopupMenu group should exist", editorPopupGroup)
        assertTrue("Should be an ActionGroup", editorPopupGroup is ActionGroup)
    }

    fun testActionUpdateThreadConfiguration() {
        val actions = listOf(
            ActionManager.getInstance().getAction("PasteWithCommas"),
            ActionManager.getInstance().getAction("PasteWithCommasSimple"),
            ActionManager.getInstance().getAction("PasteWithCommasAndQuotes"),
            ActionManager.getInstance().getAction("PasteWithCommasAndDoubleQuotes")
        )

        actions.forEach { action ->
            assertNotNull("Action should not be null", action)
            if (action is BasePasteAction) {
                assertEquals("Action should use BGT thread",
                           ActionUpdateThread.BGT, action.actionUpdateThread)
            }
        }
    }

    fun testActionVisibilityWithoutEditor() {
        val actionManager = ActionManager.getInstance()
        val action = actionManager.getAction("PasteWithCommas")

        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            // No editor in context
            .build()

        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        action.update(event)

        // Action should handle missing editor gracefully
        assertNotNull("Presentation should not be null", event.presentation)
    }
}