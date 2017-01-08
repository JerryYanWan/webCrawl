import java.util.*;
import java.io.*;

public class TextProcess {
    public static void main(String[] args) throws IOException {
	
	BufferedReader f = new BufferedReader(new FileReader(args[0]));
	PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
	String separator = ",:;!?.'[]";
        String line = null;
        StringTokenizer st;
        while ((line = f.readLine()) != null) {
            System.out.println(line);
            st = new StringTokenizer(line, separator);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                out.println(token);
            }
	}

        out.close();
    }
}
