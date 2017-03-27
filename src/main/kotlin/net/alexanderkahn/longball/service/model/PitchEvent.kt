package net.alexanderkahn.longball.service.model

class PitchEvent(val pitch: Pitch, baseRunningEvents: List<BaseRunningEvent>): GameplayEvent(baseRunningEvents)