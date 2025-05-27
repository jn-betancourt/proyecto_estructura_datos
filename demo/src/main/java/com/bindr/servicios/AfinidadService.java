package com.bindr.servicios;

import com.bindr.modelos.Estudiante;
import com.bindr.modelos.Publicacion;
import com.bindr.modelos.SolicitudAyuda;
import com.bindr.modelos.Mensaje;
import com.bindr.modelos.GrupoEstudio;
import com.bindr.modelos.Conversacion;
import com.bindr.utils.GrafoAfinidadEstudiantes;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio privado encargado de gestionar la red de afinidad entre estudiantes.
 * Utiliza un grafo no dirigido para representar y actualizar las relaciones de afinidad.
 *
 */
public class AfinidadService {

    private final GrafoAfinidadEstudiantes grafo = new GrafoAfinidadEstudiantes();

    /**
     * Agrega un estudiante a la red de afinidad.
     * Si ya existe, no realiza ninguna acción.
     * @param estudiante Estudiante a agregar
     */
    public void agregarEstudiante(Estudiante estudiante) {
        grafo.agregarEstudiante(estudiante);
    }

    /**
     * Registra una interacción entre dos estudiantes, aumentando la afinidad entre ellos.
     * Si la relación no existe, la crea con peso 1; si existe, incrementa el peso.
     * @param e1 Primer estudiante
     * @param e2 Segundo estudiante
     */
    public void registrarInteraccion(Estudiante e1, Estudiante e2) {
        grafo.agregarRelacion(e1, e2);
    }

    /**
     * Reconstruye el grafo de afinidad a partir de los datos históricos de publicaciones, solicitudes de ayuda,
     * mensajes y grupos de estudio.
     * Este método limpia el grafo actual y lo reconstruye desde cero.
     */
   public void reconstruirGrafo(List<Estudiante> estudiantes,
                             List<SolicitudAyuda> solicitudes,
                             List<GrupoEstudio> grupos,
                             List<Publicacion> publicaciones,
                             List<Conversacion> conversaciones) {
        grafo.vaciar();

        // Mapa auxiliar para buscar estudiantes por correo
        Map<String, Estudiante> estudiantesPorCorreo = estudiantes.stream()
            .collect(Collectors.toMap(Estudiante::getCorreo, e -> e));

        for (Estudiante estudiante : estudiantes) {
            agregarEstudiante(estudiante);
        }

        for (SolicitudAyuda solicitud : solicitudes) {
            if (solicitud.getRemitente() != null && solicitud.getDestinatario() != null) {
                registrarInteraccion(solicitud.getRemitente(), solicitud.getDestinatario());
            }
        }

        // Procesa conversaciones y sus mensajes
        for (Conversacion conversacion : conversaciones) {
            List<Estudiante> participantes = conversacion.getParticipantes();
            if (conversacion.getMensajes() != null) {
                for (Mensaje mensaje : conversacion.getMensajes()) {
                    Estudiante autor = mensaje.getAutor();
                    if (autor == null) continue;
                    for (Estudiante participante : participantes) {
                        if (!participante.equals(autor)) {
                            registrarInteraccion(autor, participante);
                        }
                    }
                }
            }
        }

        // Procesa grupos de estudio (todos los pares de estudiantes en el mismo grupo)
        for (GrupoEstudio grupo : grupos) {
            List<Estudiante> miembros = grupo.getEstudiantes();
            for (int i = 0; i < miembros.size(); i++) {
                for (int j = i + 1; j < miembros.size(); j++) {
                    registrarInteraccion(miembros.get(i), miembros.get(j));
                }
            }
        }

        // Procesa publicaciones (autor y quienes valoraron/comentaron)
        for (Publicacion publicacion : publicaciones) {
            Estudiante correoAutor = publicacion.getPublicador(); // ahora es String
            Estudiante autor = estudiantesPorCorreo.get(correoAutor.getCorreo());
            if (autor == null) continue;
            publicacion.getValoraciones().forEach(valoracion -> {
                String correoEvaluador = valoracion.getAutor(); // ahora es String
                Estudiante evaluador = estudiantesPorCorreo.get(correoEvaluador);
                if (evaluador != null && !evaluador.equals(autor)) {
                    registrarInteraccion(autor, evaluador);
                }
            });
        }
    }
    
    /**
     * Obtiene los estudiantes más afines a un estudiante dado.
     * @param estudiante Estudiante de referencia
     * @return Mapa de estudiantes adyacentes y el peso de la relación (mayor peso = mayor afinidad)
     */
    public Map<Estudiante, Integer> obtenerAfinidades(Estudiante estudiante) {
        return grafo.obtenerAdyacentes(estudiante);
    }

    /**
     * Verifica si dos estudiantes están conectados en la red de afinidad.
     * @param e1 Primer estudiante
     * @param e2 Segundo estudiante
     * @return true si existe una relación, false en caso contrario
     */
    public boolean estanConectados(Estudiante e1, Estudiante e2) {
        return grafo.estanConectados(e1, e2);
    }

    /**
     * Obtiene todos los estudiantes presentes en la red de afinidad.
     * @return Conjunto de estudiantes
     */
    public Set<Estudiante> obtenerTodosEstudiantes() {
        return grafo.obtenerEstudiantes();
    }

    public List<Estudiante> sugerirMasAfines(Estudiante estudiante, int max) {
        return obtenerAfinidades(estudiante).entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
            .limit(max)
            .map(Map.Entry::getKey)
            .toList();
    }
}
