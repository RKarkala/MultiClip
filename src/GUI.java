import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;


public class GUI  {
    public static void main(String args[]){
        //List<Object> allCopies = ListenerKt.getAllContents();
        String array[] = {"Hello this is a long setnence to simulate something long that someone would copy", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Short", "Hi"};
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = MouseInfo.getPointerInfo().getLocation();
        JPanel panel = new JPanel(new BorderLayout());

        List<String> labels = new ArrayList<>(25);
        for (int index = 0; index < array.length; index++) {
            labels.add(array[index]);
        }

        final JList<String> listArea = new JList<String>(labels.toArray(new String[labels.size()]));
        listArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listArea.setFont(new Font("Courier", Font.BOLD, screenSize.width/100));
        JScrollPane listScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScroller.setViewportView(listArea);
        //listArea.setLayoutOrientation(JList.VERTICAL);
        panel.add(listScroller);

        JFrame frame = new JFrame("Your Copies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //ListenerKt.listen();


    }
}
