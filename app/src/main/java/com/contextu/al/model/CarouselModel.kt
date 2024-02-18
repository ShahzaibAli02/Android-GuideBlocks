package com.contextu.al.model

data class CarouselModel(
    val items: List<Item> = emptyList(),
    val radius: Float = 0f,
) : BaseView() {
    data class Item(
        val title: TextModel,
        val content: TextModel? = null,
        val buttonModel: ButtonModel? = null,
        val image: String? = null
    ): BaseView()
}
