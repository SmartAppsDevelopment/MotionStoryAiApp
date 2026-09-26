package com.example.data.model

enum class AnimationCategory {
    ENTRANCE, MOTION, EXIT
}

enum class AnimationType(
    val title: String,
    val category: AnimationCategory
) {
    NONE("None", AnimationCategory.MOTION),
    FADE("Fade", AnimationCategory.ENTRANCE),
    ZOOM_IN("Zoom In", AnimationCategory.ENTRANCE),
    SLIDE_LEFT("Slide Left", AnimationCategory.MOTION),
    SLIDE_RIGHT("Slide Right", AnimationCategory.MOTION),
    SLIDE_UP("Slide Up", AnimationCategory.MOTION),
    SLIDE_DOWN("Slide Down", AnimationCategory.MOTION),
    ROTATE("Rotate", AnimationCategory.MOTION),
    BOUNCE("Bounce", AnimationCategory.MOTION),
    KEN_BURNS("Ken Burns", AnimationCategory.MOTION),
    PAN_LEFT("Pan Left", AnimationCategory.MOTION),
    PAN_RIGHT("Pan Right", AnimationCategory.MOTION),
    SLOW_ZOOM("Slow Zoom", AnimationCategory.MOTION),
    DYNAMIC_ZOOM("Dynamic Zoom", AnimationCategory.MOTION),
    FADE_OUT("Fade Out", AnimationCategory.EXIT),
    ZOOM_OUT("Zoom Out", AnimationCategory.EXIT),
    SLIDE_OUT("Slide Out", AnimationCategory.EXIT);
}

enum class TransitionType(val title: String) {
    NONE("None"),
    FADE("Fade"),
    DISSOLVE("Dissolve"),
    SLIDE("Slide"),
    PUSH("Push"),
    ZOOM("Zoom"),
    BLUR("Blur"),
    WIPE("Wipe"),
    CIRCLE("Circle"),
    SPIN("Spin")
}

enum class FilterType(val title: String) {
    ORIGINAL("Original"),
    VIVID("Vivid"),
    WARM("Warm"),
    COOL("Cool"),
    VINTAGE("Vintage"),
    CINEMATIC("Cinematic"),
    BW("B&W"),
    DRAMATIC("Dramatic")
}
