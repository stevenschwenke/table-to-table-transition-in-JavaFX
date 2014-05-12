import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

public class ColumnFactory implements Callback<TableColumn<UserFX, String>, TableCell<UserFX, String>> {

    private TableToTableTransitionController controller;

    private String upOrDown;

    public ColumnFactory(TableToTableTransitionController controller, String upOrDown) {
        super();
        this.controller = controller;
        this.upOrDown = upOrDown;
    }

    @Override
    public TableCell<UserFX, String> call(TableColumn<UserFX, String> arg0) {
        TableCell<UserFX, String> myTableCell = new TableCell<UserFX, String>(
        ) {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!isNoDataTableRow()) {
                    FlowPane pane = new FlowPane();
                    pane.setHgap(10);
                    Button actionButton = null;

                    // Icon
                    if(upOrDown.equals("up")) {
                        actionButton= new Button("Add!");
                        Image imageOk = new Image(getClass().getResourceAsStream("32px-Gnome-go-up.svg.png"));
                        actionButton.setGraphic(new ImageView(imageOk));
                    } else {
                        actionButton= new Button("Remove!");
                        Image imageOk = new Image(getClass().getResourceAsStream("32px-Gnome-go-down.svg.png"));
                        actionButton.setGraphic(new ImageView(imageOk));
                    }

                    // Action
                    actionButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            UserFX vo = getRowItem();
                            if (upOrDown.equals("up"))
                                controller.moveUp(vo);
                            else
                                controller.moveDown(vo);
                        }
                    });
                    pane.getChildren().add(actionButton);

                    setGraphic(pane);
                } else {
                    setGraphic(null);
                }
            }

            private boolean isNoDataTableRow() {
                return getIndex() == -1 || getIndex() >= getTableView().getItems().size();
            }

            protected UserFX getRowItem() {
                UserFX item = (UserFX) getTableRow().getItem();

                if(isNoDataTableRow())
                    return null;

                if (item == null) {
                    item = getTableView().getItems().get(getIndex());
                }
                return item;
            }
        };

        return myTableCell;
    }
}
