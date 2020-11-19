package UnoCommandPattern;

import UnoProvaClientVecchio.fakeServer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GestoreComandi {

    Map<String, Comando> elencoComandi;
    fakeServer server;

    public GestoreComandi(fakeServer serv)
    {
       elencoComandi  = new HashMap<String, Comando>();
       this.server = serv;
    }



    public void addComando(String nomeComando , Comando cmd)
    {
        elencoComandi.put(nomeComando, cmd);
    }

    public void eseguiComando(String nomeCom, String json) throws SQLException, ClassNotFoundException {
        elencoComandi.get(nomeCom).execute(json);
    }


}
