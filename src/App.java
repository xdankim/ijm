import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class App {

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

            Files.list(Paths.get(Files.readString(readFile("C:\\Users\\User\\Downloads\\.path\\source.txt"))))
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(files -> {
                    PathFileList.add(files.getFileName().toString());
                    FileLocation.add(files.toString());
                });

            String[] option = PathFileList.toArray(new String[0]);

            JComboBox<String> comboBox = new JComboBox<>(option);

            int result = JOptionPane.showConfirmDialog(null, comboBox, "Mods Available", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);


            if (result == JOptionPane.OK_OPTION){
                Path directory = Paths.get(Files.readString(readFile("C:\\Users\\User\\Downloads\\.path\\target.txt")));
                Files.createDirectories(directory);

                Path target = directory.resolve(PathFileList.get(comboBox.getSelectedIndex()));
                
                Files.copy(readFile(FileLocation.get(comboBox.getSelectedIndex())), target);

                JOptionPane.showOptionDialog(null, "Import is done", "Successfully", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            } else {
                JOptionPane.showOptionDialog(null, "Import cancelled", "Successfully", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
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
            Path folder = Path.of("C:\\Users\\User\\Downloads\\.path");

            if (Files.exists(folder)) return;
            else {
                JOptionPane.showMessageDialog(null, "File is missing 'C:\\Users\\User\\Downloads\\.path',\nYou should create this folder to use it!", "Error Message", JOptionPane.ERROR_MESSAGE, null);

                Files.createDirectories(Path.of("C:\\Users\\User\\Downloads\\.path"));

                createFile("C:\\Users\\User\\Downloads\\.path\\source.txt");
                createFile("C:\\Users\\User\\Downloads\\.path\\target.txt");


                String source = JOptionPane.showInputDialog("Paste your mods location");

                WriteFile("C:\\Users\\User\\Downloads\\.path\\source.txt", source);

                String target = JOptionPane.showInputDialog("Paste your .minecraft/mods folder as a default");

                WriteFile("C:\\Users\\User\\Downloads\\.path\\target.txt", target);

                JOptionPane.showMessageDialog(null, "Setup is finished! let start", "Successfully", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Path readFile(String File){
        return Paths.get(File);
    }

}
