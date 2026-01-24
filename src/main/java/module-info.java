module hr.algebra.head_soccer_2d_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.dyn4j;
    requires javafx.graphics;


    opens hr.algebra.head_soccer_2d_game to javafx.fxml;
    exports hr.algebra.head_soccer_2d_game.controller;
    opens hr.algebra.head_soccer_2d_game.controller to javafx.fxml;
    exports hr.algebra.head_soccer_2d_game.main;
    opens hr.algebra.head_soccer_2d_game.main to javafx.fxml;
}