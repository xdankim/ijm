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
        new Setup_();   
    }
}

class Setup_ extends FileSystem {
    public Setup_(){
        checkfolder();

        if (isLock) return;
        else {
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
    }
    
    public void checkfolder(){
        try {
            Path minecraft_folder = Path.of(minecraftdir);
            Path legacy_folder = Path.of(legacydir);

            if (Files.exists(minecraft_folder) && Files.exists(legacy_folder)) {
                JOptionPane.showInputDialog("Java cant read .minecraft and .tlauncher because you\nhave two folders of minecraft launcher. to avoid this happen, \n\nPlease report to me in github for releasing new version.", "https://github.com/xdankim/ijm/issues");
                isLock = true;
            }
            else if (Files.exists(minecraft_folder)) isOriginal(true);
            else if (Files.exists(legacy_folder)) isOriginal(false);
            else {
                JOptionPane.showMessageDialog(null,
                     "Java cant read the folder of .minecraft or .tlauncher because of:\n\n" 
                     + "1. You aren't install the original of minecraft\n"
                     + "2. You use tlauncher because this version only available for legacy launcher\n"
                     + "3. Different location. Java only read local disk which C:\\User\\...",
                     "Sorry, error was occured", 
                     JOptionPane.ERROR_MESSAGE, 
                     null);
            }
        } catch (Exception e) {}
    }

    public void isOriginal(Boolean bool){
        if (bool) build(pathdefault, minecraftdir); // original
        else build(pathdefault, insidelegacydir); // legacy
    }

    /**
     * 
     * @param Source
     * Folder sekarang dimana mods berada (boleh tukar)
     * @param Target
     * Folder untuk target
     */
    public void build(String Source, String Target){
        try {
            Path folder = Path.of(this.PathFolder);

            if (Files.exists(folder)) return;
            else {
                JOptionPane.showMessageDialog(null, "Folder is missing at "+ PathFolder +",\nYou should create that folder. Just click ok", "Warning", JOptionPane.WARNING_MESSAGE, null);

                Files.createDirectories(Path.of(this.PathFolder));

                createFile(this.PathSourceTXT);
                createFile(this.PathTargetTXT);

                String source = JOptionPane.showInputDialog("Insert your current location of mods", Source);

                WriteFile(this.PathSourceTXT, source);

                String target = JOptionPane.showInputDialog("Insert your .minecraft/mods location", Target + "\\mods");

                WriteFile(this.PathTargetTXT, target);

                JOptionPane.showMessageDialog(null, "Set up is finished. Lets start", "Successfully", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


class FileSystem {

    /**Nama File itu sendiri */
    public ArrayList<String> FileName = new ArrayList<>();
    /** Lokasi file berada */
    public ArrayList<String> PathFileLocation = new ArrayList<>();

    protected final static String home = System.getProperty("user.home");
    public final static String minecraftdir = home + "\\AppData\\Roaming\\.minecraft";
    public final static String legacydir = home + "\\AppData\\Roaming\\.tlauncher\\legacy";
    public final static String insidelegacydir = legacydir + "\\Minecraft\\game";
    public static Boolean isLock = false;

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