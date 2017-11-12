import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kotlin.jvm.Throws;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;


public class GUI  {
    public static void main(String args[]){

        ListenerKt.listen();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = MouseInfo.getPointerInfo().getLocation();
        JFrame frame = new JFrame("Your Copies");
        JPanel panel = new JPanel(new BorderLayout());
        frame.isAlwaysOnTop();
        frame.setLocation(location.x, location.y - 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setResizable(false);
        frame.setSize((int)(screenSize.width*.2), (int)(screenSize.height*.4));
        frame.setLocationRelativeTo(null);
        int lastLength = 0;
        final List<Object> tempCopies = ListenerKt.getAllContents();
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> listArea = new JList<>(model);
        listArea.setFont(new Font("Courier", Font.BOLD, screenSize.width / 100));
        JScrollPane listScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScroller.setViewportView(listArea);
        panel.add(listScroller);
        frame.setVisible(true);
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        String p = theList.getModel().getElementAt(index).toString();

                        if(p.startsWith("BufferedImage")){
                            BufferedImage image = (BufferedImage) theList.getModel().getElementAt(index);
                            TransferableImage i = new TransferableImage(image);
                            ListenerKt.getClipboard().setContents(i, null);
                        }else{
                            StringSelection s = new StringSelection(theList.getModel().getElementAt(index).toString());
                            ListenerKt.getClipboard().setContents(s, null);
                        }

                    }
                }
            }
        };
        listArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listArea.setLayoutOrientation(JList.VERTICAL_WRAP);
        listArea.setVisibleRowCount(-1);
        listArea.addMouseListener(mouseListener);
        while(true) {

            List<Object> allCopies = ListenerKt.getAllContents();
            try{
                Thread.sleep(200);
            }catch (Exception e){

            }
            if(allCopies.size()!=lastLength) {
                lastLength = allCopies.size();
                model.addElement(allCopies.get(allCopies.size()-1).toString());

            }
        }
    }
    private static class TransferableImage implements Transferable {

        Image i;

        public TransferableImage( Image i ) {
            this.i = i;
        }

        public Object getTransferData( DataFlavor flavor )
                throws UnsupportedFlavorException, IOException {
            if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
                return i;
            }
            else {
                throw new UnsupportedFlavorException( flavor );
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported( DataFlavor flavor ) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for ( int i = 0; i < flavors.length; i++ ) {
                if ( flavor.equals( flavors[ i ] ) ) {
                    return true;
                }
            }

            return false;
        }
    }
}
