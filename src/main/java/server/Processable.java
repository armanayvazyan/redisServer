package server;

@FunctionalInterface
public interface Processable {

    String process(String command[]);
}
