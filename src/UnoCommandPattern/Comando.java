package UnoCommandPattern;

import java.sql.SQLException;

public interface Comando {
    void execute(String json) throws SQLException, ClassNotFoundException;
}
