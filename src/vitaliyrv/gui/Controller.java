package vitaliyrv.gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import vitaliyrv.tree.Pattern;
import vitaliyrv.tree.Tree;
import vitaliyrv.tree.TreePainter;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Canvas mainCanvas;
    public ListView<String> patternListView;
    public Button stepButton;
    public Button startButton;
    public Button stopButton;
    public Button sizeIncButton;
    public Button sizeDecButton;
    public Button clearButton;
    public TextField delayTextField;

    private Tree tree;
    private int PW = 12;
    private TreePainter treePainter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tree = new Tree();
        //vitaliyrv.tree = new HashTree();
        tree.loadPattern(Pattern.Names.GALAXY);
        treePainter = new TreePainter(mainCanvas);
        treePainter.draw(tree.getHead());

        patternListView.getItems().add(0, "- Случайно 10х10");
        patternListView.getItems().add(1, "- Случайно 20х20");
        patternListView.getItems().add(2, "- Случайно 30х30");
        patternListView.getItems().add(3, "- Случайно 40х40");
        patternListView.getItems().add(4, "- Случайно 50х50");
        patternListView.getItems().add(5, "- Квадрат 10х10");
        patternListView.getItems().add(6, "- Квадрат 20х20");
        patternListView.getItems().add(7, "- Квадрат 30х30");
        patternListView.getItems().add(8, "- Квадрат 40х40");
        patternListView.getItems().add(9, "- Квадрат 50х50");
        patternListView.getItems().add(10, "Галактика Кока");
        patternListView.getItems().add(11, "Планерное ружье Госпера");
    }

    public void stepButtonMouseClick(MouseEvent mouseEvent) {
        lifeProcess();
    }

    public void startButtonMouseClick(MouseEvent mouseEvent) {
        animationTimer.start();
    }

    public void stopButtonMouseClick(MouseEvent mouseEvent) {
        animationTimer.stop();
    }

    public void sizeIncButtonMouseClick(MouseEvent mouseEvent) {
        treePainter.setPointWidth(++PW);
        treePainter.draw(tree.getHead());
    }

    public void sizeDecButtonMouseClick(MouseEvent mouseEvent) {
        treePainter.setPointWidth(--PW);
        treePainter.draw(tree.getHead());
    }

    public void clearButtonMouseClick(MouseEvent mouseEvent) {
        tree.clear();
        treePainter.draw(tree.getHead());
    }

    public void mainCanvasMouseClick(MouseEvent mouseEvent) {
        int mouseX = (int)mouseEvent.getX();
        int mouseY = (int)mouseEvent.getY();
        int m_x = (mouseX / PW) - (int)mainCanvas.getWidth() / (PW * 2) - 1;
        int m_y = (mouseY / PW) - (int)mainCanvas.getHeight() / (PW * 2) - 1;

        tree.setBit((int) m_x, (int) m_y+1);
        treePainter.draw(tree.getHead());
    }

    public void patternListViewMouseClick(MouseEvent mouseEvent) {
        int index = patternListView.getSelectionModel().getSelectedIndex();
        if (index == 0) {
            tree.clear();
            tree.randomFill(10);
        }
        if (index == 1) {
            tree.clear();
            tree.randomFill(20);
        }
        if (index == 2) {
            tree.clear();
            tree.randomFill(30);
        }
        if (index == 3) {
            tree.clear();
            tree.randomFill(40);
        }
        if (index == 4) {
            tree.clear();
            tree.randomFill(50);
        }
        if (index == 5) {
            tree.clear();
            tree.squareFill(10);
        }
        if (index == 6) {
            tree.clear();
            tree.squareFill(20);
        }
        if (index == 7) {
            tree.clear();
            tree.squareFill(30);
        }
        if (index == 8) {
            tree.clear();
            tree.squareFill(40);
        }
        if (index == 9) {
            tree.clear();
            tree.squareFill(50);
        }
        if (index == 10) {
            tree.clear();
            tree.loadPattern(Pattern.Names.GALAXY);
        }
        if (index == 11) {
            tree.clear();
            tree.loadPattern(Pattern.Names.PLANER_GUN);
        }
        treePainter.draw(tree.getHead());
    }

    private AnimationTimer animationTimer = new AnimationTimer() {
        private long lastUpdate = 0;
        private int delayMS, delayNS;
        private String delayStr;

        @Override
        public void handle(long now) {
            delayStr = delayTextField.getText();
            if (delayStr.isEmpty()) {
                delayMS = 200;
            } else {
                delayMS = Integer.valueOf(delayStr);
            }
            delayNS = delayMS * 1000000;
            if (now - lastUpdate >= delayNS) {
                lifeProcess();
                lastUpdate = now;
            }
        }
    };

    private void lifeProcess() {
        tree.nextGen();
        treePainter.draw(tree.getHead());
    }
}
