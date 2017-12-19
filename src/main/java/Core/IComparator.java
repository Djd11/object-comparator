package Core;

import java.io.File;
import java.io.IOException;

public interface IComparator<X,Y> {

    boolean compare(X x,Y y);

    Wrapper<X,Y> getData(File f1,File f2) throws IOException;

}