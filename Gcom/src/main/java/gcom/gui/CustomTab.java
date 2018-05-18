package gcom.gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CustomTab extends Tab {
    private TextFlow tf;
    private ScrollPane sp;
    private String label;

    public CustomTab() {
        super();
    }

    public void setContentTextFlow(TextFlow tf) {
        this.sp = new ScrollPane();
        this.tf = tf;
        sp.setContent(tf);
        this.setContent(sp);
    }

    public void setText(String text, String color) {
        Text t = new Text(text);
        t.setFill(Paint.valueOf(color));
        tf.getChildren().add(t);
    }

    public void setLabel(String label) {
        this.label=label;
    }
    public String getLabel() {
        return label;
    }

    public TextFlow getTf() {
        return tf;
    }

    public ScrollPane getSp() {
        return sp;
    }
}
