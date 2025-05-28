package com.bindr.servicios;

import com.bindr.dao.ValoracionDao;
import com.bindr.dto.ValoracionDTO;
import com.bindr.modelos.Valoracion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la gestión de valoraciones.
 * Permite crear, actualizar, eliminar y consultar valoraciones.
 */
public class ValoracionService {

    /**
     * Crea una nueva valoración.
     * @param dto DTO de la valoración
     * @return true si la valoración se creó correctamente, false en caso contrario
     */
    public static boolean crearValoracion(ValoracionDTO dto) {
        Valoracion valoracion = dto.toEntity();
        return ValoracionDao.crear(valoracion);
    }

    /**
     * Busca una valoración por su identificador.
     * @param id Identificador de la valoración
     * @return ValoracionDTO correspondiente, o null si no existe
     */
    public static ValoracionDTO buscarPorId(Long id) {
        Valoracion valoracion = ValoracionDao.buscarPorId(id);
        return valoracion != null ? ValoracionDTO.fromEntity(valoracion) : null;
    }

    /**
     * Lista todas las valoraciones.
     * @return Lista de ValoracionDTO
     */
    public static List<ValoracionDTO> listarTodas() {
        List<Valoracion> valoraciones = ValoracionDao.listarTodas();
        return valoraciones.stream()
                .map(ValoracionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas las valoraciones hechas por un autor.
     * @param autor Correo del autor
     * @return Lista de ValoracionDTO
     */
    public static List<ValoracionDTO> buscarPorAutor(String autor) {
        List<Valoracion> valoraciones = ValoracionDao.buscarPorAutor(autor);
        return valoraciones.stream()
                .map(ValoracionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una valoración existente.
     * @param dto DTO con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public static boolean actualizarValoracion(ValoracionDTO dto) {
        Valoracion valoracion = dto.toEntity();
        return ValoracionDao.actualizar(valoracion);
    }

    /**
     * Elimina una valoración por su identificador.
     * @param id Identificador de la valoración
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public static boolean eliminarValoracion(Long id) {
        return ValoracionDao.eliminar(id);
    }
}