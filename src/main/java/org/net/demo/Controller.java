package org.net.demo;

public abstract class Controller {

    protected MainController mainController;

    abstract public void OnShowing();

    abstract public void Refresh();

    abstract public void OnAttached();

    abstract public void OnLogin();

    abstract public void OnLogout();

    public void getMainController(MainController mainController)
    {
      this.mainController= mainController;
    }

    


    
}
