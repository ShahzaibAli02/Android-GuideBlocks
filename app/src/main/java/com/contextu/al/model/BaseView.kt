package com.contextu.al.model

open class BaseView {
    var height: String = "0"
    var width: String = "0"
    var cornerRadius: Int = 0
    val border: Border = Border()
    var padding: Margin = Margin()
    var margin: Margin = Margin()
    var backGroundColor = 0
    val alignment: String = Alignments.center.name
}
