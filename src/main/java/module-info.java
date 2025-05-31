module orgs.tuasl_clint {
    requires javafx.controls;
    requires javafx.fxml;


    opens orgs.tuasl_clint to javafx.fxml;
    exports orgs.tuasl_clint;
}