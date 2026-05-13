import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class App {

    private final String home = System.getProperty("user.home");
    private final String PathFolder = home + "\\Downloads\\.path";
    private final String PathSourceTXT = PathFolder + "\\source.txt";
    private final String PathTargetTXT = PathFolder + "\\target.txt";

    private App(){
        this.Render();
        
    }
    public static void main(String[] args) {
        new App();
    }

    private void Render(){
        try {

            checkFolder();

            ArrayList<String> PathFileList = new ArrayList<>();
            ArrayList<String> FileLocation = new ArrayList<>();

            Files.list(Paths.get(Files.readString(readFile(this.PathSourceTXT))))
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(files -> {
                    PathFileList.add(files.getFileName().toString());
                    FileLocation.add(files.toString());
                });

            String[] option = PathFileList.toArray(new String[0]);

            JComboBox<String> comboBox = new JComboBox<>(option);

            int result = JOptionPane.showConfirmDialog(null, comboBox, "Available Mods", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);


            if (result == JOptionPane.OK_OPTION){
                Path directory = Paths.get(Files.readString(readFile(this.PathTargetTXT)));
                Files.createDirectories(directory);

                Path target = directory.resolve(PathFileList.get(comboBox.getSelectedIndex()));
                
                Files.copy(readFile(FileLocation.get(comboBox.getSelectedIndex())), target);

                JOptionPane.showOptionDialog(null, "The Mods just imported to Minecraft", "Successfully", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            } else {
                JOptionPane.showOptionDialog(null, "Import Cancelled", "Cancelled", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
             
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void createFile(String File){
        try {
            new File(File);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void WriteFile(String File, String text){
        try {
            FileWriter writefile = new FileWriter(File);
            writefile.write(text);
            writefile.close();
        } catch (Exception e) {
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


                String source = JOptionPane.showInputDialog("Insert your current location of mods\nI recommend to copy the Downloads location");

                WriteFile(this.PathSourceTXT, source);

                String target = JOptionPane.showInputDialog("Insert your .minecraft/mods location\nexample : C:\\User\\...\\.minecraft\\mods");

                WriteFile(this.PathTargetTXT, target);

                JOptionPane.showMessageDialog(null, "Set up is finished. Lets start", "Successfully", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Path readFile(String File){
        return Paths.get(File);
    }

}
