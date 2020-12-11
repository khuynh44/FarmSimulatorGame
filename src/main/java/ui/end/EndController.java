package main.java.ui.end;

import javafx.event.ActionEvent;
import main.java.ui.Controller;
import main.java.util.View;

public class EndController extends Controller<EndModel> {
    public void switchToStartScreen(ActionEvent event) {
        View.MAIN.setViewOn(event);
    }
}
