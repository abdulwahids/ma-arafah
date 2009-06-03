package tajmi.functional.instances.cdk;

import tajmi.functional.interfaces.Fun;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.ReaderFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * <code> MoleculeReader :: String filename -> IO IMolecule </code>
 * @author badi
 */
public class MoleculeReader implements Fun {

    String filename;

    public Fun copy() {
        return new MoleculeReader().curry(filename);
    }

    public Fun curry(Object arg) {

        if (filename == null)
            filename = (String) arg;

        return this;
    }

    public IMolecule call() throws FileNotFoundException, IOException, CDKException  {
        Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        ISimpleChemObjectReader reader = new ReaderFactory().createReader(r);

        return (IMolecule) reader.read(new Molecule());
    }
}