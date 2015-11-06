package preprocessor;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Alvin Natawiguna on 11/5/2015.
 */
public class StopWordFilter implements Filter {

    private File filterFile;

    private Set<String> filterSet = new HashSet<>();

    public StopWordFilter(String filename) throws IOException {
        filterFile = new File(filename);

        loadFilter();
    }

    private void loadFilter() throws IOException {
        BufferedReader is = new BufferedReader(new FileReader(filterFile));

        String line;
        while ((line = is.readLine()) != null) {
            filterSet.add(line);
        }

        is.close();
    }

    @Override
    public String filter(String str) {
        String strings[] = str.trim().toLowerCase().split(" ");

        List<String> filteredStrings = new LinkedList<>();
        for (String s: strings) {
            if (!filterSet.contains(s)) {
                filteredStrings.add(s);
            }
        }

        return String.join(" ", filteredStrings);
    }
}
