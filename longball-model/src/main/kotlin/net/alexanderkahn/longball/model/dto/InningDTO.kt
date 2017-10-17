package net.alexanderkahn.longball.model.dto

data class InningDTO(val inningNumber: Int, val top: InningSideDTO, val bottom: InningSideDTO? = null)