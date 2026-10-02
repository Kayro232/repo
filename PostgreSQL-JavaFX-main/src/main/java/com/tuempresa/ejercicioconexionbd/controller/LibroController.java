package com.tuempresa.ejercicioconexionbd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.tuempresa.ejercicioconexionbd.connection.DatabaseConnection;
import com.tuempresa.ejercicioconexionbd.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibroController {

    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private ComboBox<String> cmbCategoria;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private TableView<Libro> tblLibro;
    @FXML
    private TableColumn<Libro, String> colId;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAutor;
    @FXML
    private TableColumn<Libro, String> colCategoria;
    @FXML
    private TableColumn<Libro, Double> colPrecio;
    @FXML
    private TableColumn<Libro, Integer> colStock;

    private final ObservableList<Libro> libros = FXCollections.observableArrayList();



    @FXML
    private void initialize() {
        configurarTabla();

        tblLibro.setItems(libros);

        cmbCategoria.getItems().addAll(
                "Cocina",
                "Medicina",
                "Ingenieria",
                "Arquitectura",
                "Pintura"
        );
    }
    private void configurarTabla(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }



    public void clickGuardar(ActionEvent actionEvent) {
        if(!validarCampos()){
            return;
        }
        String sql = "INSERT INTO libro (titulo, autor, categoria, precio, stock)" +
                "VALUES (?,?,?,?,?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, txtTitulo.getText());
            statement.setString(2, txtAutor.getText());
            statement.setString(3, cmbCategoria.getValue());
            statement.setDouble(4, Double.parseDouble(txtPrecio.getText()));
            statement.setInt(5, Integer.parseInt(txtStock.getText()));
            statement.execute();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void clickLimpiar(ActionEvent actionEvent){
        txtTitulo.setText("");
        txtAutor.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        cmbCategoria.getSelectionModel().clearSelection();
    }


    public void clickActualizar(ActionEvent actionEvent) {
        libros.clear();
        String sql = "SELECT * FROM libro";

        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                Libro libro = new Libro();
                libro.setId(resultSet.getInt("id"));
                libro.setTitulo(resultSet.getString("titulo"));
                libro.setAutor(resultSet.getString("autor"));
                libro.setCategoria(resultSet.getString("categoria"));
                libro.setPrecio(resultSet.getDouble("precio"));
                libro.setStock(resultSet.getInt("stock"));
                libros.add(libro);
            }
            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Registro almacenado",
                    "Libro registrado",
                    "El libro se ha almacenado exitosamente");


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validarCampos(){
        return true;
    }
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();

    }
}