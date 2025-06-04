package com.nick.pastewithcommasplugin

class PasteWithCommasSimpleAction : BasePasteAction() {

    override fun processClipboardContent(input: String): String {
        return addCommasToLinesSimple(input)
    }

    fun addCommasToLinesSimple(input: String): String {
        return processLinesWithCommas(input) { line, _ ->
            line
        }
    }
}