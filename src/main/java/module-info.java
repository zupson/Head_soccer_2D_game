module hr.algebra.head_soccer_2d_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.dyn4j;
    requires javafx.graphics;
    requires java.rmi;
    requires java.naming;
    requires javafx.base;


    opens hr.algebra.head_soccer_2d_game to javafx.fxml;
    exports hr.algebra.head_soccer_2d_game.client.main;
    opens hr.algebra.head_soccer_2d_game.client.main to javafx.fxml;
    exports hr.algebra.head_soccer_2d_game.client.controller.ui;
    opens hr.algebra.head_soccer_2d_game.client.controller.ui to javafx.fxml;
    exports hr.algebra.head_soccer_2d_game.client.controller.input;
    opens hr.algebra.head_soccer_2d_game.client.controller.input to javafx.fxml;
    exports hr.algebra.head_soccer_2d_game.shared.event;
    opens hr.algebra.head_soccer_2d_game.shared.event to javafx.fxml;

    //eksplicitno definiranjje da bi RMI radio s refpleksijom i znao koje metode ja imam u ChatRemoteService tjekom runtime-a
    exports hr.algebra.head_soccer_2d_game.server.rmi;
    opens hr.algebra.head_soccer_2d_game.server.rmi to javafx.rmi;
}