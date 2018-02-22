package net.dankito.richtexteditor.java.fx.toolbar

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.text.Font
import net.dankito.richtexteditor.java.fx.JavaFXIcon
import net.dankito.richtexteditor.java.fx.JavaFXJavaScriptExecutor
import net.dankito.richtexteditor.java.fx.localization.Localization
import tornadofx.*


class SearchView(private val searchViewStyle: SearchViewStyle, private val localization: Localization) : View() {


    var executor: JavaFXJavaScriptExecutor? = null


    private val searchText = SimpleStringProperty("")

    private val matchCase = SimpleBooleanProperty(true)

    private val enableButtonsPreviousNextSearchResult = SimpleBooleanProperty(false)


    init {
        searchText.addListener { _, _, _ -> search() }
    }


    override val root = hbox {
        alignment = Pos.CENTER_LEFT

        minHeight = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()
        maxHeight = minHeight

        textfield(searchText) {
            font = Font.font(searchViewStyle.searchFieldFontSize)

            minHeight = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()
            maxHeight = minHeight
            prefWidth = searchViewStyle.searchFieldWidth
        }

        button("", createJumpToPreviousNextResultIcon(searchViewStyle.jumpToPreviousResultIcon)) {
            minHeight = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()
            maxHeight = minHeight
            prefWidth = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()

            disableProperty().bind(enableButtonsPreviousNextSearchResult.not())

            action { search(false) }

            hboxConstraints {
                marginLeft = 2.0
            }
        }

        button("", createJumpToPreviousNextResultIcon(searchViewStyle.jumpToNextResultIcon)) {
            minHeight = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()
            maxHeight = minHeight
            prefWidth = searchViewStyle.toolbarCommandStyle.heightDp.toDouble()

            disableProperty().bind(enableButtonsPreviousNextSearchResult.not())

            action { search() }

            hboxConstraints {
                marginLeft = 2.0
                marginRight = 6.0
            }
        }

        checkbox(localization.getLocalizedString("search.match.case"), matchCase)
    }

    private fun createJumpToPreviousNextResultIcon(icon: JavaFXIcon): Node {
        return ImageView(Image(icon.url, searchViewStyle.jumpToPreviousNextResultIconWidth, searchViewStyle.jumpToPreviousNextResultIconWidth, true, true))
    }


    private fun search(forward: Boolean = true) {
        executor?.page?.let { page ->
            enableButtonsPreviousNextSearchResult.value = page.find(searchText.value, forward, true, matchCase.value)
        }
    }

}