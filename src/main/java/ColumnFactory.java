import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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

                if (!isDataTableRow()) {
                    FlowPane pane = new FlowPane();
                    pane.setHgap(10);
                    Button action = new Button("move!");
                    action.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            UserFX vo = getRowItem();
                            if (upOrDown.equals("up"))
                                controller.moveUp(vo);
                            else
                                controller.moveDown(vo);
                        }
                    });
                    pane.getChildren().add(action);

                    setGraphic(pane);
                }
            }

            private boolean isDataTableRow() {
                // TODO figure this out so the buttons are not shown at every row
//                    return getRowItem() != null;
                return false;
            }

            protected UserFX getRowItem() {
                UserFX item = (UserFX) getTableRow().getItem();
                if (item == null) {
                    item = getTableView().getItems().get(getIndex());
                }
                return item;
            }
        };

        return myTableCell;
    }
}
