package Exceptions;

public class FileCheckSumException extends FileSizeException{
    public FileCheckSumException(String message) {
        super(message);
    }
}
