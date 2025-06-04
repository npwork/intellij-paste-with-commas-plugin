package com.nick.pastewithcommasplugin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PasteWithCommasAndQuotesActionTest {

    private val action = PasteWithCommasAndQuotesAction()

    @Test
    fun `test single line input gets quoted`() {
        val input = "single line"
        val expected = "'single line'"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test two lines gets quotes and comma`() {
        val input = "line1\nline2"
        val expected = "'line1',\n'line2'"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test three lines as per requirement`() {
        val input = "a\nb\nc"
        val expected = "'a',\n'b',\n'c'"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test empty string returns empty string`() {
        val input = ""
        val expected = ""
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test blank string returns unchanged`() {
        val input = "   "
        val expected = "   "
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test lines with existing quotes`() {
        val input = "line1'\nline2\nline3"
        val expected = "'line1'',\n'line2',\n'line3'"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test lines with empty lines in between`() {
        val input = "line1\n\nline3"
        val expected = "'line1',\n'',\n'line3'"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test all empty lines should not add quotes and commas`() {
        val input = "\n\n"
        val expected = "\n\n"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test single newline character should not add quotes`() {
        val input = "\n"
        val expected = "\n"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test multiple empty lines should not add quotes and commas`() {
        val input = "\n\n\n\n"
        val expected = "\n\n\n\n"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test complex example with mixed content`() {
        val input = "firstName\nlastName\nemail\nphoneNumber"
        val expected = "'firstName',\n'lastName',\n'email',\n'phoneNumber'"
        val result = action.addCommasAndQuotesToLines(input)
        assertEquals(expected, result)
    }
}