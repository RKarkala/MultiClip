import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kotlin.jvm.Throws;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class GUI  {
    static boolean dontCreate = false;
    public static void main(String args[]){

        ListenerKt.listen();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = MouseInfo.getPointerInfo().getLocation();
        JFrame frame = new JFrame("Your Copies");
        frame.setSize((int)(screenSize.width*.4), (int)(screenSize.height*.7));
        JPanel outer = new JPanel();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension((int)(screenSize.width*.2), (int)(screenSize.height*.5)));
        frame.isAlwaysOnTop();
        frame.setLocation(location.x, location.y - 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel, BorderLayout.WEST);
        frame.setResizable(false);

        int lastLength = 0;
        DefaultListModel<Object> model = new DefaultListModel<>();
        JList<Object> listArea = new JList<>(model);
        listArea.setFont(new Font("Courier", Font.BOLD, screenSize.width / 100));
        listArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listArea.setVisibleRowCount(0);
        listArea.setCellRenderer(new DefaultListCellRenderer(){
            public int getHorizontalAlignment() {
                return CENTER;
            }
        });
        JScrollPane listScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listArea.setLayoutOrientation(JList.VERTICAL);
        listScroller.setViewportView(listArea);
        panel.add(listScroller);
        frame.setVisible(true);
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        String p = ListenerKt.getAllContents().get(index).toString();
                        System.out.println(p);
                            BASE64Decoder decoder = new BASE64Decoder();
                            BufferedImage image = null;
                            byte[]  imageByte;
                            try{
                                imageByte = decoder.decodeBuffer(p);
                                ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                                image = ImageIO.read(bis);
                                if(image==null){
                                    throw new Exception("Problem");
                                }
                                TransferableImage i = new TransferableImage(image);
                                ListenerKt.getClipboard().setContents(i, null);
                            }catch (Exception e){
                                System.out.println("In Exception");
                                StringSelection s = new StringSelection(theList.getModel().getElementAt(index).toString());
                                ListenerKt.getClipboard().setContents(s, null);
                            }


                    }
                }
            }
        };
        listArea.addMouseListener(mouseListener);
        while(true) {

            List<Object> allCopies = ListenerKt.getAllContents();
            try{
                Thread.sleep(250);
            }catch (Exception e){

            }
            if(allCopies.size()!=lastLength) {
                lastLength = allCopies.size();
                String temp = allCopies.get(allCopies.size()-1).toString();
                if(temp.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")){
                    BASE64Decoder decoder = new BASE64Decoder();
                    BufferedImage image = null;
                    byte[]  imageByte;
                    try{
                        imageByte = decoder.decodeBuffer(temp);
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                        image = ImageIO.read(bis);
                        Image newImage = image.getScaledInstance((int)(screenSize.width*.15),(int)(screenSize.height*.15),Image.SCALE_REPLICATE);
                        model.addElement(new ImageIcon(newImage));
                    }catch (Exception e){

                    }
                }else {
                    model.addElement(temp);
                }

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
