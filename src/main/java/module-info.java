module com.example.dsp_additional_task {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.dsp_additional_task to javafx.fxml;
    exports com.example.dsp_additional_task;
}