package com.emiliano.turnosOdontologico.dto.TurnoDTO;

import java.time.LocalDate;

public record ResumenTurnosPorEstadoDTO(
        LocalDate fecha,
        long cantidadPendientes,
        long cantidadConfirmados,
        long cantidadCancelados,
        long cantidadAtendidos
) {
}
