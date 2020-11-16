package UnoProvaServerVecchioo;

import java.util.ArrayList;

public class createCommand
{
    String comando;
    Object obj;





    public createCommand(String comando, Object obj) {
        this.comando = comando;
        this.obj = obj;



    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
