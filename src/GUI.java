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
    public static void main(String args[]){

        ListenerKt.listen();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = MouseInfo.getPointerInfo().getLocation();
        JFrame frame = new JFrame("MultiClip");
        frame.setSize((int)(screenSize.width*.4), (int)(screenSize.height*.7));

        JPanel txtPanel = new JPanel(new BorderLayout());
        JPanel imgPanel = new JPanel(new BorderLayout());

        imgPanel.setPreferredSize(new Dimension((int)(screenSize.width*.15), (int)(screenSize.height*.7)));
        txtPanel.setPreferredSize(new Dimension((int)(screenSize.width*.25), (int)(screenSize.height*.7)));




        frame.isAlwaysOnTop();
        frame.setLocation(location.x, location.y - 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(imgPanel, BorderLayout.WEST);
        frame.add(txtPanel, BorderLayout.EAST);
        frame.setResizable(false);

        int txtlastLength = 0;
        int imglastLength = 0;

        DefaultListModel<Object> txtmodel = new DefaultListModel<>();
        JList<Object> txtlistArea = new JList<>(txtmodel);
        txtlistArea.setFont(new Font("SansSerif", Font.BOLD, screenSize.width / 100));
        txtlistArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        txtlistArea.setVisibleRowCount(0);
        txtlistArea.setCellRenderer(new DefaultListCellRenderer(){
            public int getHorizontalAlignment() {
                return LEFT;
            }
        });

        DefaultListModel<Object> imgmodel = new DefaultListModel<>();
        JList<Object> imglistArea = new JList<>(imgmodel);
        imglistArea.setFont(new Font("SansSerif", Font.BOLD, screenSize.width / 100));
        imglistArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        imglistArea.setVisibleRowCount(0);
        imglistArea.setCellRenderer(new DefaultListCellRenderer(){
            public int getHorizontalAlignment() {
                return LEFT;
            }
        });

        JScrollPane txtlistScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane imglistScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        txtlistArea.setLayoutOrientation(JList.VERTICAL);
        txtlistScroller.setViewportView(txtlistArea);
        txtPanel.add(txtlistScroller);

        imglistArea.setLayoutOrientation(JList.VERTICAL);
        imglistScroller.setViewportView(imglistArea);

        imgPanel.add(imglistScroller);

        frame.setVisible(true);

        MouseListener txtmouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index > 0) {
                        String p = ListenerKt.getTxtContent().get(index).toString();
                        System.out.println(p);
                        System.out.println("In Exception");
                        StringSelection s = new StringSelection(theList.getModel().getElementAt(index).toString());
                        ListenerKt.getClipboard().setContents(s, null);
                    }
                }
            }
        };
        txtlistArea.addMouseListener(txtmouseListener);

        MouseListener imgmouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    String p = ListenerKt.getImgContent().get(index).toString();
                    if (index > 0) {
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
                    }catch(Exception e){
                        }
                    }
                }
            }
        };
        imglistArea.addMouseListener(imgmouseListener);
        while(true) {
            List<Object> txtData = ListenerKt.getTxtContent();
            List<Object> imgData = ListenerKt.getImgContent();
            try{
                Thread.sleep(250);
            }catch (Exception e){

            }
            if(txtlastLength!=txtData.size()){
                txtlastLength = txtData.size();
                String temp = txtData.get(txtData.size()-1).toString();
                System.out.println(temp);
                txtmodel.addElement(temp);
            }
            if(imglastLength!=imgData.size()){
                imglastLength = imgData.size();
                String temp = imgData.get(imgData.size()-1).toString();
                BASE64Decoder decoder = new BASE64Decoder();
                BufferedImage image = null;
                byte[]  imageByte;
                try {
                    imageByte = decoder.decodeBuffer(temp);
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                    image = ImageIO.read(bis);
                    Image newImage = image.getScaledInstance((int) (screenSize.width * .15), (int) (screenSize.height * .15), Image.SCALE_REPLICATE);
                    imgmodel.addElement(new ImageIcon(newImage));
                }catch(Exception e){
                    String t = imgData.get(imgData.size()-1).toString();
                    System.out.println(temp);
                    imgmodel.addElement(t);
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
