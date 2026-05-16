import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class App {
    public static void main(String[] args) {
        new Setup();   
    }
}

class FileSystem {

    public ArrayList<String> FileName = new ArrayList<>();
    public ArrayList<String> PathFileLocation = new ArrayList<>();

    protected final String home = System.getProperty("user.home");
    protected final String pathdefault = home + "\\Downloads";
    protected final String pathminecraft = home + "\\AppData\\Roaming\\.minecraft\\mods";
    protected final String PathFolder = home + "\\Downloads\\.path";
    protected final String PathSourceTXT = PathFolder + "\\source.txt";
    protected final String PathTargetTXT = PathFolder + "\\target.txt";

    public void createFile(String FileName){
        try {
            new File(FileName);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void WriteFile(String FileName, String Text) {
        try {
            FileWriter writefile = new FileWriter(FileName);
            writefile.write(Text);
            writefile.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Path readFile(String FileName){
        return Path.of(FileName);
    }

}

class Setup extends FileSystem {
    public Setup(){
        this.checkFolder();

        try {
            Files.list(Paths.get(Files.readString(readFile(this.PathSourceTXT))))
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(files -> {
                    this.FileName.add(files.getFileName().toString()); // pathfilelist
                    this.PathFileLocation.add(files.toString());
                });

            String[] option = FileName.toArray(new String[0]);

            JComboBox<String> comboBox = new JComboBox<>(option);

            int result = JOptionPane.showConfirmDialog(null, comboBox, "Available Mods", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);


            if (result == JOptionPane.OK_OPTION){
                Path directory = Paths.get(Files.readString(readFile(this.PathTargetTXT)));
                Files.createDirectories(directory);

                Path target = directory.resolve(FileName.get(comboBox.getSelectedIndex()));
                
                Files.copy(readFile(PathFileLocation.get(comboBox.getSelectedIndex())), target);

                JOptionPane.showOptionDialog(null, "The Mods just imported to Minecraft", "Successfully", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            } else {
                JOptionPane.showOptionDialog(null, "Import Cancelled", "Cancelled", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
             
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void checkFolder(){
        try {
            Path folder = Path.of(this.PathFolder);

            if (Files.exists(folder)) return;
            else {
                JOptionPane.showMessageDialog(null, "Folder is missing at "+ PathFolder +",\nYou should create that folder. Just click ok", "Error Message", JOptionPane.ERROR_MESSAGE, null);

                Files.createDirectories(Path.of(this.PathFolder));

                createFile(this.PathSourceTXT);
                createFile(this.PathTargetTXT);

                String source = JOptionPane.showInputDialog("Insert your current location of mods", this.pathdefault);

                WriteFile(this.PathSourceTXT, source);

                String target = JOptionPane.showInputDialog("Insert your .minecraft/mods location", this.pathminecraft);

                WriteFile(this.PathTargetTXT, target);

                JOptionPane.showMessageDialog(null, "Set up is finished. Lets start", "Successfully", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
