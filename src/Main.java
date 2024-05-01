
public class Main {
    public static void main(String[] args) {
        // Launch
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui.StringInputGUI guiComponent = new gui.StringInputGUI();
                guiComponent.setVisible(true);
            }
        });
    }
}
