package net.alexanderkahn.longball.service.service.assembler

import net.alexanderkahn.longball.service.persistence.model.entity.PxInningHalf
import net.alexanderkahn.longball.service.persistence.model.entity.PxInningHalfResult

fun PxInningHalf.toResult(): PxInningHalfResult {
    return PxInningHalfResult(null, 0, this)
}