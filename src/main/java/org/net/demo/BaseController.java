package org.net.demo;

public abstract class BaseController {

    protected MainController mainController;

    abstract public void OnShowing();

    abstract public void Refresh();

    abstract public void OnAttached();

    abstract public void OnLogin();

    abstract public void OnLogout();

    abstract public void OnExit();

    public void getMainController(MainController mainController)
    {
      this.mainController= mainController;
    }

    


    
}
