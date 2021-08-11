package com.ken.swing.elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class Window {
    private final JFrame frame;

    private int compNum;

    /**
     * 窗口类
     * @param title 窗口标题
     * @param size 窗口大小
     */
    public Window(String title, Dimension size){
        frame = new JFrame(title);
        frame.setSize(size);
        compNum = 0;
    }

    public Window(String title, int width,int height){
        frame = new JFrame(title);
        frame.setSize(width, height);
    }

    public void setBackground(Color color){
        frame.setBackground(color);
    }

    public void setLocation(int x,int y){
            frame.setLocation(new Point(x,y));
    }

    public void setLocationCenterTo(Component component){
        frame.setLocation(component.getLocation().x + Math.abs(component.getWidth() - frame.getWidth()),component.getLocation().y + Math.abs(component.getHeight() - frame.getHeight()));
    }

    public void setLayout(LayoutManager layout){
        frame.setLayout(layout);
    }

    public void show(){
        frame.setVisible(true);
    }
    public void vanish(){
        frame.setVisible(false);
    }

    public void setMainWindow(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addComp(Component comp){
        if(comp instanceof JPanel) {
            frame.add(comp);
            compNum++;
            if(frame.getLayout() instanceof GridLayout){
                frame.setLayout(new GridLayout(compNum,1,0,0));
            }
        }else {
            throw new UnmatchedElementException(frame.getClass(),comp.getClass());
        }
    }

    public void addComp(Component... comp){
        if(comp instanceof JPanel[]) {
            for(Component c:comp){
                frame.add(c);
                compNum++;
                if (frame.getLayout() instanceof GridLayout) {
                    frame.setLayout(new GridLayout(compNum, 1, 0, 0));
                }
            }
        }else {
            throw new UnmatchedElementException(frame.getClass(),comp[0].getClass());
        }
    }

    public void addKeyListener(KeyListener keyListener){
        frame.addKeyListener(keyListener);
    }

    public int getWidth(){
        return frame.getWidth();
    }

    public int getHeight(){
        return frame.getHeight();
    }

    public Point getLocation(){
        return frame.getLocation();
    }

    public JFrame getJFrame() {
        return frame;
    }
}