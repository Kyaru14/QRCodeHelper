import com.ken.cliptool.ClipBoardToolKit;
import com.ken.qrcode.QRDecoder;
import com.ken.qrcode.QREncoder;
import com.ken.swing.elements.DropDragSupportTextArea;
import com.ken.swing.elements.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class GUI {
    public static final Window mainWindow;
    private static final Clipboard sysClipboard = ClipBoardToolKit.getSysClip();
    private static final SystemClipboardMonitor systemClipboardMonitor = new SystemClipboardMonitor();

    static {
        System.setErr(System.out);
        sysClipboard.setContents(sysClipboard.getContents(null), systemClipboardMonitor);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        mainWindow = new Window("二维码工具", 300, 350);
        mainWindow.setLocation(400, 200);
        mainWindow.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainWindow.setMainWindow();

        JPanel startMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 70));
        JPanel encoderPanel = new JPanel(new GridLayout(5, 1, 0, 0));
        JPanel decoderPanel = new JPanel(new GridLayout(5, 1, 0, 0));
        startMenu.setVisible(true);
        encoderPanel.setVisible(false);
        decoderPanel.setVisible(false);

        JCheckBox clipFunc = new JCheckBox("开启截图捕获");
        clipFunc.addActionListener(e -> {
            if (clipFunc.isSelected()) {
                systemClipboardMonitor.setEnabled(true);
                mainWindow.vanish();
                if (SystemTray.isSupported()) {// 判断当前平台是否支持系统托盘
                    SystemTray st = SystemTray.getSystemTray();
                    BufferedImage image = QREncoder.createTransparentBufferedImage("qrcode helper",10,10);//定义托盘图标的图片
                    TrayIcon ti = new TrayIcon(image);
                    ti.setToolTip("QRCode");
                    ti.setImageAutoSize(true);
                    PopupMenu popupMenu1 = new PopupMenu();
                    MenuItem disable = new MenuItem("disable capture");
                    disable.addActionListener(dise -> {
                        mainWindow.show();
                        clipFunc.setSelected(false);
                        systemClipboardMonitor.setEnabled(false);
                        st.remove(ti);
                    });
                    MenuItem exit = new MenuItem("exit");
                    exit.addActionListener(exe -> System.exit(0));
                    popupMenu1.add(disable);
                    popupMenu1.add(exit);
                    ti.setPopupMenu(popupMenu1);
                    try {
                        st.add(ti);
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                systemClipboardMonitor.setEnabled(false);
            }
        });


        mainWindow.addComp(startMenu);
        mainWindow.addComp(encoderPanel);
        mainWindow.addComp(decoderPanel);


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 50));
        JButton encodeButton = new JButton();
        encodeButton.addActionListener(e -> {
            startMenu.setVisible(false);
            encoderPanel.setVisible(true);
        });
        encodeButton.setText("生成二维码");
        JButton decodeButton = new JButton();
        decodeButton.addActionListener(e -> {
            startMenu.setVisible(false);
            decoderPanel.setVisible(true);
        });
        decodeButton.setText("读取二维码");
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);
        buttonPanel.add(clipFunc);
        startMenu.add(buttonPanel);

        JFileChooser fileChooser = new JFileChooser();

        //encoderPanel
        JLabel l_content = new JLabel("要生成二维码的内容：");
        JTextArea area_content = new JTextArea();
        area_content.setColumns(15);
        area_content.setRows(3);
        area_content.setLineWrap(true);
        JScrollPane jScrollPane_encoder = new JScrollPane();
        jScrollPane_encoder.setViewportView(area_content);

        JCheckBox c_back = new JCheckBox("透明背景");
        JButton b_goback = new JButton("返回");
        b_goback.addActionListener(e -> {
            encoderPanel.setVisible(false);
            startMenu.setVisible(true);
        });
        JButton b_create = new JButton("生成");

        JPanel p_button = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));

        p_button.add(b_goback);
        p_button.add(b_create);
        encoderPanel.add(l_content);
        encoderPanel.add(jScrollPane_encoder);
        encoderPanel.add(c_back);
        encoderPanel.add(p_button);

        b_create.addActionListener(e -> {
            String content = area_content.getText();
            if (content.isEmpty()) {
                msgbox("内容为空");
            }
            JPanel p_path = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel p_name = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel p_save = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel p_size = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JLabel l_size = new JLabel("尺寸：");
            JTextField f_size = new JTextField(5);
            f_size.setText("200");
            p_size.add(l_size);
            p_size.add(f_size);
            encoderPanel.add(p_size);

            JLabel l_path = new JLabel("输出目录：");
            JLabel l_name = new JLabel("文件名：");
            JTextField f_path = new JTextField(20);
            JTextField f_name = new JTextField(20);
            f_path.setText(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
            JButton b_path = new JButton("浏览");
            b_path.addActionListener(pe -> {
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fileChooser.showSaveDialog(mainWindow.getJFrame()) == JFileChooser.APPROVE_OPTION) {
                    f_path.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            });
            JButton b_save = new JButton("保存");
            b_save.addActionListener(be -> {
                int size = Integer.parseInt(f_size.getText());
                String path = f_path.getText();
                String filename = f_name.getText();
                if (!path.endsWith("\\")) {
                    path = path + "\\";
                }
                if (filename.isEmpty()) {
                    filename = System.currentTimeMillis() + ".png";
                }
                if (!filename.endsWith(".png")) {
                    filename = filename + ".png";
                }
                if (c_back.isSelected()) {
                    QREncoder.writeTransparentQRCode(content, size, size, path + filename, null);
                } else {
                    QREncoder.writeQRCode(content, size, size, "png", new File(path + filename), null);
                }
                if (new File(path + filename).exists()) {
                    msgbox("保存成功");
                } else {
                    msgbox("保存失败，请尝试更换路径");
                }
            });

            p_path.add(l_path);
            p_path.add(f_path);
            p_path.add(b_path);
            p_name.add(l_name);
            p_name.add(f_name);
            p_save.add(b_save);

            BufferedImage image;
            if (c_back.isSelected()) {
                image = QREncoder.createTransparentBufferedImage(content, 200, 200);
            } else {
                image = QREncoder.createBufferedImage(content, 200, 200);
            }
            JFrame res = new JFrame("结果");

            res.setLayout(new FlowLayout(FlowLayout.CENTER));

            res.setSize(340, 380);
            res.setLocation(Math.abs((mainWindow.getWidth() - res.getWidth()) / 2) + mainWindow.getLocation().x, Math.abs((mainWindow.getHeight() - res.getHeight()) / 2) + mainWindow.getLocation().y);
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon(image));
            res.add(label);

            res.add(p_path);
            res.add(p_name);
            res.add(p_size);
            res.add(b_save);

            res.setVisible(true);
        });

        //decoderPanel
        JPanel dropFilePanel = new JPanel(new GridLayout(2, 1, 0, 0));
        JLabel dropFileLabel = new JLabel("拖曳文件到此处");
        DropDragSupportTextArea dropDragSupportTextArea = new DropDragSupportTextArea();
        dropDragSupportTextArea.setColumns(25);
        dropDragSupportTextArea.setRows(2);
        dropDragSupportTextArea.setEditable(false);
        dropFilePanel.add(dropFileLabel);
        dropFilePanel.add(dropDragSupportTextArea);

        JLabel resultLabel = new JLabel("解码结果：");
        JTextArea resTextArea = new JTextArea();
        resTextArea.setColumns(25);
        resTextArea.setRows(3);
        resTextArea.setLineWrap(true);
        JScrollPane jScrollPane_decoder = new JScrollPane();
        jScrollPane_decoder.setViewportView(resTextArea);
        JPanel resultPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        resultPanel.add(resultLabel);
        resultPanel.add(jScrollPane_decoder);

        JButton gobackButton = new JButton("返回");
        gobackButton.addActionListener(e -> {
            decoderPanel.setVisible(false);
            startMenu.setVisible(true);
        });
        JButton createButton = new JButton("解码");
        createButton.addActionListener(e ->
                resTextArea.setText(QRDecoder.decode(dropDragSupportTextArea.files.get(0).getAbsolutePath())));
        JButton copyButton = new JButton("复制");
        copyButton.addActionListener(e -> {
            if (!resTextArea.getText().isEmpty()) {
                sysClipboard.setContents(new StringSelection(resTextArea.getText()), null);
            }
        });

        JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        button_panel.add(gobackButton);
        button_panel.add(createButton);
        button_panel.add(copyButton);

        decoderPanel.add(dropFilePanel);
        decoderPanel.add(resultPanel);
        decoderPanel.add(button_panel);
    }


    public static void show() {
        mainWindow.show();
    }

    public static void msgbox(String msg) {
        JFrame msgWindow = new JFrame("提示");
        msgWindow.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 10) {
                    msgWindow.setVisible(false);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        msgWindow.setLayout(new BorderLayout());
        msgWindow.setSize(400, 120);
        msgWindow.setLocation(Math.abs((mainWindow.getWidth() - msgWindow.getWidth()) / 2) + mainWindow.getLocation().x, Math.abs((mainWindow.getHeight() - msgWindow.getHeight()) / 2) + mainWindow.getLocation().y);
        JLabel msgLabel = new JLabel(msg + "（按Enter键可关闭本窗口）");
        msgWindow.add(msgLabel, BorderLayout.CENTER);
        msgWindow.setVisible(true);
    }
}

class SystemClipboardMonitor implements ClipboardOwner {

    private boolean isEnabled = false;

    public SystemClipboardMonitor() {

    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isEnabled && clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
            BufferedImage bufferedImage = ClipBoardToolKit.getImage(clipboard.getContents(null));
            String content = QRDecoder.decode(bufferedImage);
            if (content != null) {
                Window window = new Window("识别结果", 300, 150);
                JTextArea result = new JTextArea(content);
                result.setLineWrap(true);
                result.setColumns(30);
                result.setRows(3);
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setViewportView(result);
                JButton copyButton = new JButton("复制到剪贴板");
                copyButton.addActionListener(e -> {
                    clipboard.setContents(new StringSelection(content), this);
                    window.vanish();
                });
                window.setLayout(new FlowLayout(FlowLayout.CENTER));
                window.getJFrame().add(scrollPane);
                window.getJFrame().add(copyButton);
                window.setLocationCenterTo(GUI.mainWindow.getJFrame());
                window.getJFrame().setAlwaysOnTop(true);
                window.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (e.getKeyChar() == 10) {
                            window.vanish();
                        }
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                window.show();
            }
        }
        clipboard.setContents(clipboard.getContents(null), this);
    }

}