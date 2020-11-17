package UnoProvaClientVecchio;

public interface Observed {
    void Attach(Observer ob);
    void Detach(Observer ob);
    void Notify();
}
