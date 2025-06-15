module de.hitec.nhplus {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;

    opens de.hitec.nhplus to javafx.fxml;
    opens de.hitec.nhplus.presenter to javafx.fxml;
    opens de.hitec.nhplus.model to javafx.base;

    exports de.hitec.nhplus;
    exports de.hitec.nhplus.presenter;
    exports de.hitec.nhplus.model;
}