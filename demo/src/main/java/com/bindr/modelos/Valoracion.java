package com.bindr.modelos;

import java.time.LocalDate;


// Clase Valoracion
public class Valoracion {
    private Long id; // Nuevo campo
    private String autor;
    private boolean like;
    private LocalDate fecha;

    public Valoracion() {
        // Constructor por defecto
    }
    // Constructor
    private Valoracion(Long id, String autor, boolean like, LocalDate fecha) {
        this.id = id;
        this.autor = autor;
        this.like = like;
        this.fecha = fecha;
    }

    // getters y setters
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public boolean isLike() {
        return like;
    }
    public void setLike(boolean like) {
        this.like = like;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    @Override
    public String toString() {
        return "Valoracion{" +
                "id=" + id +
                ", autor=" + autor +
                ", like=" + like +
                ", fecha=" + fecha +
                '}';
    }

    // builder
    public static ValoracionBuilder builder() {
        return new ValoracionBuilder();
    }

    //clase ValoracionBuilder
    public static class ValoracionBuilder {
        private Long id;
        private String autor;
        private boolean like;
        private LocalDate fecha;

        public ValoracionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ValoracionBuilder autor(String autor) {
            this.autor = autor;
            return this;
        }

        public ValoracionBuilder like(boolean like) {
            this.like = like;
            return this;
        }

        public ValoracionBuilder fecha(LocalDate fecha) {
            this.fecha = fecha;
            return this;
        }

        public Valoracion build() {
            return new Valoracion(id, autor, like, fecha);
        }
    }

}