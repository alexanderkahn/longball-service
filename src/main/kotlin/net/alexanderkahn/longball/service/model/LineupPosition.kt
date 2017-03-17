package net.alexanderkahn.longball.service.model

class LineupPosition(player: Player) {
    private var players: MutableList<Pair<Player, GameEvent?>> = ArrayList()

    init {
        players.add(Pair(player, null))
    }

}

