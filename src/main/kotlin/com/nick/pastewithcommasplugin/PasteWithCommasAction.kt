package com.nick.pastewithcommasplugin

class PasteWithCommasAction : BasePasteAction() {

    override fun processClipboardContent(input: String): String {
        return addCommasToLines(input)
    }

    fun addCommasToLines(input: String): String {
        return processLinesWithCommas(input) { line, _ ->
            val lines = input.split('\n')
            val nonEmptyLines = lines.filter { it.isNotEmpty() }
            val allLinesAreNumbers = nonEmptyLines.isNotEmpty() && nonEmptyLines.all { isNumeric(it.trim()) }
            
            if (allLinesAreNumbers) {
                line // Don't add quotes for numbers
            } else {
                "'$line'" // Add single quotes for text
            }
        }
    }
}