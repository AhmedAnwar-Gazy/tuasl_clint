module orgs.tuasl_clint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires emojisfx;
    requires mysql.connector.j;
    requires javafx.media;


    opens orgs.tuasl_clint to javafx.fxml;
    exports orgs.tuasl_clint;
    exports orgs.tuasl_clint.controllers;
    opens orgs.tuasl_clint.controllers to javafx.fxml;
    exports orgs.tuasl_clint.models2;
    opens orgs.tuasl_clint.models2 to javafx.fxml;
    exports orgs.tuasl_clint.utils;
    opens orgs.tuasl_clint.utils to javafx.fxml;
    requires org.bytedeco.javacv;

    requires org.bytedeco.opencv;
    requires javafx.swing;


}