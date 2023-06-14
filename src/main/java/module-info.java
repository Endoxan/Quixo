module com.example.quixofx2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.quixofx2 to javafx.fxml;
    exports com.example.quixofx2;
}