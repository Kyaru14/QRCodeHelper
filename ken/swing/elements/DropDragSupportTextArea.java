package com.ken.swing.elements;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
public class DropDragSupportTextArea extends JTextArea implements DropTargetListener{
    private DropTarget dropTarget;

    public List<File> files;

    public DropDragSupportTextArea() {
        this.setLineWrap(true);
        files = new ArrayList<>();
        dropTarget = new DropTarget(this,DnDConstants.ACTION_COPY_OR_MOVE, this, true);
    }

    /**
     * 拖入文件或字符串,这里只说明能拖拽，并未打开文件并显示到文本区域中
     */
    public void dragEnter(DropTargetDragEvent dtde) {
        DataFlavor[] dataFlavors = dtde.getCurrentDataFlavors();
        if(dataFlavors[0].match(DataFlavor.javaFileListFlavor)){
            try {
                Transferable tr = dtde.getTransferable();
                Object obj = tr.getTransferData(DataFlavor.javaFileListFlavor);
                if(files.isEmpty()){
                    files.add(((List<File>) obj).get(0));
                }else {
                    files.set(0,((List<File>) obj).get(0));
                }
                setText(files.get(0).getAbsolutePath());
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void dragOver(DropTargetDragEvent dtde) {

    }

    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    public void dragExit(DropTargetEvent dte) {

    }

    public void drop(DropTargetDropEvent dtde) {

    }

}
