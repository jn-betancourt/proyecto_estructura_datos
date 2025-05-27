package com.bindr;

import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;

import java.util.ArrayList;
import java.util.List;

public class EntornoData {
    private static EstudianteDTO estudianteActual;
    private static List<ConversacionDTO> conversaciones;
    public static List<ConversacionDTO> getConversaciones() {
        return conversaciones;
    }
    public static EstudianteDTO getEstudianteActual() {
        return estudianteActual;
    }
    public static void setEstudianteActual(EstudianteDTO estudianteActual) {
        EntornoData.estudianteActual = estudianteActual;
    }

    public static void setConversaciones(List<ConversacionDTO> conversacionDTOS) {
        conversaciones = new ArrayList<>(conversacionDTOS);
        if (conversaciones.size() > 0) {
            setEstudianteActual(conversaciones.get(0).participantes().get(0));
        } else {
            setEstudianteActual(null);
        }
    }
}
