import java.nio.file.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import java.awt.Toolkit;

public class App {

    public ArrayList<String> FileName = new ArrayList<>();
    public ArrayList<String> FileLocation = new ArrayList<>();

    public App(){
        this.Render();
    }

    public static void main(String[] args) {
        new App();
    }

    public void Render() {
        try {
            String folderTarget = JOptionPane.showInputDialog("Copy alamat folder untuk mod");

            Path folderPath = Paths.get(folderTarget);

            Files.list(folderPath)
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(files -> {
                    FileName.add(files.getFileName().toString());
                    FileLocation.add(files.toString());
                });

            String[] pilihan = FileName.toArray(new String[0]);

            int file = JOptionPane.showOptionDialog(
                null,
                "pilih file untuk import ke mods",
                "Files Available",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                pilihan,
                null
            );

            if (file == -1) return; // user cancel

            Path targetDir = Paths.get("C:\\Users\\User\\AppData\\Roaming\\.tlauncher\\legacy\\Minecraft\\game\\mods");
            Files.createDirectories(targetDir);
            Path source = Paths.get(FileLocation.get(file));
            Path target = targetDir.resolve(source.getFileName());

            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Mod Jar sudah terimport ke minecraft", "Succesfully Done", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }
}