package com.bindr;

import com.bindr.dto.EstudianteDTO;

public class EntornoData {
    private static EstudianteDTO estudianteActual;
    public static EstudianteDTO getEstudianteActual() {
        return estudianteActual;
    }
    public static void setEstudianteActual(EstudianteDTO estudianteActual) {
        EntornoData.estudianteActual = estudianteActual;
    }
}
