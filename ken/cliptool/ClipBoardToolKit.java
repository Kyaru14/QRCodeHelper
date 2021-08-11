package com.ken.cliptool;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClipBoardToolKit {
    public static Clipboard getSysClip(){
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }
    public static BufferedImage getImageFromClipboard(){
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = sysc.getContents(null);
        if (cc == null)
            return null;
        else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            Image image = null;
            try {
                image = (Image) cc.getTransferData(DataFlavor.imageFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
            return new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
        }
        return null;
    }
    public static BufferedImage getImage(Transferable imageFlavor){
        if(imageFlavor.isDataFlavorSupported(DataFlavor.imageFlavor)){
            BufferedImage image = null;
            try {
                image = (BufferedImage) imageFlavor.getTransferData(DataFlavor.imageFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
//            return new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            return image;
        }
        return null;
    }
}