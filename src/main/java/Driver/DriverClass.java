package Driver;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import Core.Wrapper;

public class DriverClass {

    public static void main(String[] args) throws IOException {

        File f1 = null,f2 = null;
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String fileOnePath = "",fileTwoPath = "";
        String defaultPathOne = System.getProperty("user.dir") + "/src/main/resources/file1.txt";
        String defaultPathTwo = System.getProperty("user.dir") + "/src/main/resources/file2.txt";


        System.out.println("Please provide the File 1");
        fileOnePath = scanner.nextLine().trim();
        if(!fileOnePath.isEmpty()) {
            f1 = new File(fileOnePath);}
        else{
            f1 = new File(defaultPathOne);
            System.out.println("Considering default test file1 as source " + f1.getAbsoluteFile());
        }
        System.out.println("Please provide the File 2");
        fileTwoPath = scanner.nextLine().trim();
        if(!fileTwoPath.isEmpty()) {
            f2 = new File(fileTwoPath);
        }else{
            f2 = new File(defaultPathTwo);
            System.out.println("Considering default test file2 as source " + f2.getAbsoluteFile());
        }

        Wrapper<Object,Object> xyWrapper = new Wrapper<>();
        xyWrapper.getData(f1,f2);
    }
}
