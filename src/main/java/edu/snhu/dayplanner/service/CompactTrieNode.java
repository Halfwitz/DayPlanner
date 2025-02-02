package edu.snhu.dayplanner.service;

import java.util.*;
import java.util.stream.Collectors;

public class CompactTrieNode<T, F extends Enum<F>> {
    // Allows children node to be found through the associated character "Next letter".
    public Map<Character, CompactTrieNode<T, F>> children = new HashMap<>();
    public boolean isWordEnd = false; // denotes if this node is a complete word
    public Map<F, Set<T>> data; // represents a words associated Fields and all objects associated with that field.
    public String prefixPartial;

    public CompactTrieNode(String keyPartial) {
        this.prefixPartial = keyPartial;
        data = null;
    }
    public CompactTrieNode(String keyPartial, F field, T object) {
        this.prefixPartial = keyPartial;
        data = new HashMap<F, Set<T>>();
        addObject(object, field);
    }

    // removes an object associated with a specified field. If that field has no associations, removes field from data.
    public void removeObject(F field, T object) {
        Set<T> objects = data.get(field);
        if (objects != null) {
            objects.remove(object);
            if (objects.isEmpty()) {
                data.remove(field);
            }
        }
    }

    // adds an object associated with a specified field to this Node
    public void addObject(T object, F field) {
        // initialize hashmap if null
        if (data == null) {
            data = new HashMap<>();
        }
        // initialize arraylist if field is new, then adds object to the arraylist.
        data.computeIfAbsent(field, k -> new HashSet<T>()).add(object);
    }

    // returns a copy of the list of objects stored in this node associated with the specified field or null
    public Set<T> getData(F field) {
        if (field == null) {
            return data == null ? null : data.values().stream()
                    .flatMap(Set::stream)// combines each list in data to a single List
                    .collect(Collectors.toSet()); // convert stream to set
        } else return data == null ? null : data.get(field);
    }

    // returns all objects stored in this node regardless of associated field, without duplicates, or null.
    public Set<T> getData() {
        return getData(null);
    }

    @Override
    public String toString() {
        String ret = "(\"" + prefixPartial + "\"";
        if (data != null) {
            ret += " Data: " + data;
        }
        return ret + ")";
    }




}
