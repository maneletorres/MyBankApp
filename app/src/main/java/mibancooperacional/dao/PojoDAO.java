package mibancooperacional.dao;

import java.util.ArrayList;

public interface PojoDAO {
    long add(Object obj);

    int update(Object obj);

    void delete(Object obj);

    Object search(Object obj);

    ArrayList getAll();
}