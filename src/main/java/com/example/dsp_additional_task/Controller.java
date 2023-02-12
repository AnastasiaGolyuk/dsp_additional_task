package com.example.dsp_additional_task;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

public class Controller {

    @FXML
    private ChoiceBox<String> coreSizeChoiceBox;

    @FXML
    private ChoiceBox<String> imageChoiceBox;

    @FXML
    private ImageView inputImage;

    @FXML
    private ImageView outputImage;

    @FXML
    void filterImage() throws Throwable {
        Path path = Paths.get(Objects.requireNonNull(getClass().getResource(imageChoiceBox.getValue())).toURI());
        InputStream stream = new FileInputStream(path.toString());
        stream.close();
        BufferedImage bufferedImage = ImageIO.read(new File(path.toString()));
        String sizeWindowStr = coreSizeChoiceBox.getValue().substring(0, 1);
        int sizeWindow = Integer.parseInt(sizeWindowStr);
        MedianFilter medianFilter = new MedianFilter();
        Image result = medianFilter.medianFilter(bufferedImage, sizeWindow);
        outputImage.setImage(result);
    }

    void setInputImage(int index) throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getResource(imageChoiceBox.getItems().get(index))).toURI());
        InputStream stream = new FileInputStream(path.toString());
        Image image = new Image(stream);
        stream.close();
        inputImage.setImage(image);
    }

    @FXML
    void initialize() throws URISyntaxException, IOException {
        coreSizeChoiceBox.getItems().addAll("3x3", "5x5", "7x7");
        imageChoiceBox.getItems().addAll("tiger.png", "butterfly.png", "girl.jpg", "head.jpg", "man.jpg",
                "phone.png", "cars.png", "cat.png", "img.png", "banana.png", "einstein.jpg", "monalisa.png", "woman.jpg");
        coreSizeChoiceBox.setValue(coreSizeChoiceBox.getItems().get(0));
        imageChoiceBox.setValue(imageChoiceBox.getItems().get(0));
        imageChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            try {
                setInputImage(number2.intValue());
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        setInputImage(0);
    }
}
