package com.ken.swing.elements;

import java.awt.*;

public class UnmatchedElementException extends RuntimeException{
    public UnmatchedElementException(Component comp,Component subComp){
        super(subComp.getClass().getSimpleName() + "cannot be attached to" + comp.getClass().getSimpleName());
    }
    public UnmatchedElementException(Class<? extends Component> comp,Class<? extends Component> subComp){
        super(subComp.getSimpleName() + "cannot be attached to" + comp.getSimpleName());
    }
}
