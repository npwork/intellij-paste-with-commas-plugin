package com.nick.pastewithcommasplugin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PasteWithCommasSimpleActionTest {

    private val action = PasteWithCommasSimpleAction()

    @Test
    fun `test single line input returns unchanged`() {
        val input = "single line"
        val expected = "single line"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test two lines adds comma to first line only`() {
        val input = "line1\nline2"
        val expected = "line1,\nline2"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test three lines adds commas to first two lines`() {
        val input = "a\nb\nc"
        val expected = "a,\nb,\nc"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test numeric lines without quotes`() {
        val input = "1\n2\n3"
        val expected = "1,\n2,\n3"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test text lines without quotes`() {
        val input = "text1\ntext2\ntext3"
        val expected = "text1,\ntext2,\ntext3"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test empty string returns empty string`() {
        val input = ""
        val expected = ""
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test blank string returns unchanged`() {
        val input = "   "
        val expected = "   "
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test lines with existing commas`() {
        val input = "line1,\nline2\nline3"
        val expected = "line1,,\nline2,\nline3"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test lines with empty lines in between`() {
        val input = "line1\n\nline3"
        val expected = "line1,\n,\nline3"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test all empty lines should not add commas`() {
        val input = "\n\n"
        val expected = "\n\n"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test single newline character`() {
        val input = "\n"
        val expected = "\n"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test multiple empty lines`() {
        val input = "\n\n\n\n"
        val expected = "\n\n\n\n"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test mixed numbers and text without intelligence`() {
        val input = "1\ntext\n3.14"
        val expected = "1,\ntext,\n3.14"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test trailing empty lines are removed - simple`() {
        val input = "a\nb\n\n"
        val expected = "a,\nb"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test multiple trailing newlines removed - simple`() {
        val input = "hello\nworld\n\n\n\n"
        val expected = "hello,\nworld"
        val result = action.addCommasToLinesSimple(input)
        assertEquals(expected, result)
    }
}