package com.osa.utility;

import com.osa.api.UsersValues;
import io.cucumber.datatable.DataTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Keeps original BOD/VAL -> ArrayList<UsersValues> -> HashMap design. */
public class DataTableHelper {
    public HashMap<String, List<UsersValues>> verifyResponse(DataTable data) {
        List<UsersValues> body = new ArrayList<>();
        List<UsersValues> validation = new ArrayList<>();
        HashMap<String, List<UsersValues>> userInput = new HashMap<>();
        List<List<String>> rows = data.asLists(String.class);
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).size() != 3)
                throw new IllegalArgumentException("Expected 3 columns at row " + i);
            String type = rows.get(i).get(0).trim();
            String field = rows.get(i).get(1).trim();
            String value = rows.get(i).get(2);
            if (type.equalsIgnoreCase("BOD"))
                body.add(new UsersValues(field, value));
            else if (type.equalsIgnoreCase("VAL"))
                validation.add(new UsersValues(field, value));
            else
                throw new IllegalArgumentException("Unknown row type: " + type);
        }
        userInput.put("BOD", body);
        userInput.put("VAL", validation);
        return userInput;
    }
}
