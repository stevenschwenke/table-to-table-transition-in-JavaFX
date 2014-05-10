import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

public class ZugriffsrechteFuerProjektSetzenFactorys {

    public static class TableViewActionColumnFactory implements
            Callback<TableColumn<UserFXVO, String>, TableCell<UserFXVO, String>> {

        private TableToTableTransitionController controller;

        private String upOrDown;

        public TableViewActionColumnFactory(TableToTableTransitionController controller, String upOrDown) {
            super();
            this.controller = controller;
            this.upOrDown = upOrDown;
        }

        @Override
        public TableCell<UserFXVO, String> call(TableColumn<UserFXVO, String> arg0) {
            TableCell<UserFXVO, String> myTableCell = new TableCell<UserFXVO, String>(
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
                                UserFXVO vo = getRowItem();
                                if(upOrDown.equals("up"))
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
//                    return getRowItem() != null;
                    return false;
                }

                protected UserFXVO getRowItem() {
                    UserFXVO item = (UserFXVO) getTableRow().getItem();
                    if(item == null) {
                        item = getTableView().getItems().get(getIndex());
                    }
                    return item;
                }
            };


            return myTableCell;
        }
    }


}
