package net.alexanderkahn.longball.service.model

data class GameplayEvent(val pitch: Pitch, val basepathResults: List<BasepathResult> = emptyList()): GameEvent