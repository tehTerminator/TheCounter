package in.maharaja.controllers;

import in.maharaja.gui.AbstractFrame;

/**
 * Created by Prateek on 01-11-2015.
 */
public abstract class Controller<T extends AbstractFrame> {
    private T app;

    Controller(T app){
        this.app = app;
    }

    public T getApp(){
        return app;
    }

    abstract public void registerEvents();


}
