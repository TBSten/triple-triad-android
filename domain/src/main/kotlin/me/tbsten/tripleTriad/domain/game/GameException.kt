package me.tbsten.tripleTriad.domain.game

sealed class GameException(message: String) : IllegalStateException(message) {
    class IllegalSquareAccess(label: String) : GameException("不正なマス: $label")
    class IllegalStateTransition(label: String) : GameException("不正な状態遷移: $label")
    class AlreadyPlaced : GameException("すでにカードが置いてある位置にカードを配置することはできません")
    class IllegalPlayer(playerLabel: String, message: String? = null) :
        GameException(
            "${playerLabel}が不正${if (message != null) ", $message" else ""}",
        )
}
