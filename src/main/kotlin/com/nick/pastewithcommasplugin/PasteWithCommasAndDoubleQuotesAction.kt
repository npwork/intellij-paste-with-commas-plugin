package com.nick.pastewithcommasplugin

class PasteWithCommasAndDoubleQuotesAction : BasePasteAction() {

    override fun processClipboardContent(input: String): String {
        return addCommasAndDoubleQuotesToLines(input)
    }

    fun addCommasAndDoubleQuotesToLines(input: String): String {
        return processLinesWithCommas(input) { line, _ ->
            "\"$line\""
        }
    }
}