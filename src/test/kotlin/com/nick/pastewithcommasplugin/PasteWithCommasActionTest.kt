package com.nick.pastewithcommasplugin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PasteWithCommasActionTest {

    private val action = PasteWithCommasAction()

    @Test
    fun `test single text line gets quoted`() {
        val input = "single line"
        val expected = "'single line'"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test single number line without quotes`() {
        val input = "42"
        val expected = "42"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test two text lines adds quotes and commas`() {
        val input = "line1\nline2"
        val expected = "'line1',\n'line2'"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test three text lines adds quotes and commas`() {
        val input = "a\nb\nc"
        val expected = "'a',\n'b',\n'c'"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test numeric lines without quotes`() {
        val input = "1\n2\n3"
        val expected = "1,\n2,\n3"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test decimal numbers without quotes`() {
        val input = "1.5\n2.7\n3.14"
        val expected = "1.5,\n2.7,\n3.14"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test mixed content uses quotes for all`() {
        val input = "1\ntext\n3"
        val expected = "'1',\n'text',\n'3'"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test empty string returns empty string`() {
        val input = ""
        val expected = ""
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test blank string returns unchanged`() {
        val input = "   "
        val expected = "   "
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test lines with existing commas get quoted`() {
        val input = "line1,\nline2\nline3"
        val expected = "'line1,',\n'line2',\n'line3'"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test lines with empty lines in between get quoted`() {
        val input = "line1\n\nline3"
        val expected = "'line1',\n'',\n'line3'"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test negative numbers without quotes`() {
        val input = "-1\n-2.5\n-10"
        val expected = "-1,\n-2.5,\n-10"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test numbers with spaces stay as numbers`() {
        val input = " 1 \n 2 \n 3 "
        val expected = " 1 ,\n 2 ,\n 3 "
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test all empty lines should not add commas`() {
        val input = "\n\n"
        val expected = "\n\n"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test all empty lines with more newlines should not add commas`() {
        val input = "\n\n\n\n"
        val expected = "\n\n\n\n"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test single newline character`() {
        val input = "\n"
        val expected = "\n"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }

    @Test
    fun `test multiple empty lines`() {
        val input = "\n\n\n"
        val expected = "\n\n\n"
        val result = action.addCommasToLines(input)
        assertEquals(expected, result)
    }
}