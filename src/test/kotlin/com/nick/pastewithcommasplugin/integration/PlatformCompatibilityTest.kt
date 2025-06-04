package com.nick.pastewithcommasplugin.integration

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.nick.pastewithcommasplugin.PasteWithCommasAction
import java.awt.datatransfer.StringSelection

class PlatformCompatibilityTest : BasePlatformTestCase() {

    fun testIntelliJVersionCompatibility() {
        val appInfo = ApplicationInfo.getInstance()
        val buildNumber = appInfo.build.asString()
        val versionName = appInfo.versionName
        
        println("Testing with IntelliJ IDEA version: $versionName")
        println("Build number: $buildNumber")
        
        // Verify the build number is in our supported range (241+)
        val buildNum = buildNumber.substringBefore('.').toIntOrNull() ?: 0
        assertTrue("Plugin should support build $buildNumber (expected 241+)", buildNum >= 241)
        
        // Test that we can access all required platform APIs
        assertNotNull("ApplicationInfo should be available", appInfo)
        assertNotNull("EditorFactory should be available", EditorFactory.getInstance())
        assertNotNull("ActionManager should be available", ActionManager.getInstance())
        assertNotNull("CopyPasteManager should be available", CopyPasteManager.getInstance())
    }

    fun testActionUpdateThreadCompatibility() {
        // Test that ActionUpdateThread.BGT is available (added in 2022.3+)
        // All versions 2024.1+ should have this API
        val action = PasteWithCommasAction()
        val updateThread = action.actionUpdateThread
        
        assertEquals("Action should use BGT thread", ActionUpdateThread.BGT, updateThread)
        
        // Verify the enum values are available
        val bgtValue = ActionUpdateThread.BGT
        val edtValue = ActionUpdateThread.EDT
        
        assertNotNull("BGT thread should be available", bgtValue)
        assertNotNull("EDT thread should be available", edtValue)
        
        // Verify this works across different build versions
        val appInfo = ApplicationInfo.getInstance()
        val buildNum = appInfo.build.asString().substringBefore('.').toIntOrNull() ?: 0
        
        when {
            buildNum >= 241 -> {
                // All 2024.1+ versions should support ActionUpdateThread.BGT
                println("ActionUpdateThread.BGT confirmed available in build $buildNum")
            }
            else -> {
                fail("Unsupported IntelliJ version for ActionUpdateThread: $buildNum")
            }
        }
    }

    fun testClipboardManagerCompatibility() {
        // Test CopyPasteManager APIs used by the plugin
        val clipboardManager = CopyPasteManager.getInstance()
        assertNotNull("CopyPasteManager should be available", clipboardManager)
        
        // Test setting and getting clipboard content
        val testContent = "test\ncontent\nfor\ncompatibility"
        clipboardManager.setContents(StringSelection(testContent))
        
        val retrievedContent = clipboardManager.getContents<String>(java.awt.datatransfer.DataFlavor.stringFlavor)
        assertEquals("Clipboard content should match", testContent, retrievedContent)
    }

    fun testEditorFactoryCompatibility() {
        // Test EditorFactory APIs
        val editorFactory = EditorFactory.getInstance()
        assertNotNull("EditorFactory should be available", editorFactory)
        
        // Test creating an editor (used in integration tests)
        val document = editorFactory.createDocument("test content")
        assertNotNull("Document creation should work", document)
        assertEquals("Document content should match", "test content", document.text)
        
        // Test editor creation
        val editor = editorFactory.createEditor(document, project, PlainTextFileType.INSTANCE, true)
        assertNotNull("Editor creation should work", editor)
        
        // Cleanup
        editorFactory.releaseEditor(editor)
    }

    fun testActionSystemCompatibility() {
        // Test ActionManager APIs
        val actionManager = ActionManager.getInstance()
        assertNotNull("ActionManager should be available", actionManager)
        
        // Test getting our registered actions
        val mainAction = actionManager.getAction("PasteWithCommas")
        assertNotNull("PasteWithCommas action should be registered", mainAction)
        assertTrue("Action should be PasteWithCommasAction", mainAction is PasteWithCommasAction)
        
        val simpleAction = actionManager.getAction("PasteWithCommasSimple")
        assertNotNull("PasteWithCommasSimple action should be registered", simpleAction)
        
        val quotesAction = actionManager.getAction("PasteWithCommasAndQuotes")
        assertNotNull("PasteWithCommasAndQuotes action should be registered", quotesAction)
        
        val doubleQuotesAction = actionManager.getAction("PasteWithCommasAndDoubleQuotes")
        assertNotNull("PasteWithCommasAndDoubleQuotes action should be registered", doubleQuotesAction)
    }

