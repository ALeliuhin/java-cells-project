module org.example.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires dotenv.java;
    requires com.google.gson;
    requires java.sql;
    requires org.postgresql.jdbc;


    exports org.example.finalproject.main;
    opens org.example.finalproject.main to javafx.fxml;
}