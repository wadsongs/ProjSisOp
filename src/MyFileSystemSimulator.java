import java.io.*;
import java.util.*;

public class MyFileSystemSimulator {

    //Estrutura Principal
    private final String journalFile = "journal.log";
    private final File rootDirectory;

    public MyFileSystemSimulator(String rootPath) {
        rootDirectory = new File(rootPath);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }
        initializeJournal();
    }

    private void initializeJournal() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(journalFile, true))) {
            writer.write("=== File System Journal Initialized ===\n");
        } catch (IOException e) {
            System.err.println("Failed to initialize journal: " + e.getMessage());
        }
    }

    private void logOperation(String operation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(journalFile, true))) {
            writer.write(operation + "\n");
        } catch (IOException e) {
            System.err.println("Failed to log operation: " + e.getMessage());
        }
    }

    //Implementação de Operações

    //Cria diretórios
    public void createDirectory(String directoryPath) {
        File dir = new File(rootDirectory, directoryPath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                logOperation("CREATE DIRECTORY: " + dir.getAbsolutePath());
                System.out.println("Diretório Criado: " + dir.getAbsolutePath());
            } else {
                System.err.println("Falha ao criar o diretório: " + dir.getAbsolutePath());
            }
        } else {
            System.err.println("Diretório já existente: " + dir.getAbsolutePath());
        }
    }

    //Criar arquivos
    public void createFile(String filePath, String content) {
        File file = new File(rootDirectory, filePath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                logOperation("CREATE FILE: " + file.getAbsolutePath());
                System.out.println("Arquivo Criado: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Falha ao criar arquivo: " + e.getMessage());
            }
        } else {
            System.err.println("Arquivo já existente: " + file.getAbsolutePath());
        }
    }


    //Listar Conteúdo dos Diretórios
    public void listDirectory(String directoryPath) {
        File dir = new File(rootDirectory, directoryPath);
        if (dir.exists() && dir.isDirectory()) {
            String[] contents = dir.list();
            System.out.println("Conteúdo de: " + dir.getAbsolutePath() + ":");
            if (contents != null) {
                for (String content : contents) {
                    System.out.println(content);
                }
            }
        } else {
            System.err.println("Diretório inválido: " + dir.getAbsolutePath());
        }
    }

    //Apagar arquivos e Diretórios
    public void deleteFileOrDirectory(String path) {
        File target = new File(rootDirectory, path);
        if (target.exists()) {
            if (deleteRecursive(target)) {
                logOperation("DELETE: " + target.getAbsolutePath());
                System.out.println("Deletado: " + target.getAbsolutePath());
            } else {
                System.err.println("Falha ao deletar: " + target.getAbsolutePath());
            }
        } else {
            System.err.println("Arquivo ou diretório inexistente: " + target.getAbsolutePath());
        }
    }

    private boolean deleteRecursive(File file) {
        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File child : contents) {
                    deleteRecursive(child);
                }
            }
        }
        return file.delete();
    }

    //Renomear Arquivos e Diretórios
    public void rename(String oldPath, String newPath) {
        File oldFile = new File(rootDirectory, oldPath);
        File newFile = new File(rootDirectory, newPath);
        if (oldFile.exists() && !newFile.exists()) {
            if (oldFile.renameTo(newFile)) {
                logOperation("RENAME: " + oldFile.getAbsolutePath() + " TO " + newFile.getAbsolutePath());
                System.out.println("Renomeado: " + oldFile.getAbsolutePath() + " para " + newFile.getAbsolutePath());
            } else {
                System.err.println("Falha ao renomear: " + oldFile.getAbsolutePath());
            }
        } else {
            System.err.println("Operação de renomear inválida.");
        }
    }

    //Copiar Arquivos
    public void copyFile(String sourcePath, String destinationPath) {
        File source = new File(rootDirectory, sourcePath);
        File destination = new File(rootDirectory, destinationPath);

        if (source.exists() && source.isFile()) {
            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(destination)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                logOperation("COPY: " + source.getAbsolutePath() + " TO " + destination.getAbsolutePath());
                System.out.println("Arquivo copiado: " + source.getAbsolutePath() + " para " + destination.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Falha ao copiar arquivo: " + e.getMessage());
            }
        } else {
            System.err.println("Fonte do arquivo não existe ou arquivo inválido.");
        }
    }
}