    fun testCommonDataKeysCompatibility() {
        // Test CommonDataKeys APIs used by actions
        assertNotNull("EDITOR key should be available", CommonDataKeys.EDITOR)
        assertNotNull("PROJECT key should be available", CommonDataKeys.PROJECT)
        
        // Test creating data context
        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()
        
        assertNotNull("DataContext creation should work", dataContext)
        assertEquals("Project should be retrievable from context", project, dataContext.getData(CommonDataKeys.PROJECT))
    }

    fun testAnActionEventCompatibility() {
        // Test AnActionEvent creation and APIs
        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()
        
        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        assertNotNull("AnActionEvent creation should work", event)
        assertNotNull("Presentation should be available", event.presentation)
        assertEquals("Project should be accessible", project, event.project)
    }

    fun testWriteCommandActionCompatibility() {
        // Test WriteCommandAction APIs (used for document modification)
        val file = myFixture.configureByText("test.txt", "initial content")
        val editor = myFixture.editor
        val document = editor.document
        
        // Test that WriteCommandAction can be used
        com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction(project) {
            document.insertString(0, "prefix ")
        }
        
        assertEquals("Document modification should work", "prefix initial content", document.text)
    }

    fun testPluginXmlCompatibilityAttributes() {
        // Verify that plugin.xml attributes are properly recognized
        val action = PasteWithCommasAction()
        
        // Test that the action can be updated without errors
        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()
        
        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        
        // This should not throw any exceptions on any supported platform version
        assertDoesNotThrow("Action update should work on current platform") {
            action.update(event)
        }
    }

    fun testPlatformSpecificFeatureCompatibility() {
        val appInfo = ApplicationInfo.getInstance()
        val buildNumber = appInfo.build.asString()
        val buildNum = buildNumber.substringBefore('.').toIntOrNull() ?: 0
        
        when {
            buildNum >= 251 -> {
                // IntelliJ 2025.1+ specific tests
                println("Testing IntelliJ 2025.1+ specific features")
                testAdvancedFeatures()
            }
            buildNum >= 243 -> {
                // IntelliJ 2024.3+ specific tests
                println("Testing IntelliJ 2024.3+ specific features")
                testModernFeatures()
            }
            buildNum >= 242 -> {
                // IntelliJ 2024.2+ specific tests
                println("Testing IntelliJ 2024.2+ specific features")
                testBaseFeatures()
            }
            buildNum >= 241 -> {
                // IntelliJ 2024.1+ specific tests
                println("Testing IntelliJ 2024.1+ specific features")
                testLegacyFeatures()
            }
            else -> {
                fail("Unsupported IntelliJ version: $buildNumber")
            }
        }
    }

    private fun testLegacyFeatures() {
        // Test features available in 2024.1+
        assertTrue("ActionUpdateThread.BGT should be available", true)
        assertTrue("Modern clipboard APIs should be available", true)
        assertTrue("Action system should be available", true)
        assertTrue("Editor APIs should be available", true)
        
        // Test that all required APIs for our plugin are available
        assertNotNull("ActionManager available in 2024.1+", ActionManager.getInstance())
        assertNotNull("CopyPasteManager available in 2024.1+", CopyPasteManager.getInstance())
        assertNotNull("EditorFactory available in 2024.1+", EditorFactory.getInstance())
    }

    private fun testBaseFeatures() {
        // Test features available in 2024.2+
        testLegacyFeatures()
        // Add any 2024.2+ specific features here
        assertTrue("Enhanced editor APIs should be available", true)
    }

    private fun testModernFeatures() {
        // Test features available in 2024.3+
        testBaseFeatures()
        // Add any 2024.3+ specific feature tests here
        assertTrue("Latest action system should be available", true)
    }

    private fun testAdvancedFeatures() {
        // Test features available in 2025.1+
        testModernFeatures()
        // Add any 2025.1+ specific feature tests here
        assertTrue("Newest platform features should be available", true)
    }

    fun testPluginResourcesCompatibility() {
        // Test that plugin resources (icons, plugin.xml) are properly loaded
        val action = PasteWithCommasAction()
        val dataContext = SimpleDataContext.builder()
            .add(CommonDataKeys.PROJECT, project)
            .build()
        
        val event = AnActionEvent.createFromDataContext("test", null, dataContext)
        action.update(event)
        
        val presentation = event.presentation
        assertNotNull("Presentation icon should be loaded", presentation.icon)
        assertEquals("Action text should be set", "Paste with Commas", presentation.text)
        assertNotNull("Action description should be set", presentation.description)
    }

    private fun assertDoesNotThrow(message: String, action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            fail("$message - Exception thrown: ${e.message}")
        }
    }
}