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

    // Abrir paquetes para reflexión
    opens com.bindr.persistencia to org.hibernate.orm.core;
    opens com.bindr.controladores to javafx.fxml;
    opens com.bindr.modelos to org.hibernate.orm.core; // <-- Mejor forma
    // opens com.bindr.modelos; // <-- Ya no necesario si usas la línea de arriba

    exports com.bindr;
    exports com.bindr.modelos;
    exports com.bindr.persistencia;
}
