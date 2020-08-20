package tech.ikora.socket.client.server;

import tech.ikora.socket.client.model.Action;
import tech.ikora.socket.client.model.Dom;
import tech.ikora.socket.client.model.StackTrace;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    private static final char URL_CODE = 'u';
    private static final char STACK_CODE = 's';
    private static final char DOM_CODE = 'd';
    private static final char ARGUMENT_CODE = 'a';

    private static final Pattern byPattern = Pattern.compile("^\\[By\\.(.*):\\s(.*)]$");
    private static final Pattern stringPattern = Pattern.compile("^\\[(.*),\\s(.*)]$");

    public Action read(Socket socket){
        final Action action = new Action();

        try(DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))){
            final String url = readBlock(URL_CODE, in);
            final String arguments = readBlock(ARGUMENT_CODE, in);
            final String dom = readBlock(DOM_CODE, in);
            final String stackTrace = readBlock(STACK_CODE, in);

            final FindBy findBy = extractLocatorStrategy(arguments);

            action.setUrl(url);
            action.setLocator(findBy.getLocator());
            action.setStrategy(findBy.getStrategy());
            action.setDom(new Dom(dom));
            action.setStackTrace(new StackTrace(stackTrace));
        }catch(IOException e) {
            e.printStackTrace();
        }

        return action;
    }

    private String readBlock(final char code, final DataInputStream in) throws IOException {
        char dataType = in.readChar();
        int length = in.readInt();

        if(code != dataType){
            throw new IOException("Cannot interpret type: " + dataType);
        }

        byte[] messageByte = new byte[length];
        boolean end = false;

        StringBuilder dataString = new StringBuilder(length);
        int totalBytesRead = 0;

        while(!end) {
            int currentBytesRead = in.read(messageByte);

            totalBytesRead = currentBytesRead + totalBytesRead;

            dataString.append(new String(messageByte,
                    0,
                    totalBytesRead <= length ? currentBytesRead : length - totalBytesRead + currentBytesRead,
                    StandardCharsets.UTF_8));

            if(totalBytesRead >= length) {
                end = true;
            }
        }

        return dataString.toString();
    }

    private FindBy extractLocatorStrategy(String arguments){
        String strategy = "";
        String locator = "";

        final Matcher byMatcher = byPattern.matcher(arguments);

        if(byMatcher.matches()){
            strategy = byMatcher.group(1);
            locator = byMatcher.group(2);
        }
        else{
            final Matcher stringMatcher = stringPattern.matcher(arguments);

            if(stringMatcher.matches()){
                strategy = stringMatcher.group(1);
                locator = stringMatcher.group(2);
            }
        }

        return new FindBy(strategy, locator);
    }

    static class FindBy {
        private final String strategy;
        private final String locator;

        FindBy(String strategy, String locator) {
            this.strategy = strategy;
            this.locator = locator;
        }

        public String getStrategy() {
            return strategy;
        }

        public String getLocator() {
            return locator;
        }
    }
}
