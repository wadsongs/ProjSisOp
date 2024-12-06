public class Main {
    public static void main(String[] args) {
        MyFileSystemSimulator fs = new MyFileSystemSimulator("virtual_fs");

        //Test operations
        fs.createDirectory("testDir");
        fs.createFile("testDir/sample.txt", "Hello World!");
        fs.listDirectory("testDir");
        fs.createDirectory("testDir/subDir");
        fs.listDirectory("testDir");

        fs.copyFile("testDir/sample.txt", "testDir/copy_sample.txt");
        fs.rename("testDir/copy_sample.txt", "testDir/renamed_sample.txt");

        fs.deleteFileOrDirectory("testDir");
    }
}
