package com.nick.pastewithcommasplugin

class PasteWithCommasAndQuotesAction : BasePasteAction() {

    override fun processClipboardContent(input: String): String {
        return addCommasAndQuotesToLines(input)
    }

    fun addCommasAndQuotesToLines(input: String): String {
        return processLinesWithCommas(input) { line, _ ->
            "'$line'"
        }
    }
}