module hr.algebra.head_socker_2d_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.dyn4j;
    requires javafx.graphics;

    opens hr.algebra.head_socker_2d_game to javafx.fxml;
    exports hr.algebra.head_socker_2d_game.view;
    opens hr.algebra.head_socker_2d_game.view to javafx.fxml;
}