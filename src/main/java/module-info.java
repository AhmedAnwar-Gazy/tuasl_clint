module orgs.tuasl_clint {
    requires javafx.fxml;
    requires java.desktop;
    requires emojisfx;
    requires mysql.connector.j;
    requires javafx.media;


    opens orgs.tuasl_clint to javafx.fxml;
    exports orgs.tuasl_clint;
    exports orgs.tuasl_clint.controllers;
    opens orgs.tuasl_clint.controllers to javafx.fxml;
    exports orgs.tuasl_clint.models2;
    opens orgs.tuasl_clint.models2 to javafx.fxml, com.google.gson;
    exports orgs.tuasl_clint.utils;
    opens orgs.tuasl_clint.utils to javafx.fxml;
    requires org.bytedeco.javacv;

    requires org.bytedeco.opencv;
    requires javafx.swing;
    requires org.bytedeco.libfreenect;
    requires com.google.gson;
    requires java.sql;
    requires com.gluonhq.richtextarea;
    requires javafx.controls;

    opens orgs.tuasl_clint.protocol to com.google.gson; // Add this line


}