import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

public class TableToTableTransitionController {

    @FXML
    private AnchorPane rootAnchorPane;

    // oberer Bereich zum Editieren der vorhandenen Berechtigungen:

    @FXML
    private TableView<UserFXVO> tableView1;

    @FXML
    private TableColumn<UserFXVO, String> nameCol1;
    @FXML
    private TableColumn<UserFXVO, String> userIdCol1;
    @FXML
    private TableColumn<UserFXVO, String> actionsCol1;

    ObservableList<UserFXVO> observableList1;

    // unterer Bereich zum Hinzufuegen neuer berechtigter Anwender:

    @FXML
    private TableView<UserFXVO> tableView2;
    @FXML
    private TableColumn<UserFXVO, String> nameCol2;
    @FXML
    private TableColumn<UserFXVO, String> userIdCol2;
    @FXML
    private TableColumn<UserFXVO, String> actionsCol2;

    private ObservableList<UserFXVO> observableList2;

    @FXML
    void initialize() {

        nameCol1.setCellValueFactory(new PropertyValueFactory<UserFXVO, String>("name"));
        userIdCol1.setCellValueFactory(new PropertyValueFactory<UserFXVO, String>("userId"));

        ZugriffsrechteFuerProjektSetzenFactorys.TableViewActionColumnFactory cellFactory = new ZugriffsrechteFuerProjektSetzenFactorys.TableViewActionColumnFactory(
                this, "down");
        actionsCol1.setCellFactory(cellFactory);

        nameCol2.setCellValueFactory(new PropertyValueFactory<UserFXVO, String>("name"));
        userIdCol2.setCellValueFactory(new PropertyValueFactory<UserFXVO, String>("userId"));

        cellFactory = new ZugriffsrechteFuerProjektSetzenFactorys.TableViewActionColumnFactory(
                this, "up");
        actionsCol2.setCellFactory(cellFactory);

        observableList1 = FXCollections.observableArrayList(
                new UserFXVO("Name", "UserID"),
                new UserFXVO("Name", "UserID")
        );
        tableView1.setItems(observableList1);

        observableList2 = FXCollections.observableArrayList(
                new UserFXVO("Name", "UserID"),
                new UserFXVO("Name", "UserID")
        );
        tableView2.setItems(observableList2);

    }


    void moveUp(final UserFXVO vo) {

        // Position und Snapshot des Eintrages in der unteren Tabelle merken
        TableRow<UserFXVO> rowUntereTabelle = ermittleRow(tableView2, vo);
        double yUntereTabelle = rowUntereTabelle.localToScene(0, 0).getY();
        WritableImage snapshot = rowUntereTabelle.snapshot(new SnapshotParameters(), null);

        // Eintrag in unterer Tabelle entfernen
        observableList2.remove(vo);

        // Neues VO in obere Tabelle einfuegen (betrachtete Eintrag ist kurzzeitig in beiden Tabellen vorhanden!)
        final UserFXVO newVO = new UserFXVO(vo.getName(), vo.getUserId());
        observableList1.add(newVO);

        // Neuen Eintrag in oberer Tabelle selektieren und hinscrollen
        tableView1.getSelectionModel().select(newVO);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int index = observableList1.indexOf(newVO);
                tableView1.scrollTo(index);
            }
        });

        // Animation
        TableRow<UserFXVO> rowObereTabelle = ermittleRow(tableView1, newVO);
        double yObereTabelle = rowObereTabelle.localToScene(0, 0).getY();
        animationUebergangVonUntererInObereTabelle(yObereTabelle, yUntereTabelle, snapshot);
    }

    void moveDown(final UserFXVO vo) {

        // Position und Snapshot des Eintrages in der oberen Tabelle merken
        TableRow<UserFXVO> rowObereTabelle = ermittleRow(tableView1, vo);
        double yObereTabelle = rowObereTabelle.localToScene(0, 0).getY();
        WritableImage snapshot = rowObereTabelle.snapshot(new SnapshotParameters(), null);

        // Eintrag in oberer Tabelle entfernen
        observableList1.remove(vo);

        // Neues VO in unterer Tabelle einfuegen (betrachtete Eintrag ist kurzzeitig in beiden Tabellen vorhanden!)
        final UserFXVO newVO = new UserFXVO(vo.getName(), vo.getUserId());
        observableList2.add(newVO);

        // Neuen Eintrag in unterer Tabelle selektieren und hinscrollen
        tableView2.getSelectionModel().select(newVO);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int index = observableList2.indexOf(newVO);
                tableView2.scrollTo(index);
            }
        });

        // Animation
        TableRow<UserFXVO> rowUntereTabelle = ermittleRow(tableView2, newVO);
        double yUntereTabelle = rowUntereTabelle.localToScene(0, 0).getY();
        animationUebergangVonUntererInObereTabelle(yUntereTabelle, yObereTabelle,  snapshot);
    }

    /**
     * Animiert den Uebergang eines Eintrages von der Tabelle der fuer die Berechtigung in Frage kommenden User in die
     * Tabelle der berechtigten User indem ein Snapshot vertikal von einer y-Position zu einer anderen geschoben wird.
     *
     * @param y1       Y-Koordinate des Ausgangspunktes
     * @param y2       Y-Koordinate des Zielpunktes
     * @param snapshot welcher von y1 nach y2 verschoben werden soll
     */
    private void animationUebergangVonUntererInObereTabelle(final double y1, final double y2, final WritableImage snapshot) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                final FlowPane overlayPane = new FlowPane();
                overlayPane.setStyle("-fx-border-width: 5px; -fx-border-color: lightblue");
                overlayPane.getChildren().add(new ImageView(snapshot));

                rootAnchorPane.getChildren().add(overlayPane);

                Timeline timeline = new Timeline();

                KeyValue keyValueOpacity1 = new KeyValue(overlayPane.opacityProperty(), 0);
                KeyValue keyValueY1 = new KeyValue(overlayPane.translateYProperty(), y2);
                KeyFrame kf1 = new KeyFrame(Duration.millis(0), keyValueOpacity1, keyValueY1);

                KeyValue keyValueOpacity2 = new KeyValue(overlayPane.opacityProperty(), 1);
                KeyValue keyValueY2 = new KeyValue(overlayPane.translateYProperty(), y1);
                KeyFrame kf2 = new KeyFrame(Duration.millis(500), keyValueOpacity2, keyValueY2);

                timeline.getKeyFrames().addAll(kf1, kf2);
                timeline.play();
                timeline.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        rootAnchorPane.getChildren().remove(overlayPane);
                        tableView1.requestFocus();
                    }
                });
            }
        });
    }

    /**
     * Ermittelt die {@link TableRow} eines bestimmten Tabelleneintrages.
     *
     * @param <ROWVO> Typ des Objektes einer Tabellenzeile
     * @param tabelle aus welcher die Row ermittelt werden soll
     * @param vo      Tabelleneintrag
     * @return TableRow oder null, falls diese nicht gefunden werden konnte
     */
    private <ROWVO> TableRow<ROWVO> ermittleRow(TableView<ROWVO> tabelle, final ROWVO vo) {
        ObservableList<ROWVO> items = tabelle.getItems();
        for (Node n : tabelle.lookupAll("TableRow")) {
            if (n instanceof TableRow) {

                @SuppressWarnings("unchecked")
                TableRow<ROWVO> row = (TableRow<ROWVO>) n;

                if (items.size() > row.getIndex() && items.get(row.getIndex()).equals(vo)) {
                    return row;
                }
            }
        }
        return null;
    }
}
