module sydormyk {
    requires javafx.controls;
    requires javafx.graphics;

    requires org.json;

    requires java.desktop;
    requires java.logging;

    opens cz.cvut.fel.pjv to javafx.graphics;
    opens cz.cvut.fel.pjv.game.view to javafx.graphics;
    opens cz.cvut.fel.pjv.game.controller to javafx.graphics;

    exports cz.cvut.fel.pjv;
}