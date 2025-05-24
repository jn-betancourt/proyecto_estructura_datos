module com.bindr {
    requires javafx.controls;
    requires javafx.fxml;

    // JDBC y SQLite
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    // Hibernate y JPA
    requires jakarta.persistence;
    requires org.hibernate.orm.core; // <-- Faltaba esto
    requires org.hibernate.orm.community.dialects;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // Abrir paquetes para reflexión
    opens com.bindr.persistencia to org.hibernate.orm.core;
    opens com.bindr.controladores to javafx.fxml;
    opens com.bindr.modelos to org.hibernate.orm.core, com.fasterxml.jackson.databind; // <-- Mejor forma
    // opens com.bindr.modelos; // <-- Ya no necesario si usas la línea de arriba
    
    exports com.bindr.converters to org.hibernate.orm.core;
    exports com.bindr;
    exports com.bindr.modelos;
    exports com.bindr.persistencia;
    exports com.bindr.servicios;
}
