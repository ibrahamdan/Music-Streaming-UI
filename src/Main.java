import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;

public class Main extends Application {

    ObservableList<Track> tracks = FXCollections.observableArrayList();

    ObservableList<Artist> artists = FXCollections.observableArrayList();

    ObservableList<ArtistTrack> artistTrack = FXCollections.observableArrayList();

    TableView<ArtistTrack> playlist = new TableView<>();

    String url = "jdbc:mysql://localhost:3306/tracks";
    String user = "root";
    String password = "Ibraheem2002";
    Connection connection;

    {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Statement sql;

    {
        try {
            sql = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws SQLException {

        ResultSet res = sql.executeQuery("SELECT * FROM artists");
        int artistID;
        String artistName;

        while (res.next()) {
            artistID = res.getInt("artist_id");
            artistName = res.getString("artist_name");
            Artist newArtist = new Artist(artistID, artistName);
            artists.add(newArtist);
        }

        res = sql.executeQuery("SELECT * FROM tracks");
        int trackID;
        String trackTitle;

        while (res.next()){
            trackID = res.getInt("track_id");
            trackTitle = res.getString("track_title");
            artistID = res.getInt("artist_id");
            Track newTrack = new Track(trackID, trackTitle, artistID);
            tracks.add(newTrack);
        }

        ArtistTrack newArtistTrack;
        for (int x = 0; x < tracks.size(); x++) {
            for (int i = 0; i < artists.size(); i++) {
                if (tracks.get(x).getArtistID() == artists.get(i).getArtistID()) {
                    newArtistTrack = new ArtistTrack(tracks.get(x), artists.get(i));
                    artistTrack.add(newArtistTrack);
                }
            }
        }



        stage.setTitle("Playlist");
        Button buttonOne = new Button("+");
        buttonOne.getStyleClass().add("add-button");
        buttonOne.setPrefWidth(75);

        Button logout = new Button("Logout");
        logout.getStyleClass().add("add-button");
        logout.setPrefWidth(75);

        TableColumn<ArtistTrack, String> titleColumn = new TableColumn<>("Track Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("trackTitle"));
        titleColumn.setPrefWidth(125);

        TableColumn<ArtistTrack, String> artistColumn = new TableColumn<>("Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        artistColumn.setPrefWidth(125);

        addButtonToTable();

        playlist.getColumns().add(titleColumn);
        playlist.getColumns().add(artistColumn);

        playlist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        playlist.setItems(artistTrack);

        TextField title = new TextField();
        TextField artist = new TextField();
        title.setPromptText("Title");
        artist.setPromptText("Artist");

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        Button login = new Button("Login");
        login.getStyleClass().add("login-button");

        VBox loginForm = new VBox(10);
        loginForm.getChildren().addAll(username, password, login);
        loginForm.setAlignment(Pos.CENTER);
        StackPane container = new StackPane();
        container.getChildren().add(loginForm);
        StackPane.setMargin(loginForm, new Insets(10));

        HBox controls = new HBox(5);
        controls.getChildren().addAll(buttonOne, logout);
        controls.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(playlist, title, artist, controls);
        vbox.setAlignment(Pos.CENTER);
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(vbox);
        hbox.setAlignment(Pos.CENTER);

        buttonOne.setOnAction(e -> {
            String t = title.getText();
            String a = artist.getText();
            if (t != "" && a != "") {
                int newTrackID;
                int newArtistID;
                boolean artistIsNew = true;

                for (int x = 0; x < artists.size(); x++) {
                    if (a.equals(artists.get(x).getArtistName())) {
                        artistIsNew = false;
                        break;
                    }
                }
                if (artistIsNew == true) {
                    newTrackID = tracks.get(tracks.size() - 1).getTrackID() + 1;
                    newArtistID = artists.get(artists.size() - 1).getArtistID() + 1;
                    Artist newArtist = new Artist(newArtistID, a);
                    artists.add(newArtist);
                    Track newTrack = new Track(newTrackID, t, newArtist.getArtistID());
                    tracks.add(newTrack);
                    ArtistTrack newAT = new ArtistTrack(newTrack, newArtist);
                    artistTrack.add(newAT);
                    try {
                        sql.executeUpdate("INSERT INTO artists VALUES (" + newArtistID + ", '" + a + "')");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        sql.executeUpdate("INSERT INTO tracks VALUES (" + newTrackID + ", '" + t + "', " + newArtistID + ")");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    for (int x = 0; x < artists.size(); x++) {
                        if (a.equals(artists.get(x).getArtistName())) {
                            newTrackID = tracks.get(tracks.size() - 1).getTrackID() + 1;
                            Track newTrack = new Track(newTrackID, t, artists.get(x).getArtistID());
                            tracks.add(newTrack);
                            ArtistTrack newAT = new ArtistTrack(newTrack, artists.get(x));
                            artistTrack.add(newAT);
                            playlist.refresh();
                            try {
                                sql.executeUpdate("INSERT INTO tracks VALUES (" + newTrackID + ", '" + t + "', " + artists.get(x).getArtistID() + ")");
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
                title.clear();
                artist.clear();
            }
        });

        StackPane tableView = new StackPane();
        tableView.getChildren().addAll(hbox);
        StackPane.setAlignment(vbox, Pos.BOTTOM_CENTER);
        BorderPane window = new BorderPane();
        window.setCenter(tableView);
        BorderPane.setMargin(tableView, new Insets(10));
        Scene scene = new Scene(window, 300, 400);

        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        Scene loginPage = new Scene(container, 200, 120);
        loginPage.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(loginPage);

        login.setOnAction(e -> {
            if (username.getText().equals("admin") && password.getText().equals("admin")) {
                username.setText("");
                password.setText("");
                stage.setScene(scene);
            }

            if (username.getText().isEmpty()) {
                username.setStyle("-fx-border-color: red;");
            }

            username.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    username.setStyle("-fx-border-color: transparent;");
                }
            });

            if (password.getText().isEmpty()) {
                password.setStyle("-fx-border-color: red;");
            }

            password.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    password.setStyle("-fx-border-color: transparent;");
                }
            });

            if (!username.getText().isEmpty() && !password.getText().isEmpty() && (!username.getText().equals("admin") || !password.getText().equals("admin"))) {
                Dialog<String[]> dialog = new Dialog();
                dialog.setTitle("Warning");
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                dialog.getDialogPane().getStyleClass().add("dialog-pane");

                Label warning = new Label("Username or Password Incorrect!");
                VBox warningVBox = new VBox();
                warningVBox.getChildren().add(warning);
                warningVBox.setAlignment(Pos.CENTER);
                dialog.getDialogPane().setContent(warningVBox);
                dialog.getDialogPane().getStyleClass().add("dialog-pane");

                warningVBox.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                warningVBox.getStyleClass().add("dialog");

                dialog.show();
            }
        });

        logout.setOnAction(e -> {
            username.setStyle("-fx-border-color: transparent;");
            password.setStyle("-fx-border-color: transparent;");
            stage.setScene(loginPage);
        });

        stage.show();
    }

    private void addButtonToTable() {
        TableColumn<ArtistTrack, Void> colBtn = new TableColumn();

        Callback<TableColumn<ArtistTrack, Void>, TableCell<ArtistTrack, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ArtistTrack, Void> call(final TableColumn<ArtistTrack, Void> param) {
                final TableCell<ArtistTrack, Void> cell = new TableCell<>() {

                    private final Button edit = new Button();
                    private final Button view = new Button();
                    private final Button remove = new Button();

                    {
                        edit.setOnAction((ActionEvent event) -> {
                            ArtistTrack track = getTableView().getItems().get(getIndex());

                            Dialog<String[]> dialog = new Dialog();
                            dialog.setTitle("Edit");
                            dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

                            GridPane grid = new GridPane();
                            Label trackNum = new Label("Edit " + track.getTrackTitle() + " - " + track.getArtistName());
                            trackNum.getStyleClass().add("dialog-label");
                            TextField editTitle = new TextField();
                            editTitle.setText(track.getTrackTitle());
                            editTitle.setPromptText("New Title");
                            editTitle.setPrefWidth(200);
                            TextField editArtist = new TextField();
                            editArtist.setText(track.getArtistName());
                            editArtist.setPrefWidth(200);
                            editArtist.setPromptText("New Artist");
                            VBox editVBox = new VBox(10);
                            editVBox.getChildren().addAll(trackNum, editTitle, editArtist);
                            editVBox.setAlignment(Pos.CENTER);
                            grid.add(editVBox, 0, 1);

                            dialog.getDialogPane().getStyleClass().add("dialog-pane");

                            dialog.getDialogPane().setContent(grid);

                            grid.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                            grid.getStyleClass().add("dialog");

                            dialog.setResultConverter(buttonType -> {
                                if (buttonType == buttonType.APPLY) {
                                    return new String[]{editTitle.getText(), editArtist.getText()};
                                }
                                return null;
                            });

                            ArtistTrack finaltrack = getTableView().getItems().get(getIndex());
                            dialog.showAndWait().ifPresent(result -> {
                                String newTitle = result[0];
                                String newArtist = result[1];

                                if (!newTitle.isEmpty()){
                                    finaltrack.setTrackTitle(newTitle);
                                    finaltrack.getTrack().setTrackTitle(newTitle);
                                    try {
                                        sql.executeUpdate("UPDATE tracks SET track_title = '" + newTitle + "' WHERE track_id = " + finaltrack.getTrackID());
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                if (!newArtist.isEmpty()){
                                    finaltrack.setArtistName(newArtist);
                                    finaltrack.getArtist().setArtistName(newArtist);
                                    try {
                                        sql.executeUpdate("UPDATE artists SET artist_name = '" + newArtist + "' WHERE artist_id = " + finaltrack.getArtistID());
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                            playlist.refresh();
                        });
                    }

                    {
                        view.setOnAction((ActionEvent event) -> {
                            ArtistTrack track = getTableView().getItems().get(getIndex());

                            Dialog<String[]> dialog = new Dialog();
                            dialog.setTitle("View");
                            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

                            Label trackInfo = new Label(track.getTrackTitle() + " - " + track.getArtistName());
                            HBox infoHBox = new HBox();
                            infoHBox.setPrefWidth(250);
                            infoHBox.setPrefHeight(75);
                            infoHBox.getChildren().add(trackInfo);
                            infoHBox.setAlignment(Pos.CENTER);
                            trackInfo.getStyleClass().add("dialog-label");

                            dialog.getDialogPane().getStyleClass().add("dialog-pane");

                            dialog.getDialogPane().setContent(infoHBox);

                            infoHBox.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                            infoHBox.getStyleClass().add("dialog");
                            dialog.show();
                        });
                    }

                    {
                        remove.setOnAction((ActionEvent event) -> {
                            ArtistTrack track = getTableView().getItems().get(getIndex());

                            for (int x = 0; x < artistTrack.size(); x++) {
                                if (artistTrack.get(x).getTrackID() == track.getTrackID()) {
                                    artistTrack.remove(x);
                                }

                                try {
                                    sql.executeUpdate("DELETE FROM tracks WHERE track_id = " + track.getTrackID());
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            edit.getStyleClass().add("edit-button");
                            view.getStyleClass().add("view-button");
                            remove.getStyleClass().add("remove-button");
                            HBox actionsHBox = new HBox(5);
                            actionsHBox.getChildren().addAll(edit, view, remove);
                            setGraphic(actionsHBox);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        colBtn.setPrefWidth(85);

        playlist.getColumns().add(colBtn);
    }
}